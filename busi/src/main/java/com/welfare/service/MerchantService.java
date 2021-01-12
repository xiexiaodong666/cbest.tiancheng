package com.welfare.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantWithCreditDTO;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.service.dto.MerchantDetailDTO;
import com.welfare.service.dto.MerchantReq;

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
     * 查询商户列表（分页））
     * @param merchantPageReq
     * @return
     */
    Page<MerchantWithCreditDTO> page(Page<Merchant> page, MerchantPageReq merchantPageReq);

    /**
     * 新增商户
     * @param merchant
     * @return
     */
    boolean add(MerchantDetailDTO merchant);

    /**
     * 编辑商户
     * @param merchant
     * @return
     */
    boolean update(MerchantDetailDTO merchant);

    /**
     * 导出商户列表
     * @param merchantPageReq
     * @return
     */
    List<MerchantWithCreditDTO> exportList(MerchantPageReq merchantPageReq);


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

}
