package com.welfare.persist.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
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
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商户福利类型(merchant_account_type)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_account_type")
@ApiModel("商户福利类型")
public class MerchantAccountType extends Model<MerchantAccountType> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 自增id
     */
    @ApiModelProperty("自增id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.AUTO)
	private Long id;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 商户账户类型编码
     */
    @ApiModelProperty("商户账户类型编码")   
    private String merAccountTypeCode;
    /**
     * 商户账户类型名称
     */
    @ApiModelProperty("商户账户类型名称")   
    private String merAccountTypeName;
    /**
     * 扣款序号
     */
    @ApiModelProperty("扣款序号")   
    private Integer deductionOrder;
    /**
     * 删除标识
     */
    @ApiModelProperty("删除标识")   
    private Boolean flag;
    /**
     * 备注
     */
    @ApiModelProperty("备注")   
    private String remark;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")   
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private String updateUser;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")   
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version 
    @TableField(fill = FieldFill.INSERT_UPDATE)
	private Integer version;

//以下为列明常量

    /**
    * 自增id
    */
    public static final String ID = "id";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 商户账户类型编码
    */
    public static final String MER_ACCOUNT_TYPE_CODE = "mer_account_type_code";
    /**
    * 商户账户类型名称
    */
    public static final String MER_ACCOUNT_TYPE_NAME = "mer_account_type_name";
    /**
    * 扣款序号
    */
    public static final String DEDUCTION_ORDER = "deduction_order";
    /**
    * 删除标识
    */
    public static final String FLAG = "flag";
    /**
    * 备注
    */
    public static final String REMARK = "remark";
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
    * 版本
    */
    public static final String VERSION = "version";

}