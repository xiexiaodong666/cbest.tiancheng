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

  PayChannelConfigDTO detail(PayChannelConfigReq condition);

  Boolean edit(PayChannelConfigEditReq req);
}