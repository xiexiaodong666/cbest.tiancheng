package com.welfare.servicemerchant.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 17:21
 */
@Data
@ApiModel("员工类型消费场景分页入参")
public class AccountConsumePageReq implements Serializable {

  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;
  /**
   * 员工类型名称
   */
  @ApiModelProperty("员工类型名称")
  private String accountTypeName;
  /**
   * 使用状态
   */
  @ApiModelProperty("使用状态")
  private Integer status;
  /**
   * 创建时间_start
   */
  @ApiModelProperty("创建时间_start")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeStart;
  /**
   * 创建时间_end
   */
  @ApiModelProperty("创建时间_end")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTimeEnd;
}
