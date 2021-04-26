package com.welfare.servicesettlement.dto.wholesale;

import com.baomidou.mybatisplus.annotation.TableId;
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
@ApiModel("批发结算DTO")
public class WholesaleSettlementDTO {
    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
    private Long id;
    /**
     * 账单编号
     */
    @ApiModelProperty("账单编号")
    private String settleNo;
    /**
     * 账单月
     */
    @ApiModelProperty("账单月")
    private String settleMonth;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    private String merCode;
    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")
    private BigDecimal transAmount;
    /**
     * 结算金额
     */
    @ApiModelProperty("结算金额")
    private BigDecimal settleAmount;
    /**
     * 结算的自费额度
     */
    @ApiModelProperty("结算的自费额度")
    private BigDecimal settleSelfAmount;
    /**
     * 返利金额
     */
    @ApiModelProperty("返利金额")
    private BigDecimal rebateAmount;
    /**
     * 交易笔数
     */
    @ApiModelProperty("交易笔数")
    private Integer orderNum;
    /**
     * 对账状态（待确认-unconfirmed；已确认-confirmed）
     */
    @ApiModelProperty("对账状态（待确认-unconfirmed；已确认-confirmed）")
    private String recStatus;
    /**
     * 结算状态（待结算-unsettled；已结算-settled）
     */
    @ApiModelProperty("结算状态（待结算-unsettled；已结算-settled）")
    private String settleStatus;
    /**
     * 发送状态（待发送-unsended；已发送-sended）
     */
    @ApiModelProperty("发送状态（待发送-unsended；已发送-sended）")
    private String sendStatus;
    /**
     * 发送时间
     */
    @ApiModelProperty("发送时间")
    private Date sendTime;
    /**
     * 确定时间
     */
    @ApiModelProperty("确定时间")
    private Date confirmTime;
    /**
     * 账单开始时间
     */
    @ApiModelProperty("账单开始时间")
    private Date settleStartTime;
    /**
     * 账单结束时间
     */
    @ApiModelProperty("账单结束时间")
    private Date settleEndTime;
}
