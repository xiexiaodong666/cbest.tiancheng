package com.welfare.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
@NoArgsConstructor
public class MerchantSyncDTO {

  private Long id;

  /**
   * 员工自主充值
   */
  @ApiModelProperty("员工自主充值")
  private String selfRecharge;

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merName;
  /**
   * 商户类型
   */
  @ApiModelProperty("商户类型")
  private String merType;
  /**
   * 身份属性
   */
  @ApiModelProperty("身份属性  supplier:供应商；customer:客户,多个用，号分隔")
  private String merIdentity;
  /**
   * 合作方式
   */
  @ApiModelProperty("合作方式")
  private String merCooperationMode;


  List<MerchantAddressDTO> addressList;

  /**
   * 行业属性
   */
  private List<String> tags;
}
