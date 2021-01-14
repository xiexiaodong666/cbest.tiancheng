package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.BarcodeUtil;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.BarcodeSaltDao;
import com.welfare.persist.entity.BarcodeSalt;
import com.welfare.service.BarcodeService;
import com.welfare.service.dto.payment.PaymentBarcode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/8/2021
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class BarcodeServiceImpl implements BarcodeService {
    /**
     * 最多生成多少period的saltValue
     */
    private static final Long MAX_PERIOD_GENERATE = 30L;
    private static final int MILL_SEC_A_DAY = (86400 * 1000);
    private static final String BARCODE_PREFIX = "e-welfare-barcode";
    @Value("${e-welfare.barcode.expire:60}")
    private Long barcodeExpireSecs;

    private final BarcodeSaltDao barcodeSaltDao;
    private final RedissonClient redissonClient;
    /**
     * redisAutoConfiguration会在ioc中放入redisTemplate和stringRedisTemplate两个bean，
     * 所以这里不能用构造器注入，而是应该用名字去注入
     */
    @Resource
    private RedisTemplate<String,PaymentBarcode> redisTemplate;
    @Override
    public List<BarcodeSalt> querySalt(Long fromValidPeriodNumeric) {
        QueryWrapper<BarcodeSalt> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(BarcodeSalt.VALID_PERIOD_NUMERIC,fromValidPeriodNumeric);
        List<BarcodeSalt> barcodeSalts = barcodeSaltDao.list(queryWrapper);
        generateSaltIfNeeded(barcodeSalts);
        return barcodeSalts;
    }

    private void generateSaltIfNeeded(List<BarcodeSalt> barcodeSalts) {
        if(barcodeSalts.size() < MAX_PERIOD_GENERATE){
            //全局只能有一个线程在执行生成逻辑
            RLock lock = redissonClient.getFairLock(RedisKeyConstant.GENERATE_BARCODE_SALT_LOCK);
            lock.lock();
            try{
                //让事务生效,直接this调用没有aop织入事务
                BarcodeService bean = SpringBeanUtils.getBean(BarcodeService.class);
                bean.batchGenerateSalt();
            }finally {
                lock.unlock();
            }
        }
    }

    @Override
    public BarcodeSalt generateSalt(Long targetValidPeriod) {
        QueryWrapper<BarcodeSalt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BarcodeSalt.VALID_PERIOD_NUMERIC,targetValidPeriod);
        BarcodeSalt barcodeSalt = barcodeSaltDao.getOne(queryWrapper);
        if(!Objects.isNull(barcodeSalt)){
            return barcodeSalt;
        }else{
            barcodeSalt = new BarcodeSalt();
            Long saltValue = BarcodeUtil.nextRandSalt();
            barcodeSalt.setSaltValue(saltValue);
            barcodeSalt.setValidPeriod(targetValidPeriod.toString());
            barcodeSalt.setValidPeriodNumeric(targetValidPeriod);
            boolean isSaved = barcodeSaltDao.save(barcodeSalt);
            Assert.isTrue(isSaved,"保存失败");
            return barcodeSalt;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchGenerateSalt(){
        BarcodeSalt maxPeriodBarcodeSalt = getTheLatestSaltInDb();
        Date maxPeriod = BarcodeUtil.parsePeriodToDate(maxPeriodBarcodeSalt.getValidPeriod());
        long days = (maxPeriod.getTime() - Calendar.getInstance().getTime().getTime()) / MILL_SEC_A_DAY;
        if(days < MAX_PERIOD_GENERATE){
            //循环生成缺失的period
            for (int i = 0; i < MAX_PERIOD_GENERATE - days; i++) {
                Date theDayAfterMax = DateUtils.addDays(maxPeriod, i);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String targetPeriodStr = dateFormat.format(theDayAfterMax);
                generateSalt(Long.valueOf(targetPeriodStr));
            }
        }
    }

    @Override
    public BarcodeSalt queryCurrentPeriodSaltValue() {
        Long period = BarcodeUtil.currentAsPeriod();
        return querySaltByPeriodNumeric(period);
    }

    @Override
    public BarcodeSalt querySaltByPeriodNumeric(Long period) {
        QueryWrapper<BarcodeSalt> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(BarcodeSalt.VALID_PERIOD, period);
        return barcodeSaltDao.getOne(queryWrapper);
    }

    @Override
    public PaymentBarcode getBarcode(Long accountCode) {
        BarcodeSalt barcodeSalt =  queryCurrentPeriodSaltValue();
        PaymentBarcode paymentBarcode = PaymentBarcode.of(accountCode, barcodeSalt.getSaltValue());
        redisTemplate.opsForValue().set(
                BARCODE_PREFIX + paymentBarcode.getBarcode(),
                paymentBarcode,barcodeExpireSecs,
                TimeUnit.SECONDS
        );
        return paymentBarcode;
    }

    @Override
    public Long parseAccountFromBarcode(String barcode, boolean isOffline) {
        if(!isOffline){
            PaymentBarcode paymentBarcode = redisTemplate.opsForValue().get(BARCODE_PREFIX + barcode);
            Assert.notNull(paymentBarcode,"条码过期或不存在");
        }
        BarcodeSalt barcodeSalt = queryCurrentPeriodSaltValue();
        Long accountCode = BarcodeUtil.calculateAccount(barcode, barcodeSalt.getSaltValue());
        return accountCode;
    }

    private BarcodeSalt getTheLatestSaltInDb() {
        Long currentPeriod = BarcodeUtil.currentAsPeriod();
        QueryWrapper<BarcodeSalt> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(BarcodeSalt.VALID_PERIOD_NUMERIC,currentPeriod)
                .orderByDesc(BarcodeSalt.VALID_PERIOD_NUMERIC)
                .last("limit 1");
        BarcodeSalt maxPeriodBarcodeSalt = barcodeSaltDao.getOne(queryWrapper);
        return maxPeriodBarcodeSalt;
    }
}
