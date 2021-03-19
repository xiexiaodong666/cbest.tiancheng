package com.welfare.service.dto.offline;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 3:04 下午
 */
@Data
public class OfflineOrderWarningSettingReq {

  @ApiModelProperty("主键id")
  private Long id;

  @ApiModelProperty("商户编码")
  private String merchantCode;

  @ApiModelProperty("姓名")
  private String name;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("发送时间列表，格式”HH:mm")
  private List<String> sendTimes;

  @ApiModelProperty("发送模板")
  private String template;

  @ApiModelProperty("状态，1.有效 0.无效")
  private Integer status;
}
