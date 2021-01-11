package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * hao.yin
 */
@Data
@NoArgsConstructor
public class DictDTO  {

    /**
     * id
     */
    @ApiModelProperty("id")
	private Long id;
    /**
     * 码表类型
     */
    @ApiModelProperty("码表类型")   
    private String dictType;
    /**
     * 编码
     */
    @ApiModelProperty("编码")   
    private String dictCode;
    /**
     * 名称
     */
    @ApiModelProperty("名称")   
    private String dictName;
    /**
     * 状态
     */
    @ApiModelProperty("状态")   
    private Integer status;



}