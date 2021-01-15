package com.welfare.servicemerchant.converter;

import com.welfare.persist.dto.CardApplyDTO;
import com.welfare.persist.entity.CardApply;
import com.welfare.service.converter.BaseConverter;
import org.mapstruct.Mapper;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/15 4:39 PM
 */
@Mapper(componentModel = "spring")
public interface CardApplyConverter extends
    BaseConverter<CardApply, CardApplyDTO> {

}
