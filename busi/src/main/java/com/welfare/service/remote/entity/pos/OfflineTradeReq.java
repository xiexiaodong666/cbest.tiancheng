package com.welfare.service.remote.entity.pos;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/22 1:44 下午
 */
@Data
public class OfflineTradeReq {

  @ApiModelProperty("状态，0.挂起 1.已扣款 2.不扣款")
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

  @ApiModelProperty("分页条件")
  @NotNull(message = "分页条件不能为空")
  private PagingCondition paging;

  private String merchantCode;
}
