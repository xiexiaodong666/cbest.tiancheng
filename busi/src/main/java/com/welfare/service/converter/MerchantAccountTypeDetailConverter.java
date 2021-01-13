package com.welfare.service.converter;

import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.service.dto.MerchantAccountTypeDetailDTO;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface MerchantAccountTypeDetailConverter extends BaseConverter<MerchantAccountTypeDetailDTO, MerchantAccountType> {
}
