package com.welfare.service.remote.entity.pos;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收银终端价格模板
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/1/29 11:43 上午
 */
@Data
@ApiModel("收银终端价格模板")
public class PosTerminalPriceTemplateResp {

  @ApiModelProperty("主键")
  private Long id;

  @ApiModelProperty("终端标识")
  private String identityKey;

  @ApiModelProperty("终端类型, 1.海信手持pos机 2.德卡双屏收银机 3.海信自助收银机")
  private Integer type;

  @ApiModelProperty("终端名称")
  private String name;

  @ApiModelProperty("门店号")
  private String storeCode;

  @ApiModelProperty("门店名称")
  private String storeName;

  @ApiModelProperty("收银机号")
  private String posId;

  @ApiModelProperty("交易模式，0.自定价格 1.固定价格 2.动态价格")
  private Integer tradeMode;

  @ApiModelProperty("模板主键")
  private Long templateId;

  @ApiModelProperty("模板名称")
  private String templateName;
}
