package com.welfare.service.converter;

import com.welfare.persist.dto.DepartmentUnionMerchant;
import com.welfare.service.dto.DepartmentTree;
import org.mapstruct.Mapper;


/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  10:59 AM
 */
@Mapper(componentModel = "spring")
public interface DepartmentTreeConverter extends BaseConverter<DepartmentTree, DepartmentUnionMerchant> {


}