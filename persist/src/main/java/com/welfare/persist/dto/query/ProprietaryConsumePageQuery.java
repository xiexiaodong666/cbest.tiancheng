package com.welfare.persist.dto.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/2/23 5:23 下午
 */
@Data
public class ProprietaryConsumePageQuery {

  @ApiModelProperty(value = "商户编码")
  private String merCode;

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
  private List<String> storeCodes;

  @ApiModelProperty(value = "组织机构路径")
  private List<String> departmentPaths;

  @ApiModelProperty(value = "门店类型 自营：self，第三方：third")
  private String storeType;

  @ApiModelProperty(value = "员工支出方式")
  private String merAccountType;
}
