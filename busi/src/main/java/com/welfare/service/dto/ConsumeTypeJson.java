package com.welfare.service.dto;

import java.io.Serializable;
import lombok.Data;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/14 10:17
 */
@Data
public class ConsumeTypeJson implements Serializable {
  private Boolean o2o;
  private Boolean onlineMall;
  private Boolean shopShopping;
  public static final  String O2O ="O2O";
  public static final  String ONLINE_MALL ="ONLINE_MALL";
  public static final  String SHOP_CONSUMPTION ="SHOP_CONSUMPTION";
  public boolean getType(String type){
    if( type.equals(O2O ) ){
      return o2o.booleanValue();
    }else if (type.equals(ONLINE_MALL )){
      return onlineMall.booleanValue();
    }else if(type.equals(SHOP_CONSUMPTION)){
      return shopShopping.booleanValue();
    }
    return false;
  }
}
