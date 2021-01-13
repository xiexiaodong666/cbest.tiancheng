package com.welfare.service.dto.payment;

import com.welfare.common.util.SpringBeanUtils;
import com.welfare.service.BarcodeService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/6/2021
 */
@ApiModel("线上支付请求")
@Data
public class OnlinePaymentRequest extends PaymentRequest {


    @Override
    public Long calculateAccountCode(){
        return super.getAccountCode();
    }
}
