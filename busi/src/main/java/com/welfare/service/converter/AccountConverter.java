package com.welfare.service.converter;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountBillDetailMapperDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.dto.AccountBillDetailDTO;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountDepositApplyInfo;
import org.mapstruct.Mapper;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/9 15:48
 */
@Mapper(componentModel = "spring")
public interface AccountConverter {

  Page<AccountDTO> toPage(IPage<AccountPageDTO> entityList);

  Page<AccountBillDetailDTO> toBillDetailPage(IPage<AccountBillDetailMapperDTO> entityList);

  AccountDepositApplyInfo toInfo(Account account);
}
