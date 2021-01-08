package com.welfare.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import  com.welfare.persist.dao.SupplierStoreDao;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.SupplierStorePageReq;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.SupplierStoreService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商门店服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SupplierStoreServiceImpl implements SupplierStoreService {
    private final SupplierStoreDao supplierStoreDao;

    @Override
    public Page<SupplierStore> page(Page<SupplierStore>page ,SupplierStorePageReq req) {
        return supplierStoreDao.page(page, QueryHelper.getWrapper(req));
    }

    @Override
    public SupplierStore detail(Long id) {
        return supplierStoreDao.getById(id);
    }

    @Override
    public boolean add(SupplierStore supplierStore) {
        return supplierStoreDao.save(supplierStore);
    }

    @Override
    public boolean activate(Long id, Integer status) {
        SupplierStore supplierStore=new SupplierStore();
        supplierStore.setId(id);
        supplierStore.setStatus(status);
        return supplierStoreDao.updateById(supplierStore);
    }

    @Override
    public boolean batchAdd(List<SupplierStore> list) {
        return supplierStoreDao.saveBatch(list);
    }

    @Override
    public boolean delete(Long id) {
        return supplierStoreDao.removeById(id);
    }

    @Override
    public boolean update(SupplierStore supplierStore) {
        return supplierStoreDao.updateById(supplierStore);
    }

    @Override
    public String exportList(SupplierStorePageReq req) {
        return null;
    }
}