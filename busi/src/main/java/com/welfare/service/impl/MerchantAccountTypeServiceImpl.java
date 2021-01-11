package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.regexp.internal.RE;
import com.welfare.persist.dao.MerchantAccountTypeDao;
import com.welfare.persist.dto.MerchantAccountTypeWithMerchantDTO;
import com.welfare.persist.dto.query.MerchantAccountTypePageReq;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.mapper.MerchantAccountTypeExMapper;
import com.welfare.service.MerchantAccountTypeService;
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
    private final MerchantAccountTypeExMapper merchantAccountTypeExMapper;

    @Override
    public List<MerchantAccountType> list(MerchantAccountTypeReq req) {
        return merchantAccountTypeDao.list(QueryHelper.getWrapper(req));
    }

    @Override
    public MerchantAccountType detail(Long id) {
        return merchantAccountTypeDao.getById(id);
    }

    @Override
    public Page<MerchantAccountTypeWithMerchantDTO> page(Page page, MerchantAccountTypePageReq req) {
        return merchantAccountTypeExMapper.listWithMerchant(page, req);
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

    @Override
    public MerchantAccountType queryOneByCode(String merCode, String merchantAccountTypeCode) {
        QueryWrapper<MerchantAccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantAccountType.MER_ACCOUNT_TYPE_CODE,merchantAccountTypeCode)
                .eq(MerchantAccountType.MER_CODE,merCode);
        return merchantAccountTypeDao.getOne(queryWrapper);
    }

    @Override
    public List<MerchantAccountType> queryByMerCode(String merCode) {
        QueryWrapper<MerchantAccountType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MerchantAccountType.MER_CODE,merCode);
        return merchantAccountTypeDao.list(queryWrapper);
    }
}