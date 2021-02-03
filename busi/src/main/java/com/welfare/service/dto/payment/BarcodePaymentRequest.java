package com.welfare.service.dto.payment;

import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.common.util.StringUtil;
import com.welfare.service.BarcodeService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@ApiModel("扫码支付请求")
@Data
public class BarcodePaymentRequest extends PaymentRequest {
    @ApiModelProperty(value = "条码",required = true)
    private String barcode;
    @ApiModelProperty(value = "扫描日期，yyyy-MM-ddTHH:mm:ss+08:00,例:2021-01-01T12:00:00+08:00",required = true)
    private Date scanDate;

    @Override
    public Long calculateAccountCode(){
        if(!Objects.isNull(super.getAccountCode())){
            return super.getAccountCode();
        }
        BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
        RedisTemplate<String,String> redisTemplate = SpringBeanUtils.getBean(StringRedisTemplate.class);
        String expireSecs = SpringBeanUtils.getApplicationContext()
                .getEnvironment()
                .getProperty("e-welfare.barcode.expire", "210");
        String barcodeInRedis = redisTemplate.opsForValue().get("BARCODE:" + barcode);
        if(!StringUtils.isEmpty(barcodeInRedis)){
            throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"该支付码已被使用",null);
        }else{
            redisTemplate.opsForValue().set("BARCODE:"+barcode,barcode,Long.parseLong(expireSecs), TimeUnit.SECONDS);
        }
        Long accountCode = barcodeService.parseAccountFromBarcode(barcode, scanDate,getOffline());
        this.setAccountCode(accountCode);
        return accountCode;
    }


}
