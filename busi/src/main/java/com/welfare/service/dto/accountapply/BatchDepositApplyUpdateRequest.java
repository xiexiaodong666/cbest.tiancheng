package com.welfare.service.dto.accountapply;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  9:37 AM
 */
@Data
@NoArgsConstructor
public class BatchDepositApplyUpdateRequest {

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  @NotEmpty(message = "申请id不能为空")
  private String id;

  /**
   * 文件id
   */
  @ApiModelProperty("文件id")
  private String fileId;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 福利类型
   */
  @ApiModelProperty("福利类型")
  @NotEmpty(message = "余额类型为空")
  private String merAccountTypeCode;

  /**
   * 余额类型名称
   */
  @ApiModelProperty("余额类型名称")
  @NotEmpty(message = "余额类型名称为空")
  private String merAccountTypeName;
}