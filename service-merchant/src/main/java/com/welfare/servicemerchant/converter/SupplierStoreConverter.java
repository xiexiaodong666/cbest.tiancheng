package com.welfare.servicemerchant.converter;

import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.converter.BaseConverter;
import com.welfare.servicemerchant.dto.SupplierStoreInfo;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface SupplierStoreConverter extends BaseConverter<SupplierStoreInfo, SupplierStore> {
}
