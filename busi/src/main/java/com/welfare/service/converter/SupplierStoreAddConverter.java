package com.welfare.service.converter;

import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.SupplierStoreAddDTO;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface SupplierStoreAddConverter extends BaseConverter<SupplierStoreAddDTO, SupplierStore> {
}
