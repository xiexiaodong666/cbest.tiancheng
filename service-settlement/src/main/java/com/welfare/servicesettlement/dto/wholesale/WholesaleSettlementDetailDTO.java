package com.welfare.servicesettlement.dto.wholesale;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
 * @date 4/26/2021
 */
@Data
@ApiModel("批发结算明细DTO")
public class WholesaleSettlementDetailDTO {
    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Integer id;
    /**
     * 账单编号
     */
    @ApiModelProperty("账单编号")
    private String settleNo;
    /**
     * 订单编码
     */
    @ApiModelProperty("订单编码")
    private String orderId;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")
    @ExcelProperty("交易流水号")
    private String transNo;
    /**
     * 账户
     */
    @ApiModelProperty("账户")
    @ExcelProperty("消费人编码")
    private Integer accountCode;
    /**
     * 账户名称
     */
    @ApiModelProperty("账户名称")
    @ExcelProperty("消费人")
    private String accountName;

    /**
     * 门店编码
     */
    @ApiModelProperty("门店编码")
    @ExcelProperty("消费门店编码")
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")
    @ExcelProperty("消费门店名称")
    private String storeName;
    /**
     * 交易时间
     */
    @ApiModelProperty("交易时间")
    @ExcelProperty("交易时间")
    private Date transTime;

    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")
    @ExcelProperty("消费金额")
    private BigDecimal transAmount;

    @ApiModelProperty("结算状态")
    @ExcelProperty("结算状态")
    private String settleFlag;

    /**
     * 结算金额
     */
    @ApiModelProperty("结算金额")
    private BigDecimal settleAmount;
}
