package com.welfare.service.remote.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hao.yin
 * @version 0.0.1
 * @date 2021/1/12 17:19
 */
@Data
public class MerchantShoppingReq implements Serializable {


  private String actionType;
  private String requestId;
  private String timestamp;
  private List<ListBean> list;

  @Data
  public static class ListBean  {
    private boolean canSelfCharge;
    private String merchantCode;
    private String name;
    private List<AddressBean> address;
    private List<String> idTypes;
    @Data
    public static class AddressBean  {
      private String address;
      private String addressType;
      private String name;

    }
  }
}
