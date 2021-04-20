package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.math.BigDecimal;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/20 1:34 下午
 */
@Data
public class AccountSaveAndDepositReq {

    /**
     * 员工名称
     */
    @ApiModelProperty("员工名称")
    @NotEmpty(message = "员工名称为空")
    private String accountName;

    /**
     * 商户CODE
     */
    @ApiModelProperty("商户CODE")
    @NotEmpty(message = "商户CODE为空")
    private String merCode;
    /**
     * 员工账号
     */
    @ApiModelProperty("员工手机号码")
    @NotEmpty(message = "员工手机号码为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号不合法")
    private String phone;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
