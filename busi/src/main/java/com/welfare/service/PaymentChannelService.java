package com.welfare.service;


import com.welfare.service.dto.PaymentChannelDTO;
import com.welfare.service.dto.PaymentChannelReq;

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
  List<PaymentChannelDTO> list(PaymentChannelReq req);
}
