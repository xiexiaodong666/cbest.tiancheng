package com.welfare.persist.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/15 4:17 PM
 */
@Data
public class CardApplyDTO {

  /**
   * id
   */
  @ApiModelProperty("id")
  @ExcelProperty(value ="id")
  private Long id;
  /**
   * 制卡申请号
   */
  @ApiModelProperty("制卡申请号")
  @ExcelProperty(value ="制卡申请号")
  private String applyCode;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  @ExcelProperty(value ="商户代码")
  private String merCode;
  /**
   * 商户名称
   */
  @ApiModelProperty("商户名称")
  @ExcelProperty(value ="商户名称")
  private String merName;

  /**
   * 卡片名称
   */
  @ApiModelProperty("卡片名称")
  @ExcelProperty(value ="卡片名称")
  private String cardName;
  /**
   * 卡片类型
   */
  @ApiModelProperty("卡片类型")
  @ExcelProperty(value ="卡片类型")
  private String cardType;
  /**
   * 卡片介质
   */
  @ApiModelProperty("卡片介质")
  @ExcelProperty(value ="卡片介质")
  private String cardMedium;
  /**
   * 卡片数量
   */
  @ApiModelProperty("卡片数量")
  @ExcelProperty(value ="卡片数量")
  private Integer cardNum;

  /**
   * 创建时间
   */
  @ApiModelProperty("创建时间")
  @ExcelProperty(value ="创建时间")
  private Date createTime;

  /**
   * 状态: 锁定、激活
   */
  @ApiModelProperty("状态: 锁定、激活")
  @ExcelProperty(value ="状态 1激活 0锁定")
  private Integer status;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  @ExcelIgnore
  private String remark;
}
