package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/1 1:43 PM
 */
@Data
@NoArgsConstructor
public class AccountBatchImgInfoReq {

  /**
   * 手机号码
   */
  @ApiModelProperty("手机号码")
  private String phone;
  /**
   * 文件url
   */
  @ApiModelProperty("url")
  private String url;

}
