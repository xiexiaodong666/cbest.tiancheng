package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.dao.MerAccountTypeConsumeSceneConfigDao;
import com.welfare.persist.dao.MerchantExtendDao;
import com.welfare.persist.entity.MerAccountTypeConsumeSceneConfig;
import com.welfare.persist.entity.MerchantExtend;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.MerchantExtendService;
import com.welfare.service.SupplierStoreService;
import com.welfare.service.dto.MerchantExtendDTO;
import com.welfare.service.dto.SupplierStoreListReq;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import org.springframework.transaction.annotation.Transactional;

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
    private final MerAccountTypeConsumeSceneConfigDao merAccountTypeConsumeSceneConfigDao;
    @Autowired
    private SupplierStoreService supplierStoreService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdate(MerchantExtendDTO extendDTO, String merCode) {
        BizAssert.isTrue(StringUtils.isNoneBlank(merCode), ExceptionCode.ILLEGALITY_ARGUMENTS, "商户编码为空");
        MerchantExtend merchantExtend = merchantExtendDao.getByMerCode(merCode);
        if (Objects.isNull(merchantExtend)) {
            merchantExtend = new MerchantExtend();
            merchantExtend.setMerCode(merCode);
        }
        if (Objects.nonNull(extendDTO)) {
            merchantExtend.setIndustryTag(extendDTO.getIndustryTag());
            merchantExtend.setPointMall(extendDTO.getPointMall());
            merchantExtend.setSupplierWholesaleSettleMethod(extendDTO.getSupplierWholesaleSettleMethod());
            if(StringUtils.isNotBlank(merchantExtend.getSupplierWholesaleSettleMethod()) && StringUtils.isBlank(extendDTO.getSupplierWholesaleSettleMethod())) {
                deleteInitConsume(merCode);
            }
        }
        return merchantExtendDao.saveOrUpdate(merchantExtend);
    }

    /**
     * 删除初始化的福利类型消费场景配置
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteInitConsume(String merCode) {
        SupplierStoreListReq supplierStoreListReq = new SupplierStoreListReq();
        supplierStoreListReq.setMerCode(merCode);
        List<SupplierStore> supplierStoreList = supplierStoreService.list(supplierStoreListReq);
        if(CollectionUtils.isNotEmpty(supplierStoreList)) {
            SupplierStore supplierStore = supplierStoreList.get(0);
            QueryWrapper<MerAccountTypeConsumeSceneConfig> consumeSceneConfigQueryWrapper = new QueryWrapper<>();
            consumeSceneConfigQueryWrapper.eq(MerAccountTypeConsumeSceneConfig.STORE_CODE, supplierStore.getStoreCode());
            consumeSceneConfigQueryWrapper.eq(MerAccountTypeConsumeSceneConfig.MER_ACCOUNT_TYPE_CODE, WelfareConstant.MerAccountTypeCode.WHOLESALE_PROCUREMENT.code());

            merAccountTypeConsumeSceneConfigDao.getBaseMapper().delete(consumeSceneConfigQueryWrapper);
        }

    }

}
