package com.welfare.common.util;

import com.welfare.common.enums.ConsumeTypeEnum;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    if (map.get(ConsumeTypeEnum.O2O.getCode())) {
      list.add(ConsumeTypeEnum.O2O.getCode());
    }
    if (map.get(ConsumeTypeEnum.ONLINE_MALL.getCode())) {
      list.add(ConsumeTypeEnum.ONLINE_MALL.getCode());
    }
    if (map.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
      list.add(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
    }

    return list;
  }
}
