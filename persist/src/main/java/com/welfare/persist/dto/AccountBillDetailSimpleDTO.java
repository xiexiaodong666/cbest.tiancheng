package com.welfare.persist.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

    @ApiModelProperty("交易总金额")
    private BigDecimal transAmount;

    @ApiModelProperty("交易时间")
    private Date transTime;

    @ApiModelProperty("扣款详情")
    private List<AccountBillDetailSimpleDeductionDTO> deductionList;
}
