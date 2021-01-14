package com.welfare.service.remote.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author hao.yin
 * @version 0.0.1
 * @date 2021/1/12 17:19
 */
@Data
public class StoreShoppingReq implements Serializable {
  private String actionType;
  private String requestId;
  private Date timestamp;
  private List<ListBean> list;
  @Data
  public static class ListBean  {
    private boolean enabled;
    private String merchantCode;
    private String storeName;
    private int status;
    private String cashierNo;
    private String storeCode;
    private List<AddressBean> address;
    private List<String> consumeTypes;
    @Data
    public static class AddressBean  {
      private String address;
      private String addressType;
      private String name;
    }
  }
}
