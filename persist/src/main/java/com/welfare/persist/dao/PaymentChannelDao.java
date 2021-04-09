package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.PaymentChannel;
import com.welfare.persist.mapper.PaymentChannelMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * (payment_channel)数据DAO
 *
 * @author kancy
 * @since 2021-03-11 17:28:32
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class PaymentChannelDao extends ServiceImpl<PaymentChannelMapper, PaymentChannel> {

  public static final String DEFAULT_MERCHANT_NAME = "default";

  public List<PaymentChannel> listByMerCodeGroupByCode(String merCode) {
    QueryWrapper<PaymentChannel> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(PaymentChannel.MERCHANT_CODE, merCode);
    queryWrapper.eq(PaymentChannel.DELETED, "0");
    queryWrapper.orderByAsc(PaymentChannel.SHOW_ORDER);
    return list(queryWrapper);
  }

  public List<PaymentChannel> listByDefaultGroupByCode() {
    QueryWrapper<PaymentChannel> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(PaymentChannel.MERCHANT_CODE, DEFAULT_MERCHANT_NAME);
    queryWrapper.eq(PaymentChannel.DELETED, "0");
    queryWrapper.orderByAsc(PaymentChannel.SHOW_ORDER);
    return list(queryWrapper);
  }

  public Map<String, PaymentChannel> allMap() {
    QueryWrapper<PaymentChannel> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(PaymentChannel.DELETED, "0");
    queryWrapper.groupBy(PaymentChannel.CODE);
    List<PaymentChannel> list = list(queryWrapper);
    Map<String, PaymentChannel> map = new HashMap<>();
    if (CollectionUtils.isNotEmpty(list)) {
      map = list.stream().collect(Collectors.toMap(PaymentChannel::getCode, a -> a,(k1, k2)->k1));
    }
    return map;
  }

  public Boolean delByMerCode(String merCode) {
    if (StringUtils.isNoneBlank(merCode)) {
      QueryWrapper<PaymentChannel> queryWrapper = new QueryWrapper<>();
      queryWrapper.eq(PaymentChannel.MERCHANT_CODE, merCode);
      return remove(queryWrapper);
    }
    return true;
  }
}