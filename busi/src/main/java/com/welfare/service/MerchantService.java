package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.dto.*;

import java.util.List;

/**
 * 商户信息服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MerchantService {
    /**
     * 查询商户列表（不分页）
     * @return
     */
    List<Merchant>  list(MerchantReq req);

    /**
     * 查询商户详情
     * @param id
     * @return
     */
    MerchantDetailDTO detail(Long id);

    /**
     * 查询商户列表（树形））
     * @param merchantPageReq
     * @return
     */
    List<MerchantWithCreditAndTreeDTO> tree(MerchantPageReq merchantPageReq);

    /**
     * 新增商户
     * @param merchant
     * @return
     */
    boolean add(MerchantAddDTO merchant);


    /**
     * 编辑商户
     * @param merchant
     * @return
     */
    boolean update(MerchantUpdateDTO merchant);

    /**
     * 导出商户列表
     * @param merchantPageReq
     * @return
     */
    List<MerchantWithCreditAndTreeDTO> exportList(MerchantPageReq merchantPageReq);


    /**
     * 根据MerCode查询商户详情
     * @param queryWrapper
     * @return
     */
    Merchant getMerchantByMerCode(QueryWrapper<Merchant> queryWrapper);

    Merchant getMerchantByMerCode(String merCode);

    /**
     * 通过merCode查询商户详情
     * @param merCode
     * @return
     */
    Merchant detailByMerCode(String merCode);

    /**
     * 根据编码查询
     * @param merCode
     * @return
     */
    Merchant queryByCode(String merCode);

    List<Merchant> queryMerchantByCodeList(List<String> merCodeList);


    /**
     * 通过商户编码查询配置的门店是否有返利
     * @param merCode
     * @return
     */
    Boolean queryIsRabteByMerCOde(String merCode);

    /**
     * 查询商户的供应商列表（不分页）
     * @return
     */
    List<Merchant> supplierByMer(String merCode);

    List<Merchant> wholesaleByMer(String merCode);
}
