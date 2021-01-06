package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
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
 * 商户消费场景配置(merchant_store_relation)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("merchant_store_relation")
@ApiModel("商户消费场景配置")
public class MerchantStoreRelation extends Model<MerchantStoreRelation> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")  
    private String merCode;
    /**
     * 门店编码
     */
    @ApiModelProperty("门店编码")  
    private String storeCode;
    /**
     * 消费方式
     */
    @ApiModelProperty("消费方式")  
    private String consumType;
    /**
     * 门店别名
     */
    @ApiModelProperty("门店别名")  
    private String storeAlias;
    /**
     * 是否返利
     */
    @ApiModelProperty("是否返利")  
    private Integer isRebate;
    /**
     * 返利类型
     */
    @ApiModelProperty("返利类型")  
    private String rebateType;
    /**
     * 返利比率
     */
    @ApiModelProperty("返利比率")  
    private Integer rebateRatio;
    /**
     * 备注
     */
    @ApiModelProperty("备注")  
    private String ramark;
    /**
     * 删除标志
     */
    @ApiModelProperty("删除标志")  
    private Boolean flag;
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

}