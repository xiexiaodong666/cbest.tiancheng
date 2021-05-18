package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantAccountTypeWithMerchantDTO;
import com.welfare.persist.dto.query.MerchantAccountTypePageReq;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.dto.*;

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

    List<MerchantAccountType>  listExclusion(MerchantAccountTypeReq req);

    boolean init(String merCode,  MerchantExtendDTO extend);

    /**
     * 查询商户详情
     * @param id
     * @return
     */
    MerchantAccountTypeDetailDTO detail(Long id);

    /**
     * 查询商户列表（分页））
     */
    Page<MerchantAccountTypeWithMerchantDTO> page(Page page, MerchantAccountTypePageReq pageReq);

    /**
     * 新增商户
     * @return
     */
    boolean add(MerchantAccountTypeAddDTO merchantAccountType);

    /**
     * 编辑商户
     * @return
     */
    boolean update(MerchantAccountTypeUpdateDTO merchantAccountType);

    /**
     * 导出商户列表
     * @return
     */
    List<MerchantAccountTypeWithMerchantDTO> exportList(MerchantAccountTypePageReq pageReq);

    /**
     * @Deprecated
     * 移动扣款顺序
     * @return
     */
    boolean moveDeductionsOrder(MerchantAccountTypeSortReq merchantAccountTypeSortReq);

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
    List<MerchantAccountType> queryShowedByMerCode(String merCode);

    /**
     * 查询商户下的所有MerchantAccountType
     * @param merCode
     * @return
     */
    List<MerchantAccountType> queryAllByMerCode(String merCode);

    /**
     * 新增
     * @param merchantAccountType
     */
    void saveIfNotExist(MerchantAccountType merchantAccountType);
}
