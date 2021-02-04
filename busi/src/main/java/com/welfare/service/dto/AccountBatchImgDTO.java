package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/2/1 2:01 PM
 */
@Data
@NoArgsConstructor
public class AccountBatchImgDTO {

  /**
   * 成功的集合
   */
  @ApiModelProperty("成功的集合")
  private List<String> successList;

  /**
   * 失败的集合
   */
  @ApiModelProperty("失败的集合")
  private List<UploadImgErrorMsgDTO> failList;
}
