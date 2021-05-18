package com.welfare.persist.dto.settlement.wholesale;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
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
@ExcelIgnoreUnannotated
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

    @ApiModelProperty("消费人")
    @ExcelProperty("消费人")
    private String accountName;

    @ApiModelProperty("手机号q")
    @ExcelProperty("手机号")
    private String phone;

    @ApiModelProperty("客户商户编码")
    private String customerMerCode;

    @ApiModelProperty("客户商户名称")
    @ExcelProperty("客户商户名称")
    private String customerMerName;

    @ApiModelProperty("门店编码")
    private String storeCode;

    @ApiModelProperty("门店名称")
    @ExcelProperty("门店名称")
    private String storeName;

    @ApiModelProperty("销售金额")
    @ExcelProperty("销售金额")
    private BigDecimal saleAmount;

    @ApiModelProperty("交易类型")
    private String transType;

    @ApiModelProperty("结算金额")
    @ExcelProperty("结算金额")
    private BigDecimal settleAmount;

    @ApiModelProperty("营收金额")
    @ExcelProperty("营收金额")
    private BigDecimal revenueAmount;

    @ApiModelProperty("结算状态")
    private String settleFlag;

    @ExcelProperty("结算状态")
    private String settleFlagName;
}
