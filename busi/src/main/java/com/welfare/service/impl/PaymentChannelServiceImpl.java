package com.welfare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.PaymentChannelDao;
import com.welfare.persist.dto.PayChannelMerchantDTO;
import com.welfare.persist.entity.PaymentChannel;
import com.welfare.service.PaymentChannelConfigService;
import com.welfare.service.PaymentChannelService;
import com.welfare.service.dto.PaymentChannelReq;
import com.welfare.service.dto.paymentChannel.PayChannelConfigDelDTO;
import com.welfare.service.dto.paymentChannel.PaymentChannelDTO;
import com.welfare.service.dto.paymentChannel.PaymentChannelMerchantDTO;
import com.welfare.service.dto.paymentChannel.PaymentChannelSortReq;
import com.welfare.service.utils.PageReq;
import com.welfare.service.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
    private PaymentChannelConfigService paymentChannelConfigService;

    @Override
    public List<com.welfare.service.dto.PaymentChannelDTO> list(PaymentChannelReq req) {
        List<com.welfare.service.dto.PaymentChannelDTO> result;
        if (StringUtils.isNoneBlank(req.getMerCode())) {
            result = com.welfare.service.dto.PaymentChannelDTO.of(paymentChannelDao.listByMerCodeGroupByCode(req.getMerCode()));
            if (CollectionUtils.isEmpty(result)) {
                result = com.welfare.service.dto.PaymentChannelDTO.of(paymentChannelDao.listByDefaultGroupByCode());
            }
        } else {
            result = com.welfare.service.dto.PaymentChannelDTO.of(paymentChannelDao.listByDefaultGroupByCode());
        }
        if (req.getFiltered() != null && req.getFiltered()) {
            if (CollectionUtils.isNotEmpty(result)) {
                result = result.stream().filter(paymentChannelDTO ->
                        !Lists.newArrayList(WelfareConstant.PaymentChannel.ALIPAY.code(),WelfareConstant.PaymentChannel.WECHAT.code())
                                .contains(paymentChannelDTO.getPaymentChannelCode()))
                        .collect(Collectors.toList());
            }
        }
        return result;
    }

    @Override
    public List<PaymentChannelDTO> defaultList() {
        List<PaymentChannelDTO> resps = new ArrayList<>();
        List<PaymentChannel> paymentChannels = paymentChannelDao.listByDefaultGroupByCode();
        if (CollectionUtils.isNotEmpty(paymentChannels)) {
            paymentChannels.stream().forEach(paymentChannel -> {
                PaymentChannelDTO dto = new PaymentChannelDTO();
                BeanUtils.copyProperties(paymentChannel, dto);
                dto.setId(paymentChannel.getId() + "");
                resps.add(dto);
            });
        }
        return resps;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean delByMerCode(String merCode) {
        PaymentChannelReq req = new PaymentChannelReq();
        req.setFiltered(false);
        req.setMerCode(merCode);
        List<PaymentChannel> oldList = paymentChannelDao.listByMerCodeGroupByCode(req.getMerCode());
        List<PaymentChannel> defaultList = paymentChannelDao.listByDefaultGroupByCode();
        if (CollectionUtils.isNotEmpty(oldList) && CollectionUtils.isNotEmpty(defaultList)) {
            Map<String, PaymentChannel> defaultMap = defaultList.stream().collect(Collectors
                    .toMap(PaymentChannel::getCode, a -> a, (k1, k2) -> k1));
            List<PaymentChannel> specialList;
            specialList = oldList.stream().filter(item -> !defaultMap.containsKey(item.getCode())).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(specialList)) {
                // 删除商户特殊支付渠道配置
                paymentChannelConfigService.batchDel(PayChannelConfigDelDTO.of(merCode, specialList));
            }
        }
        return paymentChannelDao.delByMerCode(merCode);
    }

    @Override
    public List<PaymentChannelDTO> specialListByMerCode(String merCode) {
        List<PaymentChannelDTO> resps = new ArrayList<>();
        List<PaymentChannel> paymentChannels = paymentChannelDao.listByMerCodeGroupByCode(merCode);
        if (CollectionUtils.isEmpty(paymentChannels)) {
            return resps;
        }
        paymentChannels.forEach(paymentChannel -> {
            PaymentChannelDTO resp = new PaymentChannelDTO();
            resp.setCode(paymentChannel.getCode());
            resp.setMerchantCode(paymentChannel.getMerchantCode());
            resp.setName(paymentChannel.getName());
            resp.setShowOrder(paymentChannel.getShowOrder());
            resp.setId(paymentChannel.getId() + "");
            resps.add(resp);
        });
        return resps;
    }

    @Override
    public List<PaymentChannelDTO> defaultListByMerCode(String merCode) {
        List<PaymentChannelDTO> dtos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(paymentChannelDao.listByMerCodeGroupByCode(merCode))) {
            return dtos;
        } else {
            dtos = defaultList();
            if (CollectionUtils.isNotEmpty(dtos)) {
                dtos.forEach(paymentChannelDTO -> {
                    paymentChannelDTO.setMerchantCode(merCode);
                    paymentChannelDTO.setId(null);
                });
            }
        }
        return dtos;
    }

    @Override
    public Page<PaymentChannelMerchantDTO> merchantPayChannelList(PageReq pageReq) {
        Page<PayChannelMerchantDTO> page = new Page<>(pageReq.getCurrent(), pageReq.getSize());
        page = paymentChannelDao.getBaseMapper().merchantPayChannelList(page);
        List<PaymentChannelMerchantDTO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            page.getRecords().forEach(payChannelMerchantDTO -> {
                PaymentChannelMerchantDTO dto = new PaymentChannelMerchantDTO();
                BeanUtils.copyProperties(payChannelMerchantDTO, dto);
                list.add(dto);
            });
        }
        return PageUtils.toPage(page, list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean sort(List<PaymentChannelSortReq> sorts) {
        boolean flag = true;
        if (CollectionUtils.isNotEmpty(sorts)) {
            List<PaymentChannel> saveOrUpdateList = new ArrayList<>();
            sorts.forEach(sort -> {
                if (StringUtils.isNoneBlank(sort.getId())) {
                    PaymentChannel paymentChannel = paymentChannelDao.getById(sort.getId());
                    Assert.notNull(paymentChannel, String.format("支付渠道不存在[%s]", sort.getId()));
                    paymentChannel.setShowOrder(sort.getShowOrder());
                    saveOrUpdateList.add(paymentChannel);
                } else {
                    PaymentChannel paymentChannel = new PaymentChannel();
                    paymentChannel.setCode(sort.getCode());
                    paymentChannel.setMerchantCode(sort.getMerchantCode());
                    paymentChannel.setShowOrder(sort.getShowOrder());
                    paymentChannel.setName(sort.getName());
                    paymentChannel.setDeleted(0);
                    paymentChannel.setCreateTime(new Date());
                    paymentChannel.setCreateUser("system");
                    paymentChannel.setVersion(0);
                    saveOrUpdateList.add(paymentChannel);
                }
            });
            if (CollectionUtils.isNotEmpty(saveOrUpdateList)) {
                flag = paymentChannelDao.saveOrUpdateBatch(saveOrUpdateList);
            }
        }
        return flag;
    }
}