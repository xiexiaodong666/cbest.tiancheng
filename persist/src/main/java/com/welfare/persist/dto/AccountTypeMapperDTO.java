package com.welfare.persist.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.util.Date;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 9:09
 */
@Data
public class AccountTypeMapperDTO {
  @JsonSerialize(using = ToStringSerializer.class)
  private String id;
  private String merCode;
  private String typeCode;
  private String typeName;
  private String remark;
  private Date createTime;
}
