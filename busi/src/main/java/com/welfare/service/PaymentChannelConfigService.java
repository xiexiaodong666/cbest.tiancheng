package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

  Page<PayChannelConfigSimpleDTO> simplePage(PayChannelConfigSimpleReq req);

  PayChannelConfigDetailDTO detail(PayChannelConfigReq condition);

  Boolean edit(PayChannelConfigEditReq req);

  /**
   * 评论删除支付渠道
   * @param delDtos
   * @return 删除条数
   */
  int batchDel(List<PayChannelConfigDelDTO> delDtos);

  /**
   * 通过门店编码和消费方式删除
   * @param storeCode
   * @param consumeType
   * @return
   */
  int delByStoreCodeAndConsumeType(String storeCode, String consumeType);

  boolean batchSave(List<PayChannelConfigDTO> payChannelConfigDTOS);
}