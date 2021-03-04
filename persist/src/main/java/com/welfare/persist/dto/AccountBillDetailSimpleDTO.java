package com.welfare.persist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@ApiModel("员工卡首页账号交易明细")
public class AccountBillDetailSimpleDTO {

    @ApiModelProperty("消费门店代码")
    private String storeCode;

    @ApiModelProperty("消费门店名称")
    private String storeName;

    @ApiModelProperty("交易类型")
    private String transType;

    @ApiModelProperty("交易类型名称")
    private String transTypeName;

    @ApiModelProperty("交易总金额")
    private BigDecimal transAmount;

    @ApiModelProperty("交易时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date transTime;

    @ApiModelProperty("渠道")
    private String channel;

    @ApiModelProperty("账户类型名称")
    private String merAccountTypeCode;

    @ApiModelProperty("账户类型名称")
    private String merAccountTypeName;

    @ApiModelProperty("交易流水号")
    private String transNo;
}
