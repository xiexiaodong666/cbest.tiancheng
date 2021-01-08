package com.welfare.servicemerchant.converter;

import com.welfare.persist.entity.AccountType;
import com.welfare.servicemerchant.dto.AccountTypeReq;
import org.mapstruct.Mapper;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/8 11:36
 */
@Mapper(componentModel = "spring")
public interface AccountTypeConverter {
  AccountType toEntity(AccountTypeReq accountTypeReq);
}
