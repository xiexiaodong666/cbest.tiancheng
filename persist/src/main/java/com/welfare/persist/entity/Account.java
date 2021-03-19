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
 * 账户信息(account)实体类
 *
 * @author Yuxiang Li
 * @since 2021-02-01 11:20:41
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account")
@ApiModel("账户信息")
public class Account extends Model<Account> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 员工名称
     */
    @ApiModelProperty("员工名称")   
    private String accountName;
    /**
     * 员工账号
     */
    @ApiModelProperty("员工账号")   
    private Long accountCode;
    /**
     * 员工类型编码
     */
    @ApiModelProperty("员工类型编码")   
    private String accountTypeCode;
    /**
     * 所属商户
     */
    @ApiModelProperty("所属商户")   
    private String merCode;
    /**
     * 所属部门
     */
    @ApiModelProperty("所属部门")   
    private String storeCode;
    /**
     * 账号状态(1正常2禁用)
     */
    @ApiModelProperty("账号状态(1正常2禁用)")   
    private Integer accountStatus;

    @ApiModelProperty("离线启用标识 1启用，0-禁用")
    private Integer offlineLock;
    /**
     * 员工状态
     */
    @ApiModelProperty("员工状态")   
    private String staffStatus;
    /**
     * 是否绑卡(1绑定0未绑定)
     */
    @ApiModelProperty("是否绑卡(1绑定0未绑定)")   
    private Integer binding;
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
     * 账户余额
     */
    @ApiModelProperty("账户余额")   
    private BigDecimal accountBalance;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic   
    @TableField(fill = FieldFill.INSERT)
	private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT)
	private Integer version;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")   
    private String phone;
    /**
     * 最大授权额度
     */
    @ApiModelProperty("最大授权额度")   
    private BigDecimal maxQuota;
    /**
     * 剩余授权额度
     */
    @ApiModelProperty("剩余授权额度")   
    private BigDecimal surplusQuota;
    @ApiModelProperty("个人授信额度溢缴款")
    private BigDecimal surplusQuotaOverpay;
    /**
     * 备注
     */
    @ApiModelProperty("备注")   
    private String remark;
    /**
     * 员工账号变更记录ID
     */
    @ApiModelProperty("员工账号变更记录ID")   
    private Long changeEventId;
    /**
     * 是否授信
     */
    @ApiModelProperty("是否授信")   
    private Boolean credit;
    /**
     * 文件存储关联id
     */
    @ApiModelProperty("文件存储关联id")   
    private Long fileUniversalStorageId;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 员工名称
    */
    public static final String ACCOUNT_NAME = "account_name";
    /**
    * 员工账号
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 员工类型编码
    */
    public static final String ACCOUNT_TYPE_CODE = "account_type_code";
    /**
    * 所属商户
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 所属部门
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 账号状态(1正常2禁用)
    */
    public static final String ACCOUNT_STATUS = "account_status";
    /**
    * 员工状态
    */
    public static final String STAFF_STATUS = "staff_status";
    /**
    * 是否绑卡(1绑定0未绑定)
    */
    public static final String BINDING = "binding";
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
    * 账户余额
    */
    public static final String ACCOUNT_BALANCE = "account_balance";
    /**
    * 删除标志
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";
    /**
    * 手机号
    */
    public static final String PHONE = "phone";
    /**
    * 最大授权额度
    */
    public static final String MAX_QUOTA = "max_quota";
    /**
    * 剩余授权额度
    */
    public static final String SURPLUS_QUOTA = "surplus_quota";

    /**
     * 个人授权额度溢缴款
     */
    public static final String SURPLUS_QUOTA_OVERPAY = "surplus_quota_overpay";
    /**
    * 备注
    */
    public static final String REMARK = "remark";
    /**
    * 员工账号变更记录ID
    */
    public static final String CHANGE_EVENT_ID = "change_event_id";
    /**
    * 是否授信
    */
    public static final String CREDIT = "credit";
    /**
    * 文件存储关联id
    */
    public static final String FILE_UNIVERSAL_STORAGE_ID = "file_universal_storage_id";

    public static final String OFFLINE_LOCK = "offline_lock";

}