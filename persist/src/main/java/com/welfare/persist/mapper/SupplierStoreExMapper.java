package com.welfare.persist.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.MerSupplierStoreDTO;
import com.welfare.persist.dto.SupplierStoreWithMerchantDTO;
import com.welfare.persist.dto.query.StorePageReq;
import com.welfare.persist.entity.SupplierStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

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
    List<MerSupplierStoreDTO> queryMerSupplierStoreDTList(@Param("merCode")String merCode);
    List<SupplierStore> listUnionMerchant(@Param("merCodes")Set<String> merCodes);

}
