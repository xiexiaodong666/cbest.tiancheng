package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.SupplierStoreWithMerchantDTO;
import com.welfare.persist.dto.query.StorePageReq;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.*;
import org.springframework.web.multipart.MultipartFile;

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
    Page<SupplierStoreWithMerchantDTO> page(Page page, StorePageReq req);
    List<SupplierStore> list(SupplierStoreListReq req);
    List<SupplierStoreTreeDTO> tree(String merCode , String source);

    /**
     * 查询供应商门店详情
     * @param id
     * @return
     */
    SupplierStoreDetailDTO detail(Long id);

    /**
     * 查询供应商门店详情
     * @param storeCode
     * @return
     */
    SupplierStore getSupplierStoreByStoreCode(String storeCode);

    /**
     * 新增供应商门店
     * @param supplierStore
     * @return
     */
    boolean add(SupplierStoreAddDTO supplierStore);

    /**
     * 数据迁移后将所有门店置为激活状态（慎用！）
     * @param reqList
     */
    void batchActivate(List<SupplierStoreActivateReq> reqList);


    /**
     * 更改供应商门店激活状态
     * @return
     */
    boolean activate(SupplierStoreActivateReq storeActivateReq);

    boolean batchAdd(List<SupplierStoreAddDTO> list);

    List<SupplierStore> list(QueryWrapper<SupplierStore> queryWrapper);

    /**
     * 批量新增供应商门店
     * @return
     */
    String upload(MultipartFile multipartFile);

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
    boolean update(SupplierStoreUpdateDTO supplierStore);

    /**
     * 导出供应商门店列表
     * @param req
     * @return
     */
    List<SupplierStoreWithMerchantDTO> exportList(StorePageReq req);

    /**
     * 同步门店消费能力数据
     */
    boolean syncConsumeType(String storeCode, String consumeType);
}
