package com.welfare.service.converter;

import com.welfare.persist.entity.Merchant;
import com.welfare.service.dto.MerchantDetailDTO;
import org.mapstruct.Mapper;


/**
 * @author hao.yin
 * @version 0.0.1
 * @date 2021/1/9 15:48
 */
@Mapper(componentModel = "spring")
public interface MerchantDetailConverter extends BaseConverter<MerchantDetailDTO, Merchant>{

}
