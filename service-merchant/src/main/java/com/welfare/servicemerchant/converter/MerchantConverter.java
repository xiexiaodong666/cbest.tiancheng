package com.welfare.servicemerchant.converter;

import com.welfare.persist.entity.Merchant;
import com.welfare.service.converter.BaseConverter;
import com.welfare.servicemerchant.dto.MerchantInfo;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface MerchantConverter extends BaseConverter<MerchantInfo,Merchant> {
}
