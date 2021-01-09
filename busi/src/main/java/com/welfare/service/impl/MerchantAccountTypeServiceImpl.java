package com.welfare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.dto.MerchantAccountTypePageReq;
import com.welfare.service.dto.MerchantAccountTypeReq;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户信息服务接口实现
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantAccountTypeServiceImpl implements MerchantAccountTypeService {
    private final MerchantAccountTypeDao merchantAccountTypeDao;

    @Override
    public List<MerchantAccountType> list(MerchantAccountTypeReq req) {
        return merchantAccountTypeDao.list(QueryHelper.getWrapper(req));
    }

    @Override
    public MerchantAccountType detail(Long id) {
        return merchantAccountTypeDao.getById(id);
    }

    @Override
    public Page<MerchantAccountType> page(Page page, MerchantAccountTypePageReq pageReq) {
        return null;
    }

    @Override
    public boolean add(MerchantAccountType merchantAccountType) {
        return merchantAccountTypeDao.save(merchantAccountType);
    }

    @Override
    public boolean update(MerchantAccountType merchantAccountType) {
        return merchantAccountTypeDao.updateById(merchantAccountType);
    }

    @Override
    public String exportList(MerchantAccountTypePageReq pageReq) {
        return null;
    }

    @Override
    public boolean moveDeductionsOrder() {
        return false;
    }
}