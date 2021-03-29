package com.welfare.persist.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (payment_channel)实体类
 *
 * @author kancy
 * @since 2021-03-11 17:28:32
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("payment_channel")
@ApiModel("")
public class PaymentChannel extends Model<PaymentChannel> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 支出渠道编码
     */
    @ApiModelProperty("支出渠道编码")  
    private String code;
    /**
     * 支付渠道名称
     */
    @ApiModelProperty("支付渠道名称")  
    private String name;
    /**
     * 商户编码
     */
    @ApiModelProperty("商户编码")  
    private String merchantCode;
    /**
     * 删除标志  1-删除、0-未删除
     */
    @ApiModelProperty("删除标志  1-删除、0-未删除")
    private Integer deleted;
    /**
     * 创建人
     */
    @ApiModelProperty("创建人")  
    private String createUser;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")  
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")  
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
    @ApiModelProperty("版本") @Version 
    private Integer version;
    /**
     * 展示顺序
     */
    @ApiModelProperty("展示顺序")
    private Integer showOrder;
//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 支出渠道编码
    */
    public static final String CODE = "code";
    /**
    * 支付渠道名称
    */
    public static final String NAME = "name";
    /**
    * 商户编码
    */
    public static final String MERCHANT_CODE = "merchant_code";
    /**
    * 删除标志  1-删除、0-未删除
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
    * 版本
    */
    public static final String VERSION = "version";
    /**
     * 展示顺序
     */
    public static final String SHOW_ORDER = "show_order";
}