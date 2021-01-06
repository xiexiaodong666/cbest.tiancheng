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
 * @since 2021-01-06 16:35:13
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
    private Integer flag;
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

//以下为列明常量

    /**
    * id
    */
    public static final String ID = "id";
    /**
    * 商户代码
    */
    public static final String MER_CODE = "mer_code";
    /**
    * 门店代码
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 门店名称
    */
    public static final String STORE_NAME = "store_name";
    /**
    * 门店层级
    */
    public static final String STORE_LEVEL = "store_level";
    /**
    * 父级门店
    */
    public static final String STORE_PARENT = "store_parent";
    /**
    * 门店路径
    */
    public static final String STORE_PATH = "store_path";
    /**
    * 备注
    */
    public static final String REMARK = "remark";
    /**
    * 消费方式
    */
    public static final String CONSUM_TYPE = "consum_type";
    /**
    * 删除标志
    */
    public static final String FLAG = "flag";
    /**
    * 创建人
    */
    public static final String CREATE_USER = "create_user";
    /**
    * 创建日期
    */
    public static final String CREATE_TIME = "create_time";
    /**
    * 更新人
    */
    public static final String UPDATE_USER = "update_user";
    /**
    * 更新日期
    */
    public static final String UPDATE_TIME = "update_time";
    /**
    * 外部编码
    */
    public static final String EXTERNAL_CODE = "external_code";
    /**
    * 版本
    */
    public static final String VERSION = "version";

}