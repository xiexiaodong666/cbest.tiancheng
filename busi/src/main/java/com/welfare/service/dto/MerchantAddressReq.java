package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author hao.yin
 * @version 1.0.0
 * @date 2021/1/8 2:26 PM
 */
@Data
public class MerchantAddressReq {
  /**
   * 状态
   */
  @ApiModelProperty("状态")
  @Query(type = Query.Type.EQUAL)
  private Integer status;
  /**
   * 关联类型
   */
  @ApiModelProperty("关联类型")
  @Query(type = Query.Type.EQUAL)
  private String relatedType;
  /**
   * 关联id
   */
  @ApiModelProperty("关联id")
  @Query(type = Query.Type.EQUAL)
  private Long relatedId;

}
