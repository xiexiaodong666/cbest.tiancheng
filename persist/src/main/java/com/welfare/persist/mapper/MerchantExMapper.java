package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantStoreRelationDTO;
import com.welfare.persist.dto.MerchantWithCreditDTO;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.entity.MerchantStoreRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商户信息(merchant)数据Mapper
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantExMapper  {
    Page<MerchantWithCreditDTO> listWithCredit(
            Page page,@Param("req") MerchantPageReq req);
}
