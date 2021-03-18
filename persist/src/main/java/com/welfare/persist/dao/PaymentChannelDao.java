package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.entity.PaymentChannel;
import com.welfare.persist.mapper.PaymentChannelMapper;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

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

  public List<PaymentChannel> listByMerCodeGroupByCode(String merCode) {
    QueryWrapper<PaymentChannel> queryWrapper = new QueryWrapper<>();
    if (StringUtils.isNoneBlank(merCode)) {
      queryWrapper.eq(PaymentChannel.MERCHANT_CODE, merCode);
    }
    queryWrapper.eq(PaymentChannel.DELETED, "0");
    queryWrapper.groupBy(PaymentChannel.CODE);
    return list(queryWrapper);
  }
}