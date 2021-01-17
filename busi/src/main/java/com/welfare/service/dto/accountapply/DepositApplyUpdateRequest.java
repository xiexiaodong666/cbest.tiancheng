package com.welfare.service.dto.accountapply;

import com.welfare.service.dto.AccountDepositRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/7  7:06 PM
 */
@Data
@NoArgsConstructor
@ApiModel("修改账号存款申请请求")
public class DepositApplyUpdateRequest {

  /**
   * 申请id
   */
  @ApiModelProperty("申请id")
  @NotEmpty(message = "id为空")
  private String id;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 福利类型
   */
  @ApiModelProperty("福利类型")
  private String merAccountTypeCode;

  /**
   * 福利类型名称
   */
  @ApiModelProperty("福利类型名称")
  private String merAccountTypeName;

  @ApiModelProperty("申请员工额度信息")
  @Valid
  private AccountDepositRequest info;
}