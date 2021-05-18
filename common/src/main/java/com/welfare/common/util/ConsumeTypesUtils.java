package com.welfare.common.util;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.exception.BizException;
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
    if (map != null && map.size() > 0) {
      Set<Map.Entry<String, Boolean>> set = map.entrySet();
      set.removeIf(entry -> !entry.getValue());
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

  public static List<ConsumeTypeEnum> getRedundantConsumeType(String oldConsumeType, String newConsumeType) {
    Map<String, Boolean> oldConsumeTypeMap = new HashMap<>();
    Map<String, Boolean> newConsumeTypeMap = new HashMap<>();
    if (StringUtils.isNotBlank(oldConsumeType)) {
      oldConsumeTypeMap = JSON.parseObject(oldConsumeType, Map.class);
    }
    if (StringUtils.isNotBlank(newConsumeType)) {
      newConsumeTypeMap = JSON.parseObject(newConsumeType, Map.class);
    }
    if (newConsumeTypeMap == null || newConsumeTypeMap.size() == 0 || oldConsumeTypeMap == null || oldConsumeTypeMap.size() == 0) {
      return getTrueByMap(oldConsumeTypeMap);
    } else {
      List<ConsumeTypeEnum> consumeTypeList = new ArrayList<>();
      for (Map.Entry<String, Boolean> oldType: oldConsumeTypeMap.entrySet()) {
        if (oldType.getValue() && (!newConsumeTypeMap.containsKey(oldType.getKey()) || !newConsumeTypeMap.get(oldType.getKey()) )) {
          consumeTypeList.add(ConsumeTypeEnum.getByType(oldType.getKey()));
        }
      }
      return consumeTypeList;
    }
  }

  private static List<ConsumeTypeEnum> getTrueByMap(Map<String, Boolean> consumeTypeMap){
    List<ConsumeTypeEnum> consumeTypeEnums = new ArrayList<>();
    if (consumeTypeMap != null && consumeTypeMap.size() > 0) {
      consumeTypeMap.forEach((s, aBoolean) -> {
        if(aBoolean) {
          consumeTypeEnums.add(ConsumeTypeEnum.getByType(s));
        }
      });
    }
    return consumeTypeEnums;
  }

  public static List<ConsumeTypeEnum> getByConsumeTypeJson(String consumeTypeJson){
    List<ConsumeTypeEnum> consumeTypeEnums = new ArrayList<>();
    if (StringUtils.isNotBlank(consumeTypeJson)) {
      Map<String, Boolean> consumeTypeMap = JSON.parseObject(consumeTypeJson, Map.class);
      if (consumeTypeMap != null && consumeTypeMap.size() > 0) {
        consumeTypeMap.forEach((s, aBoolean) -> {
          if(aBoolean) {
            consumeTypeEnums.add(ConsumeTypeEnum.getByType(s));
          }
        });
      }
    }
    return consumeTypeEnums;
  }

  /**
   * 该门店是否包含批发业务
   */
  public static boolean isRelationedWholesale(String consumeType) {
    Map<String, Boolean> consumeTypeMap;
    try {
      ObjectMapper mapper = new ObjectMapper();
      consumeTypeMap = mapper.readValue(
          consumeType, Map.class);
    }  catch (JsonProcessingException e) {
      throw new BizException("消费方法格式错误");
    }
    if (consumeTypeMap == null) {
      throw new BizException("消费方法格式错误");
    }
    Boolean isRelationedWholesale = consumeTypeMap.get(ConsumeTypeEnum.WHOLESALE.getCode());
    if(isRelationedWholesale == null) {
      isRelationedWholesale = false;
    }
    return isRelationedWholesale;
  }
}
