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
 * 供应商门店(supplier_store)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("supplier_store")
@ApiModel("供应商门店")
public class SupplierStore extends Model<SupplierStore> implements Serializable {
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
     * 门店代码
     */
    @ApiModelProperty("门店代码")  
    private String storeCode;
    /**
     * 门店名称
     */
    @ApiModelProperty("门店名称")  
    private String storeName;
    /**
     * 门店层级
     */
    @ApiModelProperty("门店层级")  
    private Integer storeLevel;
    /**
     * 父级门店
     */
    @ApiModelProperty("父级门店")  
    private String storeParent;
    /**
     * 门店路径
     */
    @ApiModelProperty("门店路径")  
    private String storePath;
    /**
     * 备注
     */
    @ApiModelProperty("备注")  
    private String remark;
    /**
     * 消费方式
     */
    @ApiModelProperty("消费方式")  
    private String consumType;
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
     * 创建日期
     */
    @ApiModelProperty("创建日期")  
    private Date createTime;
    /**
     * 更新人
     */
    @ApiModelProperty("更新人")  
    private String updateUser;
    /**
     * 更新日期
     */
    @ApiModelProperty("更新日期")  
    @TableField(update = "now()")
	private Date updateTime;
    /**
     * 外部编码
     */
    @ApiModelProperty("外部编码")  
    private String externalCode;
    /**
     * 版本
     */
    @ApiModelProperty("版本") @Version 
    private Integer version;

}