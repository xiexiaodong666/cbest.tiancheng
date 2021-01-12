package com.welfare.service.impl;

import  com.welfare.persist.dao.MerchantAddressDao;
import com.welfare.persist.entity.MerchantAddress;
import com.welfare.service.converter.MerchantAddressConverter;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantAddressReq;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantAddressService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地址信息服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantAddressServiceImpl implements MerchantAddressService {
    private final MerchantAddressDao merchantAddressDao;
    private final MerchantAddressConverter merchantAddressConverter;

    @Override
    public List<MerchantAddressDTO> list(MerchantAddressReq merchantAddressReq) {
        return merchantAddressConverter.toD(merchantAddressDao.list(QueryHelper.getWrapper(merchantAddressReq)));
    }

    @Override
    public boolean saveOrUpdateBatch(List<MerchantAddressDTO> list) {

        return merchantAddressDao.saveOrUpdateBatch(merchantAddressConverter.toE(list));
    }
}