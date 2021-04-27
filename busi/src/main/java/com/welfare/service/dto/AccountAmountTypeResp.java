package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/25 5:30 下午
 */
@Data
public class AccountAmountTypeResp {

    /**
     * 账户编码
     */
    @ApiModelProperty("账户编码")
    private Long accountCode;
    /**
     * 商家账户类型
     */
    @ApiModelProperty("商家账户类型")
    private String merAccountTypeCode;
    /**
     * 商家账户类型
     */
    @ApiModelProperty("商家账户类型")
    private String merAccountTypeName;
    /**
     * 余额
     */
    @ApiModelProperty("余额(100 或 10/100)")
    private String balance;

    @ApiModelProperty("排序")
    private Integer showOrder;
}
