package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerchantAccountTypeWithMerchantDTO;
import com.welfare.persist.dto.query.MerchantAccountTypePageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商户账号类型Mapper
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface MerchantAccountTypeExMapper {
    Page<MerchantAccountTypeWithMerchantDTO> listWithMerchant(
            Page page,@Param("req") MerchantAccountTypePageReq req);
}
