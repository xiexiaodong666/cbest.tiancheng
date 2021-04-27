package com.welfare.persist.dto.settlement.wholesale;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/27/2021
 */
@Data
@ApiModel("平台批发应收结算明细")
public class PlatformWholesaleSettleDetailDTO {
    @ApiModelProperty("结算明细ID")
    private String id;

    @ApiModelProperty("交易单号")
    @ExcelProperty("交易单号")
    private String transNo;

    @ApiModelProperty("订单号")
    @ExcelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("交易时间")
    @ExcelProperty("交易时间")
    private Date transTime;

    @ApiModelProperty("账户编码")
    private Long accountCode;

    @ApiModelProperty("账户名称")
    @ExcelProperty("账户名称")
    private String accountName;

    @ApiModelProperty("电话号码")
    @ExcelProperty("电话号码")
    private String phone;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

    @ApiModelProperty("供应商名称")
    @ExcelProperty("供应商名称")
    private String supplierName;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("门店名称")
    @ExcelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("交易金额")
    @ExcelProperty("交易金额")
    private BigDecimal transAmount;

    @ApiModelProperty("交易类型")
    private String transType;

    @ApiModelProperty("结算金额")
    @ExcelProperty("结算金额")
    private BigDecimal settleAmount;

    @ApiModelProperty("结算状态")
    @ExcelProperty("结算状态")
    private String settleFlag;
}
