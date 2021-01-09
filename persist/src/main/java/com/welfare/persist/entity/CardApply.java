package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 制卡信息(card_apply)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 14:23:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("card_apply")
@ApiModel("制卡信息")
public class CardApply extends Model<CardApply> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 制卡申请号
     */
    @ApiModelProperty("制卡申请号")   
    private String applyCode;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")   
    private String merCode;
    /**
     * 卡片名称
     */
    @ApiModelProperty("卡片名称")   
    private String cardName;
    /**
     * 卡片类型
     */
    @ApiModelProperty("卡片类型")   
    private String cardType;
    /**
     * 卡片介质
     */
    @ApiModelProperty("卡片介质")   
    private String cardMedium;
    /**
     * 卡片数量
     */
    @ApiModelProperty("卡片数量")   
    private Integer cardNum;
    /**
     * 识别码方法
     */
    @ApiModelProperty("识别码方法")   
    private String identificationCode;
    /**
     * 识别码长度
     */
    @ApiModelProperty("识别码长度")   
    private Integer identificationLength;
    /**
     * 备注
     */
    @ApiModelProperty("备注")   
    private String remark;
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
     * 删除标志
     */
    @ApiModelProperty("删除标志") @TableLogic @TableField  
    private Boolean deleted;
    /**
     * 状态: 锁定、激活
     */
    @ApiModelProperty("状态: 锁定、激活")   
    private Integer status;

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 制卡申请号
    */
    public static final String APPLY_CODE = "apply_code";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 卡片名称
    */
    public static final String CARD_NAME = "card_name";
    /**
    * 卡片类型
    */
    public static final String CARD_TYPE = "card_type";
    /**
    * 卡片介质
    */
    public static final String CARD_MEDIUM = "card_medium";
    /**
    * 卡片数量
    */
    public static final String CARD_NUM = "card_num";
    /**
    * 识别码方法
    */
    public static final String IDENTIFICATION_CODE = "identification_code";
    /**
    * 识别码长度
    */
    public static final String IDENTIFICATION_LENGTH = "identification_length";
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
    * 删除标志
    */
    public static final String DELETED = "deleted";
    /**
    * 状态: 锁定、激活
    */
    public static final String STATUS = "status";

}