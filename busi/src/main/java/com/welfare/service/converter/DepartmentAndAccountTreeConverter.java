package com.welfare.service.converter;

import com.welfare.persist.dto.DepartmentAndAccountTreeDTO;
import com.welfare.service.dto.DepartmentAndAccountTreeResp;
import org.mapstruct.Mapper;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/5 3:57 下午
 */
@Mapper(componentModel = "spring")
public interface DepartmentAndAccountTreeConverter extends BaseConverter<DepartmentAndAccountTreeDTO, DepartmentAndAccountTreeResp>{

}
