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
 * 员工消费场景配置(account_consume_scene)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-06 16:35:13
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("account_consume_scene")
@ApiModel("员工消费场景配置")
public class AccountConsumeScene extends Model<AccountConsumeScene> implements Serializable {
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
     * 员工类型编码
     */
    @ApiModelProperty("员工类型编码")  
    private String accountTypeId;
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
    @ApiModelProperty("删除标志")  
    private Integer flag;
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
    * 员工类型编码
    */
    public static final String ACCOUNT_TYPE_ID = "account_type_id";
    /**
    * 门店编码
    */
    public static final String STORE_CODE = "store_code";
    /**
    * 消费方式
    */
    public static final String CONSUM_TYPE = "consum_type";
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
    public static final String FLAG = "flag";
    /**
    * 版本
    */
    public static final String VERSION = "version";

}