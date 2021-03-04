package com.welfare.service.settlement.impl;

import com.alibaba.fastjson.JSONObject;
import com.welfare.common.constants.RedisKeyConstant;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DateUtil;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.persist.dao.EmployeeSettleDetailDao;
import com.welfare.persist.entity.EmployeeSettleDetail;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.persist.mapper.EmployeeSettleDetailMapper;
import com.welfare.service.settlement.EmployeeSettleDetailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.welfare.common.constants.RedisKeyConstant.MER_ACCOUNT_TYPE_OPERATE;

/**
 * 服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeSettleDetailServiceImpl implements EmployeeSettleDetailService {
    private final EmployeeSettleDetailDao employeeSettleDetailDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pullAccountDetailByDate(Date date) {
        String dateStr = DateUtil.date2Str(date, DateUtil.DEFAULT_DATE_FORMAT);
        RLock lock = DistributedLockUtil.lockFairly(RedisKeyConstant.buidKey(RedisKeyConstant.PULL_EMPLOYEE_SETTLE_DETAIL, dateStr));
        EmployeeSettleDetailMapper employeeSettleDetailMapper = employeeSettleDetailDao.getBaseMapper();
        try {
            Date dateStart = DateUtil.getDayMin(date, -1);
            Date dateEnd = DateUtil.getDayMax(date, -1);
            if (employeeSettleDetailDao.countByTransTime(dateStart, dateEnd) > 0) {
                log.info("日期:{}的员工授信流水数据已经同步过了", dateStr);
                return;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("startTime", dateStart);
            params.put("endTime", dateEnd);
            params.put("minId", 0);
            params.put("limit", 2000);
            do {
                log.info("employeeSettleDetailMapper循employeeSettleDetailMapper循环拉取账户授信详细交易数据环拉取账户授信详细交易数据，请求参数：{}", JSONObject.toJSONString(params));
                List<EmployeeSettleDetail> settleDetails = employeeSettleDetailMapper.getFromAccountDetail(params);
                if (!settleDetails.isEmpty()) {
                    params.put("minId", settleDetails.get(settleDetails.size() - 1).getId() + 1);
                    employeeSettleDetailDao.saveBatch(settleDetails);
                }
            } while (true);
        } catch (Exception e) {
            log.error("同步{}的员工授信流水数据失败", dateStr, e);
            throw new BusiException(String.format("同步%s的员工授信流水数据失败", dateStr));
        } finally {
            DistributedLockUtil.unlock(lock);
        }
    }
}