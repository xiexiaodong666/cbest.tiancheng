package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Created by hao.yin on 2021/1/7.
 */
@Data
@NoArgsConstructor
public class DepartmentUpdateDTO {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty("id")
    private Long id;
    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @NotBlank
    private String departmentName;


    @ApiModelProperty("部门类型")
    @NotBlank
    private String departmentType;

    /**
     * 更新人
     */
    @ApiModelProperty("更新人")
    private String updateUser;
    /**
     * 外部编码
     */
    @ApiModelProperty("外部编码")
    private String externalCode;
}
