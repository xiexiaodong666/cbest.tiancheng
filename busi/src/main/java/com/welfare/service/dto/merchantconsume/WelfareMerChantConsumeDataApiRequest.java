package com.welfare.service.dto.merchantconsume;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/4/14 10:13 AM
 */
@Data
public class WelfareMerChantConsumeDataApiRequest {

  /**
   * 业务类型
   */
  @ApiModelProperty("业务类型")
  private List<String> businessType;
  /**
   * 消费方式
   */
  @ApiModelProperty("消费方式")
  private List<String> consumeType;
  /**
   * 消费时间筛选结束
   */
  @ApiModelProperty("消费时间筛选结束")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date endDate;
  /**
   * 消费时间筛选起始
   */
  @ApiModelProperty("消费时间筛选起始")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private Date startDate;
  /**
   * 商户代码
   */
  @ApiModelProperty("商户代码")
  private String merCode;


  /**
   * 需要展开的表格数据类型
   */
  @ApiModelProperty("需要展开的表格数据类型")
  private List<String> rowsBusinessType;

  /**
   * 需要展开的汇总数据类型
   */
  @ApiModelProperty("需要展开的汇总数据类型")
  private List<String> extBusinessType;

}
