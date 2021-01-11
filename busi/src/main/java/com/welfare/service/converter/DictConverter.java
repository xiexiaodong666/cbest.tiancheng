package com.welfare.service.converter;

import com.welfare.persist.entity.Dict;
import com.welfare.service.dto.DictDTO;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface DictConverter extends BaseConverter<DictDTO, Dict> {
}
