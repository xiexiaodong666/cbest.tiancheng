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
 * 账户交易明细拉取记录表(pull_account_detail_record)实体类
 *
 * @author kancy
 * @since 2021-01-13 20:51:36
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("pull_account_detail_record")
@ApiModel("账户交易明细拉取记录表")
public class PullAccountDetailRecord extends Model<PullAccountDetailRecord> implements Serializable {
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
    private String merchantCode;
    /**
     * 处理日期
     */
    @ApiModelProperty("处理日期")  
    private Date delDate;
    /**
     * 处理状态 success-成功 fail-失败
     */
    @ApiModelProperty("处理状态 success-成功 fail-失败")  
    private String delStatus;
    /**
     * 重试次数
     */
    @ApiModelProperty("重试次数")  
    private Integer tryCount;
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

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 商户代码
    */
    public static final String MERCHANT_CODE = "merchant_code";
    /**
    * 处理日期
    */
    public static final String DEL_DATE = "del_date";
    /**
    * 处理状态 success-成功 fail-失败
    */
    public static final String DEL_STATUS = "del_status";
    /**
    * 重试次数
    */
    public static final String TRY_COUNT = "try_count";
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

}