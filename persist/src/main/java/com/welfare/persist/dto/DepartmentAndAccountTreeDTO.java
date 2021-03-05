package com.welfare.persist.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/5 3:51 下午
 */
@Data
public class DepartmentAndAccountTreeDTO {

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
  private Date updateTime;
  /**
   * 外部编码
   */
  @ApiModelProperty("外部编码")
  private String externalCode;
}
