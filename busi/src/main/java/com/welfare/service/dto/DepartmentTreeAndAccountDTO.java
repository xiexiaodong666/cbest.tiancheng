package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 3:57 下午
 */
@Data
@ApiModel("组织机构及下员工数量")
public class DepartmentTreeAndAccountDTO extends Tree{

  /**
   * id
   */
  @ApiModelProperty("id")
  private Long id;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 部门名称
   */
  @ApiModelProperty("部门名称")
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
  private String departmentParent;
  /**
   * 部门层级
   */
  @ApiModelProperty("部门层级")
  private Integer departmentLevel;
  /**
   * 部门路径
   */
  @ApiModelProperty("部门路径")
  private String departmentPath;
  /**
   * 人员数量
   */
  @ApiModelProperty("人员数量")
  private Integer accountTotal;
}
