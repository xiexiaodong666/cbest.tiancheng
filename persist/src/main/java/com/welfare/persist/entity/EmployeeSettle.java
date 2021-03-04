package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商户员工结算账单(employee_settle)实体类
 *
 * @author Yuxiang Li
 * @since 2021-03-03 17:44:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("employee_settle")
@ApiModel("商户员工结算账单")
public class EmployeeSettle extends Model<EmployeeSettle> implements Serializable {
    private static final long serialVersionUID = 1L;

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
     * 账单周期
     */
    @ApiModelProperty("账单周期")   
    private String settlePeriod;
    /**
     * 账户
     */
    @ApiModelProperty("账户")
    private Integer accountCode;
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
     * 交易笔数
     */
    @ApiModelProperty("交易笔数")   
    private Integer orderNum;
    /**
     * 结算状态（待结算-unsettled；已结算-settled）
     */
    @ApiModelProperty("结算状态（待结算-unsettled；已结算-settled）")   
    private String settleStatus;
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
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    @TableField(fill = FieldFill.INSERT)
	private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")   
    @TableField(fill = FieldFill.INSERT)
	private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    private String uppdateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
    @TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 账单编号
    */
    public static final String SETTLE_NO = "settle_no";
    /**
    * 账单周期
    */
    public static final String SETTLE_PERIOD = "settle_period";
    /**
    * 商户代码
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 交易金额
    */
    public static final String TRANS_AMOUNT = "trans_amount";
    /**
    * 结算金额
    */
    public static final String SETTLE_AMOUNT = "settle_amount";
    /**
    * 交易笔数
    */
    public static final String ORDER_NUM = "order_num";
    /**
    * 结算状态（待结算-unsettled；已结算-settled）
    */
    public static final String SETTLE_STATUS = "settle_status";
    /**
    * 发送时间
    */
    public static final String SEND_TIME = "send_time";
    /**
    * 确定时间
    */
    public static final String CONFIRM_TIME = "confirm_time";
    /**
    * 账单开始时间
    */
    public static final String SETTLE_START_TIME = "settle_start_time";
    /**
    * 账单结束时间
    */
    public static final String SETTLE_END_TIME = "settle_end_time";
    /**
    * 创建人
    */
    public static final String CREATE_USER = "create_user";
    /**
    * 创建时间
    */
    public static final String CREATE_TIME = "create_time";
    /**
    * 更新人
    */
    public static final String UPPDATE_USER = "uppdate_user";
    /**
    * 更新时间
    */
    public static final String UPDATE_TIME = "update_time";
    /**
    * 删除标志
    */
    public static final String DELETED = "deleted";

}