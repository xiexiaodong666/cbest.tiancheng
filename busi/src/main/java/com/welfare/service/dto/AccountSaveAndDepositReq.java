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
     * 商户CODE
     */
    @ApiModelProperty(value = "商户CODE",required = true)
    @NotEmpty(message = "商户CODE为空")
    private String merCode;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "员工手机号码",required = true)
    @NotEmpty(message = "员工手机号码为空")
    @Pattern(regexp = "^1\\d{10}$", message = "手机号不合法")
    private String phone;

    /**
     * 账号状态
     */
    @ApiModelProperty(value = "账号状态 1正常 2禁用",required = true)
    @NotNull(message = "账号状态为空")
    private Integer accountStatus;

    /**
     * 员工类型编码名称
     */
    @ApiModelProperty(value = "员工类型编码",required = true)
    @NotEmpty(message = "员工类型编码为空")
    private String accountTypeCode;

    /**
     * 所属部门
     */
    @ApiModelProperty(value = "所属部门Code",required = true)
    @NotEmpty(message = "所属部门为空")
    private String departmentCode;

    /**
     * 福利类型
     */
    @ApiModelProperty(value = "福利类型", required = true)
    @NotEmpty(message = "余额类型为空")
    private String merAccountTypeCode;

    /**
     * 充值金额（不能小于零)
     */
    @ApiModelProperty(value = "充值金额", required = true)
    @DecimalMin(value = "0", message = "充值金额不能小于零")
    private BigDecimal amount;

    /**
     * 是否授信
     */
    @ApiModelProperty("是否授信,1是 0否")
    private Boolean credit;

    /**
     * 最大授权额度
     */
    @ApiModelProperty("最大授权额度")
    private BigDecimal maxQuota;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
