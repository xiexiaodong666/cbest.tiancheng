package com.welfare.service.dto.messagepushconfig;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 11:42 上午
 */
@Data
public class MessagConfigContactAddReq {

  @ApiModelProperty(value = "用户电话", required = true)
  @NotEmpty(message = "用户电话不能为空")
  @Pattern(regexp = "^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$", message = "手机号不合法")
  private String contact;

  @ApiModelProperty(value = "用户姓名", required = true)
  @NotEmpty(message = "用户姓名不能为空")
  private String contactPerson;

  @ApiModelProperty(value = "格式：23:00",required = true)
  @NotEmpty(message = "最少保留一个推送时间")
  private List<String> pushTimes;
}
