package com.welfare.service.impl;

import com.welfare.service.MessagePushConfigService;
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
    private final MessagePushConfigDao messagePushConfigDao;

}