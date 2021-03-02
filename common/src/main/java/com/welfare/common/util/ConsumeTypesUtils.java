package com.welfare.common.util;

import com.welfare.common.enums.ConsumeTypeEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;

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
    map.forEach((consume, flag) -> {
      if (flag) {
        list.add(consume);
      }
    });
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
    if (!map.get(ConsumeTypeEnum.WHOLESALE.getCode())) {
      map.remove(ConsumeTypeEnum.WHOLESALE.getCode());
    }
  }

  public static Map<String, Boolean> transfer(String str) {
    List<String> list = Arrays.asList(str.split(","));
    Map<String, Boolean> map = defaultMap();
    Set<String> keys=map.keySet();
    for (String s : list) {
      if(keys.contains(s)){
        map.put(s,Boolean.TRUE);
      }
    }
    return map;
  }
  public static Map<String, Boolean> defaultMap(){
    Map<String, Boolean> map = new HashMap<>();
    for (ConsumeTypeEnum consumeTypeEnum : ConsumeTypeEnum.values()) {
      map.put(consumeTypeEnum.getCode(),Boolean.FALSE);
    }
    return map;
  }

  public static Map<String, Boolean> transferWithExcel(List<String> list) {
    Map<String, Boolean> map=defaultMap();
    Set<String> keys=map.keySet();
    for (String s : list) {
      String code=ConsumeTypeEnum.getTypeByExcelType(s);
      if(keys.contains(code)){
        map.put(code,Boolean.TRUE);
      }
    }
    return map;
  }
}
