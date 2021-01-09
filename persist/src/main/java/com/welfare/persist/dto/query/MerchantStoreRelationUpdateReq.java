package com.welfare.persist.dto.query;

import com.welfare.persist.dto.AdminMerchantStore;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 7:12 PM
 */
@Data
public class MerchantStoreRelationUpdateReq {

  /**
   * id
   */
  @ApiModelProperty("id")
  private Long id;


  /**
   * 门店，消费方式集合
   */
  @ApiModelProperty("门店，消费方式集合")
  private List<AdminMerchantStore> adminMerchantStoreList;

  /**
   * 备注
   */
  @ApiModelProperty("备注")
  private String ramark;
}
