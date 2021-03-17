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
 * (third_party_payment_request)实体类
 *
 * @author Yuxiang Li
 * @since 2021-03-17 09:20:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("third_party_payment_request")
@ApiModel("")
public class ThirdPartyPaymentRequest extends Model<ThirdPartyPaymentRequest> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * pk
     */
    @ApiModelProperty("pk")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 交易流水号
     */
    @ApiModelProperty("交易流水号")   
    private String transNo;
    /**
     * 交易金额
     */
    @ApiModelProperty("交易金额")   
    private BigDecimal transAmount;
    /**
     * 支付方式
     */
    @ApiModelProperty("支付方式")   
    private String paymentType;
    /**
     * 支付方式内容（条码或者磁卡信息）
     */
    @ApiModelProperty("支付方式内容（条码或者磁卡信息）")   
    private String paymentTypeInfo;
    /**
     * 支付请求类型
     */
    @ApiModelProperty("支付请求类型")   
    private String paymentRequestType;
    /**
     * 支付请求
     */
    @ApiModelProperty("支付请求")   
    private String paymentRequest;
    /**
     * 状态
     */
    @ApiModelProperty("状态")   
    private Integer transStatus;
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

//以下为列明常量

    /**
    * pk
    */
    public static final String ID = "id";
    /**
    * 交易流水号
    */
    public static final String TRANS_NO = "trans_no";
    /**
    * 交易金额
    */
    public static final String TRANS_AMOUNT = "trans_amount";
    /**
    * 支付方式
    */
    public static final String PAYMENT_TYPE = "payment_type";
    /**
    * 支付方式内容（条码或者磁卡信息）
    */
    public static final String PAYMENT_TYPE_INFO = "payment_type_info";
    /**
    * 支付请求类型
    */
    public static final String PAYMENT_REQUEST_TYPE = "payment_request_type";
    /**
    * 支付请求
    */
    public static final String PAYMENT_REQUEST = "payment_request";
    /**
    * 状态
    */
    public static final String TRANS_STATUS = "trans_status";
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

}