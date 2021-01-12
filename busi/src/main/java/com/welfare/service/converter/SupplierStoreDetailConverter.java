package com.welfare.service.converter;

import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.SupplierStoreDetailDTO;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface SupplierStoreDetailConverter extends BaseConverter<SupplierStoreDetailDTO, SupplierStore> {
}
