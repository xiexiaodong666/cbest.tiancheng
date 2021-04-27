package com.welfare.service.dto.payment;

import com.welfare.common.exception.BizException;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.service.BarcodeService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = true)
@ApiModel("扫码支付请求")
@Data
public class BarcodePaymentRequest extends PaymentRequest {
    @ApiModelProperty(value = "条码",required = true)
    private String barcode;
    @ApiModelProperty(value = "扫描日期，yyyy-MM-ddTHH:mm:ss+08:00,例:2021-01-01T12:00:00+08:00",required = true)
    private Date scanDate;
    @ApiModelProperty("支付渠道，甜橙:welfare 联通:wo_life 微信:wechat 支付宝:alipay")
    private String paymentChannel;


    @Override
    public Long calculateAccountCode(){
        if(!Objects.isNull(super.getAccountCode())){
            return super.getAccountCode();
        }
        this.paymentChannel = calculatePaymentChannelByBarcode(this.barcode);
        BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
        RedisTemplate<String,String> redisTemplate = SpringBeanUtils.getBean(StringRedisTemplate.class);
        String expireSecs = SpringBeanUtils.getApplicationContext()
                .getEnvironment()
                .getProperty("e-welfare.barcode.expire-for-pay", "240");
        String barcodeInRedis = redisTemplate.opsForValue().get("BARCODE:" + barcode);
        if(!StringUtils.isEmpty(barcodeInRedis) && !isNotification() && !getOffline()){
            //在不是“支付成功通知”的情况下，需要校验支付码是否已经被使用过了
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS,"该支付码已被使用",null);
        }else{
            redisTemplate.opsForValue().set("BARCODE:"+barcode,barcode,Long.parseLong(expireSecs), TimeUnit.SECONDS);
        }
        Long accountCode = barcodeService.parseAccountFromBarcode(barcode, scanDate,getOffline(), isNotification());
        this.setAccountCode(accountCode);
        return accountCode;
    }

    public static String calculatePaymentChannelByBarcode(final String barcode) {
        if(barcode.startsWith(WelfareConstant.PaymentChannel.WELFARE.barcodePrefix())){
            return WelfareConstant.PaymentChannel.WELFARE.code();
        }else if(barcode.startsWith(WelfareConstant.PaymentChannel.WO_LIFE.barcodePrefix())){
            return WelfareConstant.PaymentChannel.WO_LIFE.code();
        }else if(barcode.startsWith(WelfareConstant.PaymentChannel.WECHAT.barcodePrefix())){
            return WelfareConstant.PaymentChannel.WECHAT.code();
        }else if(barcode.startsWith(WelfareConstant.PaymentChannel.ALIPAY.barcodePrefix())){
            return WelfareConstant.PaymentChannel.ALIPAY.code();
        }else{
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "条码不符合规则", null);
        }
    }


}
