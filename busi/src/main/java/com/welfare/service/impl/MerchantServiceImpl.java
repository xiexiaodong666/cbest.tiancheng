package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.welfare.persist.dao.MerchantDao;
import com.welfare.persist.dto.MerchantWithCreditDTO;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.mapper.MerchantExMapper;
import com.welfare.service.dto.MerchantReq;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.MerchantService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 商户信息服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class MerchantServiceImpl implements MerchantService {
    private final MerchantDao merchantDao;
    private final MerchantExMapper merchantExMapper;


    @Override
    public List<Merchant> list(MerchantReq req) {
        return merchantDao.list(QueryHelper.getWrapper(req));
    }

    @Override
    public Merchant detail(Long id) {
        return merchantDao.getById(id);
    }

    @Override
    public Page<MerchantWithCreditDTO> page(Page<Merchant> page, MerchantPageReq merchantPageReq) {
        return merchantExMapper.listWithCredit(page,merchantPageReq);
    }

    @Override
    public boolean add(Merchant merchant) {
        return merchantDao.save(merchant);
    }

    @Override
    public boolean update(Merchant merchant) {
        return merchantDao.updateById(merchant);
    }

    @Override
    public String exportList(MerchantPageReq merchantPageReq) {
        return null;
    }

    @Override
    public Merchant getMerchantByMerCode(QueryWrapper<Merchant> queryWrapper) {
        return merchantDao.getOne(queryWrapper);
    }


}