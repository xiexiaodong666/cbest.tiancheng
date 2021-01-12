package com.welfare.service.dto.accountapply;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  9:33 AM
 */
@Data
@NoArgsConstructor
@ApiModel("批量导入账号申请请求")
public class BatchDepositApplyRequest {

  @ApiModelProperty("请求id（用于幂等处理，UUID即可）")
  @NotEmpty(message = "requestId为空")
  private String requestId;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 福利类型
   */
  @ApiModelProperty("福利类型")
  @NotEmpty(message = "福利类型为空")
  private String merAccountTypeCode;

  /**
   * 审批类型
   */
  @ApiModelProperty("审批类型（单个：SINGLE，批量：BATCH）")
  private String approvalType;
}