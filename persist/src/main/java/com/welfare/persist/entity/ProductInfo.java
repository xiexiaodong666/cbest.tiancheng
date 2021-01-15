package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Date;

/**
 * 字典(dict)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 15:13:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("product_info")
@ApiModel("商品表")
public class ProductInfo extends Model<ProductInfo> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品编码
     */
    @ApiModelProperty("商品编码")
    private String productCode;
    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String productName;
    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;


//以下为列明常量


    /**
    * 编码
    */
    public static final String PRODUCR_CODE = "product_code";
    /**
    * 名称
    */
    public static final String PRODUCT_NAME = "product_name";
    /**
    * 状态
    */
    public static final String UPDATE_IMTE = "update_time";

}