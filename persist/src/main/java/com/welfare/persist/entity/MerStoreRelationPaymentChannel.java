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
 * (mer_store_relation_payment_channel)实体类
 *
 * @author Yuxiang Li
 * @since 2021-03-25 15:54:37
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("mer_store_relation_payment_channel")
@ApiModel("")
public class MerStoreRelationPaymentChannel extends Model<MerStoreRelationPaymentChannel> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @ApiModelProperty("pk")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 商户编码
     */
    @ApiModelProperty("商户编码")   
    private String merCode;
    /**
     * 门店编码
     */
    @ApiModelProperty("门店编码")   
    private String storeCode;
    /**
     * 消费场景
     */
    @ApiModelProperty("消费场景")   
    private String consumType;
    /**
     * 支付渠道
     */
    @ApiModelProperty("支付渠道")   
    private String paymentChannel;
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
    * pk
    */
    public static final String ID = "id";
    /**
    * 商户编码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 门店编码
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 消费场景
    */
    public static final String CONSUM_TYPE = "consum_type";
    /**
    * 支付渠道
    */
    public static final String PAYMENT_CHANNEL = "payment_channel";
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