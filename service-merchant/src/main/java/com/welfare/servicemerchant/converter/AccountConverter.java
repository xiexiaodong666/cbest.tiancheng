package com.welfare.servicemerchant.converter;

import com.welfare.persist.entity.Account;
import com.welfare.servicemerchant.dto.AccountDepositApplyInfo;
import org.mapstruct.Mapper;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/6  5:30 PM
 */
@Mapper(componentModel = "spring")
public interface AccountConverter {

  AccountDepositApplyInfo toInfo(Account account);

}