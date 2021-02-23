package com.welfare.service.dto.proprietary;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 3:34 下午
 */
@Data
public class ProprietaryConsumePageReq  {

  @ApiModelProperty(value = "订单编号")
  private String orderNo;

  @ApiModelProperty(value = "消费流水号")
  private String transNo;

  @ApiModelProperty(value = "手机号")
  private String phone;

  @ApiModelProperty(value = "起始时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date startTime;

  @ApiModelProperty(value = "结束时间")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date endTime;

  @ApiModelProperty(value = "门店编码")
  private String storeCode;

  @ApiModelProperty(value = "组织机构代码")
  private String departmentCode;
}
