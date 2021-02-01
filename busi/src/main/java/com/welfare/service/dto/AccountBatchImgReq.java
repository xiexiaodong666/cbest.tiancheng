package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/1 1:38 PM
 */
@Data
@NoArgsConstructor
public class AccountBatchImgReq {

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @NotEmpty(message = "商户代码不能为空")
  private String merCode;

  /**
   * 手机号和s3存储key集合对象
   */
  @ApiModelProperty("手机号和s3存储key集合对象")
  @NotNull(message = "手机号和s3存储key集合不能唯恐")
  private List<AccountBatchImgInfoReq> accountBatchImgInfoReqList;
}
