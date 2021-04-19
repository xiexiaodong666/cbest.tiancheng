package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.PaymentChannelConfig;
import com.welfare.service.dto.paymentChannel.*;

import java.util.List;

/**
 * 服务接口
 *
 * @author Yuxiang Li
 * @since 2021-03-23 09:35:43
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface PaymentChannelConfigService {

  /**
   * 商户支付渠道简要信息
   * @param req
   * @return
   */
  Page<PayChannelConfigSimpleDTO> simplePage(PayChannelConfigSimpleReq req);

  /**
   * 商户支付渠道详情
   * @param condition
   * @return
   */
  PayChannelConfigDetailDTO detail(PayChannelConfigReq condition);

  /**
   * 编辑商户支付渠道配置
   * @param req
   * @return
   */
  Boolean edit(PayChannelConfigEditReq req);

  /**
   * 批量删除支付渠道
   * @param delDtos
   * @return 删除条数
   */
  int batchDel(List<PayChannelConfigDelDTO> delDtos);

  /**
   * 批量新增商户支付渠道
   * @param payChannelConfigDTOS
   * @return
   */
  boolean batchSave(List<PayChannelConfigDTO> payChannelConfigDTOS);

  /**
   * 通过商户编码、门店编码、消费场景查询
   * @param req
   * @return
   */
  List<PaymentChannelConfig> getByMerCodeAndStoreAndConsume(PaymentChannelConfigReq req);
}