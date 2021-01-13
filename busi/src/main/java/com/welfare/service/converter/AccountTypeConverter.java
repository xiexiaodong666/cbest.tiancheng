package com.welfare.service.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountTypeMapperDTO;
import com.welfare.persist.dto.query.AccountTypeReq;
import com.welfare.persist.entity.AccountType;
import com.welfare.service.dto.AccountTypeDTO;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 9:28
 */
@Mapper(componentModel = "spring")
public interface AccountTypeConverter {
  Page<AccountTypeDTO> toPage(IPage<AccountTypeDTO> entityList);
  Page<AccountTypeDTO> toDTOPage(IPage<AccountTypeMapperDTO> entityList);
  List<AccountTypeDTO> toDTOList(List<AccountTypeMapperDTO> accountTypeMapperDTOList);
  AccountType toEntity(AccountTypeReq accountTypeReq);
}
