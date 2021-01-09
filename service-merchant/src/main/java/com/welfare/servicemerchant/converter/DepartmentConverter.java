package com.welfare.servicemerchant.converter;

import com.welfare.persist.entity.Department;
import com.welfare.service.converter.BaseConverter;
import com.welfare.servicemerchant.dto.DepartmentInfo;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface DepartmentConverter extends BaseConverter<DepartmentInfo,Department> {
}
