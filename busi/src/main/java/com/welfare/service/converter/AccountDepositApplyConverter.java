package com.welfare.service.converter;

import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.persist.entity.AccountDepositApplyDetail;
import com.welfare.service.dto.accountapply.AccountDepositApplyExcelInfo;
import com.welfare.service.dto.accountapply.AccountDepositApplyInfo;
import com.welfare.service.dto.accountapply.BatchDepositApplyRequest;
import com.welfare.service.dto.accountapply.DepositApplyRequest;
import org.mapstruct.Mapper;
import org.skife.jdbi.v2.Batch;

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

  AccountDepositApplyExcelInfo toInfoExcel(AccountDepositApply apply);

  List<AccountDepositApplyExcelInfo> toInfoExcelList(List<AccountDepositApply> applys);
}