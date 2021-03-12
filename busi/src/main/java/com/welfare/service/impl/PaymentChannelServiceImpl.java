package com.welfare.service.impl;

import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.PaymentChannelDao;
import com.welfare.service.PaymentChannelService;
import com.welfare.service.dto.PaymentChannelDTO;
import com.welfare.service.dto.PaymentChannelReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务接口实现
 *
 * @author kancy
 * @since 2021-03-11 17:31:46
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentChannelServiceImpl implements PaymentChannelService {
    private final PaymentChannelDao paymentChannelDao;

    @Override
    public List<PaymentChannelDTO> list(PaymentChannelReq req) {
        // 1.4.5 先写死，后续版本从配置表取
        List<PaymentChannelDTO> result = new ArrayList<>();
        for (WelfareConstant.PaymentChannel paymentChannel : WelfareConstant.PaymentChannel.values()) {
            PaymentChannelDTO paymentChannelDTO = new PaymentChannelDTO();
            paymentChannelDTO.setPaymentChannelName(paymentChannel.name());
            paymentChannelDTO.setPaymentChannelCode(paymentChannel.code());
            result.add(paymentChannelDTO);
        }
        if (req.getFiltered() != null && req.getFiltered()) {
            result = result.stream().filter(paymentChannelDTO ->
                    !Lists.newArrayList(WelfareConstant.PaymentChannel.ALIPAY.code(),WelfareConstant.PaymentChannel.WECHAT.code())
                            .contains(paymentChannelDTO.getPaymentChannelCode()))
                    .collect(Collectors.toList());
        }
        return result;
    }
}