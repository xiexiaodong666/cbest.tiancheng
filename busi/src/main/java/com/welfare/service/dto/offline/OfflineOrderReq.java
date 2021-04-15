package com.welfare.service.dto.offline;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/19 2:38 下午
 */
@Data
public class OfflineOrderReq extends PageReq {

  @ApiModelProperty("状态，0.挂起 1.已扣款 2.未知")
  private Integer status;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty("交易起始时间，格式“yyyy-MM-dd HH:mm:ss”")
  private Date beginTradeTime;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  @ApiModelProperty("交易结束时间，格式“yyyy-MM-dd HH:mm:ss”")
  private Date endTradeTime;

  @ApiModelProperty("手机号")
  private String phone;

  @ApiModelProperty("部门Code列表")
  private List<String> departmentCodes;
}
