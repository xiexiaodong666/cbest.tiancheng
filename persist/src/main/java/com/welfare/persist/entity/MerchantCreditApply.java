package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 商户金额申请(merchant_credit_apply)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_credit_apply")
@ApiModel("商户金额申请")
public class MerchantCreditApply extends Model<MerchantCreditApply> implements Serializable {
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
     * 商户编码
     */
    @ApiModelProperty("商户编码")   
    private String merCode;
    /**
     * 申请类型
     */
    @ApiModelProperty("申请类型")   
    private String applyType;
    /**
     * 金额
     */
    @ApiModelProperty("金额")   
    private BigDecimal balance;
    /**
     * 备注
     */
    @ApiModelProperty("备注")   
    private String remark;
    /**
     * 附件
     */
    @ApiModelProperty("附件")   
    private String enclosure;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;
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
    @TableField(fill = FieldFill.UPDATE)
	private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
    @TableField(fill = FieldFill.UPDATE)
	private Date updateTime;
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
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;
    /**
     * 申请人
     */
    @ApiModelProperty("申请人")   
    private String applyUser;
    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")   
    private Date applyTime;
    /**
     * 请求id
     */
    @ApiModelProperty("请求id")   
    private String requestId;

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
    * 商户编码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 申请类型
    */
    public static final String APPLY_TYPE = "apply_type";
    /**
    * 金额
    */
    public static final String BALANCE = "balance";
    /**
    * 备注
    */
    public static final String REMARK = "remark";
    /**
    * 附件
    */
    public static final String ENCLOSURE = "enclosure";
    /**
    * 删除标志
    */
    public static final String DELETED = "deleted";
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
    public static final String UPDATE_USER = "update_user";
    /**
    * 更新时间
    */
    public static final String UPDATE_TIME = "update_time";
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
    * 版本
    */
    public static final String VERSION = "version";
    /**
    * 申请人
    */
    public static final String APPLY_USER = "apply_user";
    /**
    * 申请时间
    */
    public static final String APPLY_TIME = "apply_time";
    /**
    * 请求id
    */
    public static final String REQUEST_ID = "request_id";

}