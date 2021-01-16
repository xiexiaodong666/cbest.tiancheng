package com.welfare.common.util;

import com.welfare.common.enums.ConsumeTypeEnum;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/11 11:31 PM
 */
public class ConsumeTypesUtils {

  public static List<String> transfer(Map<String, Boolean> map) {

    if (map == null) {
      return new ArrayList<>(0);
    }

    List<String> list = new ArrayList<>();
    if (map.get(ConsumeTypeEnum.O2O.getCode()) != null && map.get(ConsumeTypeEnum.O2O.getCode())) {
      list.add(ConsumeTypeEnum.O2O.getCode());
    }
    if (map.get(ConsumeTypeEnum.ONLINE_MALL.getCode()) != null && map.get(
        ConsumeTypeEnum.ONLINE_MALL.getCode())) {
      list.add(ConsumeTypeEnum.ONLINE_MALL.getCode());
    }
    if (map.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode()) != null && map.get(
        ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
      list.add(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
    }

    return list;
  }

  public static String transferStr(Map<String, Boolean> map) {
    return StringUtils.join(transfer(map), ",");
  }

  public static void removeFalseKey(Map<String, Boolean> map) {
    if (!map.get(ConsumeTypeEnum.O2O.getCode())) {
      map.remove(ConsumeTypeEnum.O2O.getCode());
    }
    if (!map.get(ConsumeTypeEnum.ONLINE_MALL.getCode())) {
      map.remove(ConsumeTypeEnum.ONLINE_MALL.getCode());
    }
    if (!map.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
      map.remove(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
    }
  }


  public static Map<String, Boolean> transfer(String str) {
    List<String> list = Arrays.asList(str.split(","));
    Map<String, Boolean> map = ConsumeTypeEnum.defaultMap;
    Set<String> keys=map.keySet();
    for (String s : list) {
      if(keys.contains(s)){
        map.put(s,Boolean.TRUE);
      }
    }
    return map;
  }

  public static Map<String, Boolean> transferWithExcel(List<String> list) {
    Map<String, Boolean> map = ConsumeTypeEnum.defaultMap;
    Set<String> keys=map.keySet();
    for (String s : list) {
      if(keys.contains(ConsumeTypeEnum.getTypeByExcelType(s))){
        map.put(s,Boolean.TRUE);
      }
    }
    return map;
  }
}
