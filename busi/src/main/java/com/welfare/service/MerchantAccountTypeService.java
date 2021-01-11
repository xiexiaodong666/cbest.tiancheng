package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.dto.MerchantAccountTypePageReq;
import com.welfare.service.dto.MerchantAccountTypeReq;

import java.util.List;

/**
 * 商户福利类型服务接口
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MerchantAccountTypeService {
    /**
     * 查询商户列表（不分页）
     * @return
     */
    List<MerchantAccountType>  list(MerchantAccountTypeReq req);

    /**
     * 查询商户详情
     * @param id
     * @return
     */
    MerchantAccountType detail(Long id);

    /**
     * 查询商户列表（分页））
     */
    Page<MerchantAccountType> page(Page page, MerchantAccountTypePageReq pageReq);

    /**
     * 新增商户
     * @return
     */
    boolean add(MerchantAccountType merchantAccountType);

    /**
     * 编辑商户
     * @return
     */
    boolean update(MerchantAccountType merchantAccountType);

    /**
     * 导出商户列表
     * @return
     */
    String exportList(MerchantAccountTypePageReq pageReq);

    /**
     * 移动扣款顺序
     * @return
     */
    boolean moveDeductionsOrder();

    /**
     * 根据编码查询单条
     * @param merCode
     * @param merchantAccountTypeCode
     * @return
     */
    MerchantAccountType queryOneByCode(String merCode, String merchantAccountTypeCode);

    /**
     * 查询商户下的所有MerchantAccountType
     * @param merCode
     * @return
     */
    List<MerchantAccountType> queryByMerCode(String merCode);
}
