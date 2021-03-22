package com.welfare.service.impl;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.entity.MessagePushConfig;
import com.welfare.service.MessagePushConfigService;
import com.welfare.service.SequenceService;
import com.welfare.service.dto.MessagePushConfigDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 服务接口实现
 *
 * @author kancy
 * @since 2021-03-19 11:01:29
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MessagePushConfigServiceImpl implements MessagePushConfigService {

    private final static String MESSAGE_CONTENT_TEMPLATE = "甜橙生活：今日挂起订单3笔，交易额178元，累计挂起订单12笔，交易额782元，请登录甜橙生活查看并处理，以免影响正常交易。";

}