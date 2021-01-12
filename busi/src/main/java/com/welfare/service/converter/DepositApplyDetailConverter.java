package com.welfare.service.converter;

import com.welfare.persist.entity.AccountDepositApplyDetail;
import com.welfare.service.dto.accountapply.AccountDepositApplyDetailInfo;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/9  10:59 AM
 */
@Mapper(componentModel = "spring")
public interface DepositApplyDetailConverter {

  AccountDepositApplyDetailInfo toInfo(AccountDepositApplyDetail detail);

  List<AccountDepositApplyDetailInfo> toInfoList(List<AccountDepositApplyDetail> details);

}