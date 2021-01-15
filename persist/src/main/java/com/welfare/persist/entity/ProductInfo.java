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
 * (product_info)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("product_info")
@ApiModel("")
public class ProductInfo extends Model<ProductInfo> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品编码
     */
    @ApiModelProperty("商品编码")   
    @TableId
	private String productCode;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")   
    private String productName;
    /**
     * updateTime
     */
    @ApiModelProperty("updateTime")   
    @TableField(fill = FieldFill.UPDATE)
	private Date updateTime;

//以下为列明常量

    /**
    * 商品编码
    */
    public static final String PRODUCT_CODE = "product_code";
    /**
    * 商品名称
    */
    public static final String PRODUCT_NAME = "product_name";
    /**
    * 
    */
    public static final String UPDATE_TIME = "update_time";

}