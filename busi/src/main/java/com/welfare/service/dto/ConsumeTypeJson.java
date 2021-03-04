package com.welfare.service.dto;

import lombok.Data;

import java.io.Serializable;

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
  private Boolean WHOLESALE;
  public static final  String F_O2O ="O2O";
  public static final  String F_ONLINE_MALL ="ONLINE_MALL";
  public static final  String F_SHOP_CONSUMPTION ="SHOP_CONSUMPTION";
  public static final  String F_WHOLESALE = "WHOLESALE";
  public boolean getType(String type){
    if( null != O2O && type.equals(F_O2O ) ){
      return O2O.booleanValue();
    }else if (null != ONLINE_MALL && type.equals(F_ONLINE_MALL )){
      return ONLINE_MALL.booleanValue();
    }else if(null != SHOP_CONSUMPTION && type.equals(F_SHOP_CONSUMPTION)){
      return SHOP_CONSUMPTION.booleanValue();
    }else if(null != WHOLESALE && type.equals(F_WHOLESALE)){
      return WHOLESALE.booleanValue();
    }
    return false;
  }

  public String getValue(String type){
    if( null != O2O && type.equals(F_O2O ) ){
      return F_O2O;
    }else if (null != ONLINE_MALL && type.equals(F_ONLINE_MALL )){
      return F_ONLINE_MALL;
    }else if(null != SHOP_CONSUMPTION && type.equals(F_SHOP_CONSUMPTION)){
      return F_SHOP_CONSUMPTION;
    }else if(null != WHOLESALE && type.equals(F_WHOLESALE)){
      return F_WHOLESALE;
    }
    return "";
  }
}
