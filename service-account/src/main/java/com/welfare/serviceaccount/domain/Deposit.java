package com.welfare.serviceaccount.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Data
@ApiModel("充值请求")
public class Deposit {
    @ApiModelProperty("请求id")
    private String requestId;
    @ApiModelProperty("账户")
    private String accountCode;
    @ApiModelProperty("充值卡号")
    private String cardNo;
    @ApiModelProperty("充值金额")
    private BigDecimal amount;
    @ApiModelProperty("充值来源")
    private String depositSource;
    @ApiModelProperty(value = "充值状态",notes = "1:新增, 2:处理中, 3:处理成功 -1:处理失败")
    private Integer depositStatus;
}
