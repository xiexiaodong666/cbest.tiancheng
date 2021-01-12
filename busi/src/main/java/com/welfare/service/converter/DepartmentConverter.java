package com.welfare.service.converter;

import com.welfare.persist.entity.Department;
import com.welfare.service.dto.DepartmentDTO;
import org.mapstruct.Mapper;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Mapper(componentModel = "spring")
public interface DepartmentConverter extends BaseConverter<DepartmentDTO,Department> {
}
