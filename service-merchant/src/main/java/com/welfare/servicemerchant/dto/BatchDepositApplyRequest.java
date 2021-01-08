package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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

  @ApiModelProperty("请求id（用于幂等处理）")
  private String requestId;

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;

  /**
   * 申请备注
   */
  @ApiModelProperty("申请备注")
  private String applyRemark;

  /**
   * 申请人
   */
  @ApiModelProperty("申请人")
  private String applyUser;

  /**
   * 余额类型
   */
  @ApiModelProperty("余额类型（待定义）")
  private String balanceType;

  /**
   * 审批类型
   */
  @ApiModelProperty("审批类型（单个：SINGLE，批量：BATCH）")
  private String approvalType;


  @ApiModelProperty("Excel二级制(MultipartFile)")
  private MultipartFile file;
}