package com.welfare.service.impl;

import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.MerchantExtendDao;
import com.welfare.persist.entity.MerchantExtend;
import com.welfare.service.MerchantExtendService;
import com.welfare.service.dto.MerchantExtendDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/16 1:54 下午
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MerchantExtendServiceImpl implements MerchantExtendService {

    private final MerchantExtendDao merchantExtendDao;

    @Override
    public boolean saveOrUpdate(MerchantExtendDTO extendDTO, String merCode) {
        BizAssert.isTrue(StringUtils.isNoneBlank(merCode), ExceptionCode.ILLEGALITY_ARGURMENTS, "商户编码为空");
        MerchantExtend merchantExtend = merchantExtendDao.getByMerCode(merCode);
        if (Objects.isNull(merchantExtend)) {
            merchantExtend = new MerchantExtend();
            merchantExtend.setMerCode(merCode);
        }
        if (Objects.nonNull(extendDTO)) {
            merchantExtend.setIndustryTag(extendDTO.getIndustryTag());
            merchantExtend.setPointMall(extendDTO.getPointMall());
        }
        return merchantExtendDao.saveOrUpdate(merchantExtend);
    }
}
