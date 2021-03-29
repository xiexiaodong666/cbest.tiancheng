package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.PaymentChannelReq;
import com.welfare.service.dto.paymentChannel.PaymentChannelDTO;
import com.welfare.service.dto.paymentChannel.PaymentChannelMerchantDTO;
import com.welfare.service.dto.paymentChannel.PaymentChannelSortReq;
import com.welfare.service.utils.PageReq;

import java.util.List;

/**
 * 服务接口
 *
 * @author kancy
 * @since 2021-03-11 17:31:46
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface PaymentChannelService {

  /**
   * 根据条件查询支付取代
   * @return
   */
  List<com.welfare.service.dto.PaymentChannelDTO> list(PaymentChannelReq req);

  /**
   * 查询默认的支付渠道
   * @return
   */
  List<PaymentChannelDTO> defaultList();

  /**
   * 删除商户的支付渠道排序配置
   * @param merCode
   * @return
   */
  Boolean delByMerCode(String merCode);

  /**
   * 根据商户编码查询支付渠道配列表,没有独立配置就返回null
   * @param merCode
   * @return
   */
  List<PaymentChannelDTO>  specialListByMerCode(String merCode);

  /**
   * 查询默认的支付渠道，如果商户有独立的配置就返回null
   * @param merCode
   * @return
   */
  List<PaymentChannelDTO>  defaultListByMerCode(String merCode);

  /**
   * 查询已配置独立支付渠道或已排序的商户
   * @param pageReq
   * @return
   */
  Page<PaymentChannelMerchantDTO> merchantPayChannelList(PageReq pageReq);

  /**
   * 排序
   * @param req
   * @return
   */
  Boolean sort(List<PaymentChannelSortReq> req);
}
