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
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 商户扩展表(merchant_extend)实体类
 *
 * @author Yuxiang Li
 * @since 2021-04-12 10:38:04
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_extend")
@ApiModel("商户扩展表")
public class MerchantExtend extends Model<MerchantExtend> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 商户编码
     */
    @ApiModelProperty("商户编码")   
    private String merCode;
    /**
     * 行业标签(多个逗号隔开)
     */
    @ApiModelProperty("行业标签")
    private String industryTag;

    /**
     * 结算方式
     */
    @ApiModelProperty("结算方式")
    private String supplierWholesaleSettleMethod;

    /**
     * 积分商城是否开启
     */
    @ApiModelProperty("积分商城是否开启")   
    private Boolean pointMall;
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

//以下为列明常量

    /**
    * 主键
    */
    public static final String ID = "id";
    /**
    * 商户编码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 行业标签
    */
    public static final String INDUSTRY_TAG = "industry_tag";
    /**
    * 积分商城是否开启
    */
    public static final String POINT_MALL = "point_mall";
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
    * 删除标志
    */
    public static final String DELETED = "deleted";
    /**
    * 版本
    */
    public static final String VERSION = "version";

}