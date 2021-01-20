package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class DepartmentAddDTO {

    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    @NotBlank
    private String merCode;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @NotBlank
    private String departmentName;
    /**
     * 部门编码
     */
    @ApiModelProperty("部门编码")
    private String departmentCode;
    /**
     * 部门父级
     */
    @ApiModelProperty("部门父级")
    @NotBlank
    private String departmentParent;


    @ApiModelProperty("部门类型")
    @NotBlank
    private String departmentType;

    /**
     * 外部编码
     */
    @ApiModelProperty("外部编码")
    private String externalCode;
}
