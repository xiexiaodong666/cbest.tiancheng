package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.mapper.MerchantCreditMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商户额度信(merchant_credit)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class MerchantCreditDao extends ServiceImpl<MerchantCreditMapper, MerchantCredit> {

  public Map<String, MerchantCredit> mapByMerCodes(List<String> merCodes) {
    Map<String, MerchantCredit> map = new HashMap<>();
    QueryWrapper<MerchantCredit> queryWrapper = new QueryWrapper<>();
    queryWrapper.in(MerchantCredit.MER_CODE, merCodes);
    List<MerchantCredit> list = list(queryWrapper);
    if (CollectionUtils.isNotEmpty(list)) {
      map = list.stream().collect(Collectors.toMap(MerchantCredit::getMerCode, a -> a,(k1, k2)->k1));
    }
    return map;
  }
}