package com.welfare.persist.dto.query;

import java.util.Date;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 17:28
 */
@Data
public class AccountConsumePageQuery {
  /**
   * 商户代码
   */
  private String merCode;
  /**
   * 员工类型编码
   */
  private Long accountTypeId;
  /**
   * 使用状态
   */
  private Integer status;
  /**
   * 创建时间_start
   */
  private Date createTimeStart;
  /**
   * 创建时间_end
   */
  private Date createTimeEnd;
}
