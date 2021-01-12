package com.welfare.service;


import com.welfare.persist.entity.MerchantAddress;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantAddressReq;

import java.util.List;

/**
 * 地址信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MerchantAddressService {

    List<MerchantAddressDTO> list(MerchantAddressReq merchantAddressReq);
    boolean saveOrUpdateBatch(List<MerchantAddressDTO> list);
}
