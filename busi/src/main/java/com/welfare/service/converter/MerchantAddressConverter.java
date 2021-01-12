package com.welfare.service.converter;

import com.welfare.persist.entity.MerchantAddress;
import com.welfare.service.dto.MerchantAddressDTO;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface MerchantAddressConverter extends BaseConverter<MerchantAddressDTO, MerchantAddress> {
}
