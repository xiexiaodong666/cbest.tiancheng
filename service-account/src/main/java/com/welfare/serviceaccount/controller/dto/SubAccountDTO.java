package com.welfare.serviceaccount.controller.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@ApiModel("子账户信息")
@Data
public class SubAccountDTO implements Serializable {
    @ApiModelProperty(value = "账户号",required = true)
    @NotNull(message = "accountCode can not be null")
    private Long accountCode;
    @ApiModelProperty(value = "支付渠道",required = true)
    @NotNull(message = "paymentChannel can not be null")
    private String paymentChannel;
    @ApiModelProperty(value = "免密签名",required = true)
    @NotNull(message = "passwordFreeSignature can not be null")
    private String passwordFreeSignature;

}
