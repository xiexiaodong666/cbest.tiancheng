package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.SupplierStoreWithMerchantDTO;
import com.welfare.persist.dto.query.MerchantPageReq;
import com.welfare.persist.dto.query.StorePageReq;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 门店数据Mapper
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface SupplierStoreExMapper {
    Page<SupplierStoreWithMerchantDTO> listWithMerchant(
            Page page,@Param("req") StorePageReq req);
}
