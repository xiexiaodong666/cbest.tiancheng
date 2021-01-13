package com.welfare.service.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountTypeDTO;
import com.welfare.persist.dto.query.AccountTypeReq;
import com.welfare.persist.entity.AccountType;
import org.mapstruct.Mapper;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/13 9:28
 */
@Mapper(componentModel = "spring")
public interface AccountTypeConverter {
  Page<AccountTypeDTO> toPage(IPage<AccountTypeDTO> entityList);
  AccountType toEntity(AccountTypeReq accountTypeReq);
}
