package com.welfare.service.dto.nhc;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:02 上午
 */
@Data
public class NhcFamilyMemberDTO {

  @ApiModelProperty("家庭编码")
  private String familyCode;

  @ApiModelProperty("家庭余额")
  private String familyBalance;

  @ApiModelProperty("成员信息")
  private List<NhcUserInfoDTO> members;
}
