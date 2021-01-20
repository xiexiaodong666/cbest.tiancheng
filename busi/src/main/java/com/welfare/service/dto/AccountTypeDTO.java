package com.welfare.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 16:10
 */
@Data
@ApiModel("员工类型model")
public class AccountTypeDTO implements Serializable {
  @ApiModelProperty("ID")
  @JsonSerialize(using = ToStringSerializer.class)
  private String id;
  @ApiModelProperty("商户编码")
  private String merCode;
  @ApiModelProperty("员工类型编码")
  private String typeCode;
  @ApiModelProperty("员工类型名称")
  private String typeName;
  @ApiModelProperty("备注")
  private String remark;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") //入参
  @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss") //出参
  private Date createTime;
}
