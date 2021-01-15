package com.welfare.service.converter;

import com.welfare.persist.entity.Merchant;
import com.welfare.service.dto.MerchantSyncDTO;
import org.mapstruct.Mapper;


/**
 * @author hao.yin
 * @version 0.0.1
 * @date 2021/1/9 15:48
 */
@Mapper(componentModel = "spring")
public interface MerchantSyncConverter extends BaseConverter<MerchantSyncDTO, Merchant>{

}
