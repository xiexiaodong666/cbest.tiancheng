package com.welfare.persist.entity;

import java.beans.Transient;
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
 * 账户充值申请(account_deposit_apply)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_deposit_apply")
@ApiModel("账户充值申请")
public class AccountDepositApply extends Model<AccountDepositApply> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 申请编码
     */
    @ApiModelProperty("申请编码")   
    private String applyCode;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 充值账户个数
     */
    @ApiModelProperty("充值账户个数")   
    private Integer rechargeNum;
    /**
     * 申请充值总额
     */
    @ApiModelProperty("申请充值总额")   
    private BigDecimal rechargeAmount;
    /**
     * 充值状态
     */
    @ApiModelProperty("充值状态")   
    private String rechargeStatus;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String createUser;
    /**
     * 创建日期
     */
    @ApiModelProperty("创建日期")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateUser;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")   
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Integer version;
    /**
     * 审批状态
     */
    @ApiModelProperty("审批状态")   
    private String approvalStatus;
    /**
     * 审批人
     */
    @ApiModelProperty("审批人")   
    private String approvalUser;
    /**
     * 审批时间
     */
    @ApiModelProperty("审批时间")   
    private Date approvalTime;
    /**
     * 审批备注
     */
    @ApiModelProperty("审批备注")   
    private String approvalRemark;
    /**
     * 删除标志  1-删除、0-未删除
     */
    @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Boolean deleted;
    /**
     * 申请备注
     */
    @ApiModelProperty("申请备注")   
    private String applyRemark;
    /**
     * 申请人
     */
    @ApiModelProperty("申请人")   
    private String applyUser;
    /**
     * 审批意见
     */
    @ApiModelProperty("审批意见")   
    private String approvalOpinion;
    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")   
    private Date applyTime;
    /**
     * 请求类型
     */
    @ApiModelProperty("请求类型")   
    private String approvalType;
    /**
     * requestId
     */
    @ApiModelProperty("requestId")   
    private String requestId;
    /**
     * merAccountTypeCode
     */
    @ApiModelProperty("merAccountTypeCode")   
    private String merAccountTypeCode;

    @ApiModelProperty("渠道")
    private String channel;
//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 申请编码
    */
    public static final String APPLY_CODE = "apply_code";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 充值账户个数
    */
    public static final String RECHARGE_NUM = "recharge_num";
    /**
    * 申请充值总额
    */
    public static final String RECHARGE_AMOUNT = "recharge_amount";
    /**
    * 充值状态
    */
    public static final String RECHARGE_STATUS = "recharge_status";
    /**
    * 创建人
    */
    public static final String CREATE_USER = "create_user";
    /**
    * 创建日期
    */
    public static final String CREATE_TIME = "create_time";
    /**
    * 更新人
    */
    public static final String UPDATE_USER = "update_user";
    /**
    * 更新日期
    */
    public static final String UPDATE_TIME = "update_time";
    /**
    * 版本
    */
    public static final String VERSION = "version";
    /**
    * 审批状态
    */
    public static final String APPROVAL_STATUS = "approval_status";
    /**
    * 审批人
    */
    public static final String APPROVAL_USER = "approval_user";
    /**
    * 审批时间
    */
    public static final String APPROVAL_TIME = "approval_time";
    /**
    * 审批备注
    */
    public static final String APPROVAL_REMARK = "approval_remark";
    /**
    * 删除标志  1-删除、0-未删除
    */
    public static final String DELETED = "deleted";
    /**
    * 申请备注
    */
    public static final String APPLY_REMARK = "apply_remark";
    /**
    * 申请人
    */
    public static final String APPLY_USER = "apply_user";
    /**
    * 审批意见
    */
    public static final String APPROVAL_OPINION = "approval_opinion";
    /**
    * 申请时间
    */
    public static final String APPLY_TIME = "apply_time";
    /**
    * 请求类型
    */
    public static final String APPROVAL_TYPE = "approval_type";
    /**
    * 
    */
    public static final String REQUEST_ID = "request_id";
    /**
    * 
    */
    public static final String MER_ACCOUNT_TYPE_CODE = "mer_account_type_code";
    /**
     * 渠道
     */
    public static final String CHANNEL = "channel";
}