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
 * (order_trans_relation)实体类
 *
 * @author kancy
 * @since 2021-01-15 00:07:28
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("order_trans_relation")
@ApiModel("")
public class OrderTransRelation extends Model<OrderTransRelation> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")  
    private String orderId;
    /**
     * 交易号
     */
    @ApiModelProperty("交易号")  
    private String transNo;
    /**
     * 类型(充值订单还是消费订单)
     */
    @ApiModelProperty("类型(充值订单还是消费订单)")  
    private String type;
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
     * 删除标志  1-删除、0-未删除
     */
    @ApiModelProperty("删除标志  1-删除、0-未删除") @TableLogic
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Boolean deleted;
    /**
     * 版本
     */
    @ApiModelProperty("版本")  @Version
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Integer version;
//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 订单号
    */
    public static final String ORDER_ID = "order_id";
    /**
    * 交易号
    */
    public static final String TRANS_NO = "trans_no";
    /**
    * 类型(充值订单还是消费订单)
    */
    public static final String TYPE = "type";
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
    * 删除标记
    */
    public static final String DELETED = "deleted";

}