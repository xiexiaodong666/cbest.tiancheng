package com.welfare.service;

import com.welfare.persist.entity.MerchantExtend;
import com.welfare.service.dto.MerchantExtendDTO;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/16 1:53 下午
 */
public interface MerchantExtendService {

    boolean saveOrUpdate(MerchantExtendDTO extendDTO, String merCode);
}
