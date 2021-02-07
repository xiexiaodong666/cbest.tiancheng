package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/4 4:48 PM
 */
@Data
@NoArgsConstructor
public class UploadImgErrorMsgDTO {

  /**
   * 手机号码
   */
  @ApiModelProperty("手机号码")
  private String phone;

  /**
   * 错误信息
   */
  @ApiModelProperty("错误信息")
  private String error;
}
