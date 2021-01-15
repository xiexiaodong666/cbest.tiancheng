package com.welfare.persist.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * (mer_deposit_apply_file)实体类
 *
 * @author kancy
 * @since 2021-01-14 14:47:06
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
    @ApiModelProperty("id")  @JsonSerialize(using = ToStringSerializer.class)
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