package com.welfare.service.impl;

import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.PaymentChannelDao;
import com.welfare.service.DictService;
import com.welfare.service.PaymentChannelService;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.PaymentChannelDTO;
import com.welfare.service.dto.PaymentChannelReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private DictService dictService;

    public static final String PAYMENTCHANNEL_DICT_NAME = "PaymentChannel";

    @Override
    public List<PaymentChannelDTO> list(PaymentChannelReq req) {
        if (StringUtils.isNoneBlank(req.getMerCode())) {
            List<PaymentChannelDTO> result = PaymentChannelDTO.of(paymentChannelDao.listByMerCodeGroupByCode(req.getMerCode()));
            if (req.getFiltered() != null && req.getFiltered()) {
                result = result.stream().filter(paymentChannelDTO ->
                        !Lists.newArrayList(WelfareConstant.PaymentChannel.ALIPAY.code(),WelfareConstant.PaymentChannel.WECHAT.code())
                                .contains(paymentChannelDTO.getPaymentChannelCode()))
                        .collect(Collectors.toList());
            }
            return result;
        } else {
            DictReq dictReq = new DictReq();
            dictReq.setDictType(PAYMENTCHANNEL_DICT_NAME);
            return PaymentChannelDTO.of2(dictService.getByType(dictReq));
        }

    }
}