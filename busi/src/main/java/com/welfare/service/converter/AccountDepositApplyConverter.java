package com.welfare.service.converter;

import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.persist.entity.AccountDepositApplyDetail;
import com.welfare.service.dto.AccountDepositApplyInfo;
import com.welfare.service.dto.DepositApplyRequest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/8  2:45 PM
 */
@Mapper(componentModel = "spring")
public interface AccountDepositApplyConverter {

  AccountDepositApply toAccountDepositApply(DepositApplyRequest request);

  AccountDepositApplyDetail toAccountDepositApplyDetail(AccountDepositApply apply);

  AccountDepositApplyInfo toInfo(AccountDepositApply apply);

  List<AccountDepositApplyInfo> toInfoList(List<AccountDepositApply> applys);

}