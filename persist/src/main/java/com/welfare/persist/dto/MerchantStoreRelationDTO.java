package com.welfare.persist.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
@ExcelIgnoreUnannotated
public class MerchantStoreRelationDTO {

  /**
   * id
   */
  @ExcelProperty(value ="id")
  private Long id;

  /**
   * 商户代码
   */
  @ExcelProperty(value ="商户代码")
  private String merCode;

  /**
   * 商户名称
   */
  @ExcelProperty(value ="商户名称")
  private String merName;

  /**
   * 门店编码
   */
  @ExcelProperty(value ="门店编码")
  private String storeCode;

  /**
   * 门店名称
   */
  @ExcelProperty(value ="门店名称")
  private String storeAlias;

  /**
   * 创建时间
   */
  @ExcelProperty(value ="创建时间")
  private Date createTime;

  /**
   * 备注
   */
  @ExcelProperty(value ="备注")
  private String ramark;

  /**
   * 状态
   */
  @ExcelProperty(value ="状态 1 启用 0 禁用")
  private String status;

}
