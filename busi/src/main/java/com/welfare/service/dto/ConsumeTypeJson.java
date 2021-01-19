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
  private Boolean O2O;
  private Boolean ONLINE_MALL;
  private Boolean SHOP_CONSUMPTION;
  public static final  String F_O2O ="O2O";
  public static final  String F_ONLINE_MALL ="ONLINE_MALL";
  public static final  String F_SHOP_CONSUMPTION ="SHOP_CONSUMPTION";
  public boolean getType(String type){
    if( type.equals(F_O2O ) ){
      return O2O.booleanValue();
    }else if (type.equals(F_ONLINE_MALL )){
      return ONLINE_MALL.booleanValue();
    }else if(type.equals(F_SHOP_CONSUMPTION)){
      return SHOP_CONSUMPTION.booleanValue();
    }
    return false;
  }
}
