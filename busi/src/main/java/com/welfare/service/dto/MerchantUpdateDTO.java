package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
@NoArgsConstructor
public class MerchantUpdateDTO {

  @ApiModelProperty("id")
  @NotNull
  private Long id;
  /**
   * 员工自主充值
   */

  @ApiModelProperty("员工自主充值")
  private String selfRecharge;
  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  @NotBlank
  private String merName;
  /**
   * 商户类型
   */
  @ApiModelProperty("商户类型")
  @NotBlank
  private String merType;
  /**
   * 身份属性
   */
  @ApiModelProperty("身份属性  supplier:供应商；customer:客户,多个用，号分隔")
 @NotBlank
  private String merIdentity;
  /**
   * 合作方式
   */
  @ApiModelProperty("合作方式")
  private String merCooperationMode;


  /**
   * 更新人
   */
  @ApiModelProperty("更新人")
  @NotBlank
  private String updateUser;


  @ApiModelProperty("备注")
  @Length(max = 50)
  private String remark;
  List<MerchantAddressDTO> addressList;
}
