package com.welfare.persist.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * (mer_deposit_apply_file)实体类
 *
 * @author Yuxiang Li
 * @since 2021-01-15 15:14:23
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@TableName("mer_deposit_apply_file")
@ApiModel("")
public class MerDepositApplyFile extends Model<MerDepositApplyFile> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")   @JsonSerialize(using = ToStringSerializer.class)
    @TableId
	private Long id;
    /**
     * 申请编码
     */
    @ApiModelProperty("申请编码")   
    private String merDepositApplyCode;
    /**
     * fileUrl
     */
    @ApiModelProperty("fileUrl")   
    private String fileUrl;

//以下为列明常量

    /**
    * 
    */
    public static final String ID = "id";
    /**
    * 申请编码
    */
    public static final String MER_DEPOSIT_APPLY_CODE = "mer_deposit_apply_code";
    /**
    * 
    */
    public static final String FILE_URL = "file_url";

}