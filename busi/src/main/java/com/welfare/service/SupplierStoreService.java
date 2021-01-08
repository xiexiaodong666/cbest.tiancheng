package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.SupplierStorePageReq;

import java.util.List;

/**
 * 供应商门店服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface SupplierStoreService {
    /**
     * 查询供应商门店列表（分页）
     * @param req
     * @return
     */
    Page<SupplierStore> page(Page<SupplierStore>page,SupplierStorePageReq req);

    /**
     * 查询供应商门店详情
     * @param id
     * @return
     */
    SupplierStore detail(Long id);

    /**
     * 新增供应商门店
     * @param supplierStore
     * @return
     */
    boolean add(SupplierStore supplierStore);

    /**
     * 更改供应商门店激活状态
     * @param id
     * @param status
     * @return
     */
    boolean activate(Long id,Integer status);

    /**
     * 批量新增供应商门店
     * @param list
     * @return
     */
    boolean batchAdd(List<SupplierStore> list);

    /**
     * 删除供应商门店
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 更新供应商门店
     * @param supplierStore
     * @return
     */
    boolean update(SupplierStore supplierStore);

    /**
     * 导出供应商门店列表
     * @param req
     * @return
     */
    String exportList(SupplierStorePageReq req);
}
