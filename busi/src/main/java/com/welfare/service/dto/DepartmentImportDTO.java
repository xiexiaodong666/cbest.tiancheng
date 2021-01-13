package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 商户部门导入类
 *
 * @author hao.yin
 * @since 2021-01-12 11:52:40
 * @description 由 Mybatisplus Code Generator 创建
 */
@Data
@NoArgsConstructor
@ApiModel("商户部门")
public class DepartmentImportDTO {

    /**
     * 部门名称
     */
    @ApiModelProperty("部门名称")
    @ExcelProperty(value = "机构名称", index = 2)
    private String departmentName;
    @ApiModelProperty("商户代码")
    @ExcelProperty(value = "商户代码", index = 0)
    private String merCode;
    /**
     * 部门父级
     */
    @ApiModelProperty("部门父级")
    @ExcelProperty(value = "上级机构代码", index = 1)
    private String departmentParent;
    /**
     * 部门类型
     */
    @ApiModelProperty("部门类型")
    @ExcelProperty(value = "机构类型", index = 3)
    private String departmentType;


}