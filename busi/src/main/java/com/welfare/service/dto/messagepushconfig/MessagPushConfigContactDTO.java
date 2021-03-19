package com.welfare.service.dto.messagepushconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 10:40 上午
 */
@Data
public class MessagPushConfigContactDTO {

  @ApiModelProperty("主键")
  private String id;

  @ApiModelProperty("用户电话")
  private String contact;

  @ApiModelProperty("用户姓名")
  @NotEmpty(message = "用户姓名不能为空")
  private String contactPerson;

  @ApiModelProperty("推送时间")
  @NotEmpty(message = "至少保留一个推送时间")
  private String pushTime;

  @ApiModelProperty("配置所属商户")
  private String merCode;
}
