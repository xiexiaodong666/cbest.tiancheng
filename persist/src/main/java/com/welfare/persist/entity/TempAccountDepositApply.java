package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.math.BigDecimal;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (temp_account_deposit_apply)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-09 14:52:38
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("temp_account_deposit_apply")
@ApiModel("")
public class TempAccountDepositApply extends Model<TempAccountDepositApply> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 上传文件id
     */
    @ApiModelProperty("上传文件id")   
    private String fileId;
    /**
     * 员工账号
     */
    @ApiModelProperty("员工账号")   
    private String accountCode;
    /**
     * 充值金额
     */
    @ApiModelProperty("充值金额")   
    private BigDecimal rechargeAmount;
    /**
     * 上传请求id
     */
    @ApiModelProperty("上传请求id")   
    private String requestId;

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 上传文件id
    */
    public static final String FILE_ID = "file_id";
    /**
    * 员工账号
    */
    public static final String ACCOUNT_CODE = "account_code";
    /**
    * 充值金额
    */
    public static final String RECHARGE_AMOUNT = "recharge_amount";
    /**
    * 上传请求id
    */
    public static final String REQUEST_ID = "request_id";

}