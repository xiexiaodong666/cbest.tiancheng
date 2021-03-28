package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.persist.dao.MerchantDao;
import com.welfare.persist.dao.PaymentChannelConfigDao;
import com.welfare.persist.dto.PayChannelConfigSimple;
import com.welfare.persist.dto.PaymentChannelConfigDetailDTO;
import com.welfare.persist.dto.query.PayChannelConfigQuery;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.PaymentChannelConfig;
import com.welfare.service.DictService;
import com.welfare.service.PaymentChannelConfigService;
import com.welfare.service.PaymentChannelService;
import com.welfare.service.dto.DictDTO;
import com.welfare.service.dto.DictReq;
import com.welfare.service.dto.PaymentChannelReq;
import com.welfare.service.dto.paymentChannel.*;
import com.welfare.service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-03-23 09:35:43
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class PaymentChannelConfigServiceImpl implements PaymentChannelConfigService {
    private final PaymentChannelConfigDao paymentChannelConfigDao;
    private final MerchantDao merchantDao;
    @Autowired
    private PaymentChannelService paymentChannelService;
    @Autowired
    private DictService dictService;
    @Override
    public Page<PayChannelConfigSimpleDTO> simplePage(PayChannelConfigSimpleReq req) {
        Page<PayChannelConfigSimple> page = new Page<>(req.getCurrent(), req.getSize());
        paymentChannelConfigDao.getBaseMapper().simplePage(page, req.getMerchantName());
        List<PayChannelConfigSimpleDTO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(payChannelConfigSimple -> {
                PayChannelConfigSimpleDTO dto = new PayChannelConfigSimpleDTO();
                BeanUtils.copyProperties(payChannelConfigSimple, dto);
                list.add(dto);
            });
        }
        return PageUtils.toPage(page, list);
    }

    @Override
    public PayChannelConfigDetailDTO detail(PayChannelConfigReq condition) {
        Merchant merchant = merchantDao.queryByCode(condition.getMerchantCode());
        Assert.notNull(merchant, "商户不存在");
        PaymentChannelReq channelReq = new PaymentChannelReq();
        channelReq.setFiltered(false);
        channelReq.setMerCode(merchant.getMerCode());
        List<com.welfare.service.dto.PaymentChannelDTO> allPaymentChannel = paymentChannelService.list(channelReq);
        PayChannelConfigDetailDTO dto = new PayChannelConfigDetailDTO();
        dto.setMerchantCode(merchant.getMerCode());
        dto.setMerchantName(merchant.getMerName());
        // 查询所有的消费场景
        DictReq dictReq = new DictReq();
        dictReq.setDictType(WelfareConstant.DictType.STORE_CONSUM_TYPE.code());
        List<DictDTO> allConsumeTypes = dictService.getByType(dictReq);
        dto.setHeader(PayChannelConfigDetailDTO.ConsumeType.of(allConsumeTypes));
        PayChannelConfigQuery query = new PayChannelConfigQuery();
        BeanUtils.copyProperties(condition, query);
        List<PaymentChannelConfigDetailDTO> configs = paymentChannelConfigDao.getBaseMapper().list(query);
        List<PayChannelConfigRowDTO> rows = new ArrayList<>();
        dto.setRows(rows);
        if (CollectionUtils.isNotEmpty(configs)) {
            // 根据消费门店编码分组
            Map<String, List<PaymentChannelConfigDetailDTO>> storeMap = 
                    configs.stream().collect(Collectors.groupingBy(PaymentChannelConfigDetailDTO::getStoreCode));
            storeMap.forEach((storeCode, dtos) -> {
                PayChannelConfigRowDTO row = new PayChannelConfigRowDTO();
                row.setStoreCode(storeCode);
                row.setStoreName(dtos.get(0).getStoreName());
                rows.add(row);
                // 根据消费场景分组
                if (!StringUtils.isEmpty(dtos.get(0).getConsumeType())) {
                    Map<String, List<PaymentChannelConfigDetailDTO>> consumeTypeMap =
                            dtos.stream().collect(Collectors.groupingBy(PaymentChannelConfigDetailDTO::getConsumeType));
                    List<PayChannelConfigRowDTO.ConsumeTypeAndPayChannel> consumeAndChannels = new ArrayList<>();
                    row.setConsumeTypeAndPayChannels(consumeAndChannels);
                    consumeTypeMap.forEach((consumeType, dtos2) -> {
                        PayChannelConfigRowDTO.ConsumeTypeAndPayChannel consumeAndChannel = new PayChannelConfigRowDTO.ConsumeTypeAndPayChannel();
                        consumeAndChannel.setConsumeType(consumeType);
                        consumeAndChannels.add(consumeAndChannel);
                        List<PayChannelConfigRowDTO.PaymentChannel> paymentChannels = new ArrayList<>();
                        consumeAndChannel.setPaymentChannels(paymentChannels);
                        dtos2.forEach(paymentChannelConfig -> {
                            PayChannelConfigRowDTO.PaymentChannel paymentChannel = new PayChannelConfigRowDTO.PaymentChannel();
                            paymentChannel.setId(paymentChannelConfig.getPaymentChannelConfigId());
                            paymentChannel.setPaymentChannelCode(paymentChannelConfig.getPaymentChannelCode());
                            paymentChannel.setPaymentChannelName(paymentChannelConfig.getPaymentChannelName());
                            paymentChannel.setSelected(true);
                            if (paymentChannelConfig.getPaymentChannelCode().equals(WelfareConstant.PaymentChannel.WELFARE.code())) {
                                paymentChannel.setEnable(false);
                            } else {
                                paymentChannel.setEnable(true);
                            }
                            paymentChannels.add(paymentChannel);
                        });
                        // 补全其余支付渠道
                        if (CollectionUtils.isEmpty(condition.getPaymentChannelCode())) {
                            paymentChannels.addAll(getCompletionOtherPaymentChannel(allPaymentChannel, paymentChannels));
                        }
                    });
                }
            });
        }
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean edit(PayChannelConfigEditReq req) {
        List<PaymentChannelConfig> saveList = new ArrayList<>();
        List<Long> removeList = new ArrayList<>();
        if (Objects.nonNull(req) && CollectionUtils.isNotEmpty(req.getRows())) {
            req.getRows().forEach(payChannelConfigRowDTO -> {
                if (CollectionUtils.isNotEmpty(payChannelConfigRowDTO.getConsumeTypeAndPayChannels())) {
                    payChannelConfigRowDTO.getConsumeTypeAndPayChannels().forEach(consumeTypeAndPayChannel -> {
                        if (CollectionUtils.isNotEmpty(consumeTypeAndPayChannel.getPaymentChannels())) {
                            consumeTypeAndPayChannel.getPaymentChannels().forEach(paymentChannel -> {
                                if (paymentChannel.getEnable()) {
                                    if (StringUtils.isEmpty(paymentChannel.getId()) && paymentChannel.getSelected()) {
                                        // 新勾选的
                                        PaymentChannelConfig config = new PaymentChannelConfig();
                                        config.setPaymentChannelCode(paymentChannel.getPaymentChannelCode());
                                        config.setPaymentChannelName(paymentChannel.getPaymentChannelName());
                                        config.setConsumeType(consumeTypeAndPayChannel.getConsumeType());
                                        config.setStoreCode(payChannelConfigRowDTO.getStoreCode());
                                        config.setMerCode(req.getMerchantCode());
                                        saveList.add(config);
                                    } else if (!StringUtils.isEmpty(paymentChannel.getId()) && !paymentChannel.getSelected()) {
                                        // 取消勾选的
                                        removeList.add(Long.valueOf(paymentChannel.getId()));
                                    }
                                }
                            });
                        }
                    });
                }
            });
        }
        boolean flag = true;
        boolean flag3 = true;
        if (CollectionUtils.isNotEmpty(saveList)) {
            flag = paymentChannelConfigDao.saveBatch(saveList);
        }
        if (CollectionUtils.isNotEmpty(removeList)) {
            flag3 = paymentChannelConfigDao.removeByIds(removeList);
        }
        return flag && flag3;
    }

    @Override
    public int batchDel(List<PayChannelConfigDelDTO> delDtos) {
        if (CollectionUtils.isNotEmpty(delDtos)) {
            QueryWrapper<PaymentChannelConfig> queryWrapper = new QueryWrapper<>();
            delDtos.forEach(delDTO -> {
                queryWrapper.or(wrapper -> {
                    if (StringUtils.hasText(delDTO.getMerCode())) {
                        wrapper.eq(PaymentChannelConfig.MER_CODE, delDTO.getMerCode());
                    }
                    if (StringUtils.hasText(delDTO.getStoreCode())) {
                        wrapper.eq(PaymentChannelConfig.STORE_CODE, delDTO.getStoreCode());
                    }
                    if (StringUtils.hasText(delDTO.getConsumeType())) {
                        wrapper.eq(PaymentChannelConfig.CONSUME_TYPE, delDTO.getConsumeType());
                    }
                    if (StringUtils.hasText(delDTO.getPaymentChannel())) {
                        wrapper.eq(PaymentChannelConfig.PAYMENT_CHANNEL_CODE, delDTO.getPaymentChannel());
                    }
                });
            });
            return paymentChannelConfigDao.getBaseMapper().delete(queryWrapper);
        }
        return 0;
    }

    @Override
    public int delByStoreCodeAndConsumeType(String storeCode, String consumeType) {
        if (StringUtils.isEmpty(storeCode) || StringUtils.isEmpty(consumeType)) {
            throw new BusiException("门店编码或消费方式不能为空");
        }
        QueryWrapper<PaymentChannelConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(PaymentChannelConfig.STORE_CODE, storeCode)
                .eq(PaymentChannelConfig.CONSUME_TYPE, consumeType);
        return paymentChannelConfigDao.getBaseMapper().delete(queryWrapper);
    }

    @Override
    public boolean batchSave(List<PayChannelConfigDTO> payChannelConfigDTOS) {
        if (CollectionUtils.isNotEmpty(payChannelConfigDTOS)) {
            List<PaymentChannelConfig> channelConfigs = new ArrayList<>();
            payChannelConfigDTOS.forEach(payChannelConfigDTO -> {
                PaymentChannelConfig channelConfig = new PaymentChannelConfig();
                BeanUtils.copyProperties(payChannelConfigDTO, channelConfig);
                channelConfigs.add(channelConfig);
            });
            return paymentChannelConfigDao.saveBatch(channelConfigs);
        }
        return true;
    }

    private List<PayChannelConfigRowDTO.PaymentChannel> getCompletionOtherPaymentChannel(List<com.welfare.service.dto.PaymentChannelDTO> all,
                                                                                         List<PayChannelConfigRowDTO.PaymentChannel> selectedPaymentChannel) {
        List<PayChannelConfigRowDTO.PaymentChannel> otherPaymentChannels = new ArrayList<>();
        Map<String, PayChannelConfigRowDTO.PaymentChannel> selectedMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(selectedPaymentChannel)) {
            selectedMap = selectedPaymentChannel.stream().collect(Collectors.toMap(PayChannelConfigRowDTO.PaymentChannel::getPaymentChannelCode, a -> a,(k1,k2)->k1));
        }
        if (CollectionUtils.isNotEmpty(all)) {
            for (com.welfare.service.dto.PaymentChannelDTO dto : all) {
                //从已选择的支取渠道中匹配其它的渠道
                if (!selectedMap.containsKey(dto.getPaymentChannelCode())) {
                    PayChannelConfigRowDTO.PaymentChannel otherPaymentChannel = new PayChannelConfigRowDTO.PaymentChannel();
                    otherPaymentChannel.setPaymentChannelName(dto.getPaymentChannelName());
                    otherPaymentChannel.setPaymentChannelCode(dto.getPaymentChannelCode());
                    otherPaymentChannel.setSelected(false);
                    otherPaymentChannel.setEnable(true);
                    otherPaymentChannels.add(otherPaymentChannel);
                }
            }
        }
        return otherPaymentChannels;
    }
}
