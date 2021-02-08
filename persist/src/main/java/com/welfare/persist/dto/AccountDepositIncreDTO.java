package com.welfare.persist.dto;

import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author duanhy
 * @title: AccountDepositIncreDTO
 * @description: TODO
 * @date 2021/2/613:19
 */
@Data
public class AccountDepositIncreDTO {

    private Long accountCode;

    private BigDecimal amount;

    private Integer version;

    private String merAccountTypeCode;

    private Long changeEventId;

    public static List<AccountDepositIncreDTO> of(List<Account> accounts, Map<Long, BigDecimal> amountMap,
                                                  Map<Long, Long> changeEventIdMap) {
        List<AccountDepositIncreDTO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accounts)) {
            accounts.forEach(account -> {
                AccountDepositIncreDTO accountDepositIncreDTO = new AccountDepositIncreDTO();
                accountDepositIncreDTO.setAccountCode(account.getAccountCode());
                accountDepositIncreDTO.setAmount(amountMap.get(account.getAccountCode()));
                accountDepositIncreDTO.setVersion(account.getVersion());
                accountDepositIncreDTO.setChangeEventId(changeEventIdMap.get(account.getAccountCode()));
                list.add(accountDepositIncreDTO);
            });
        }
        return list;
    }

    public static List<AccountDepositIncreDTO> of(List<AccountAmountType> accountAmountTypes, Map<Long, BigDecimal> amountMap) {
        List<AccountDepositIncreDTO> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountAmountTypes)) {
            accountAmountTypes.forEach(accountAmountType -> {
                AccountDepositIncreDTO accountDepositIncreDTO = new AccountDepositIncreDTO();
                accountDepositIncreDTO.setAccountCode(accountAmountType.getAccountCode());
                accountDepositIncreDTO.setAmount(amountMap.get(accountAmountType.getAccountCode()));
                accountDepositIncreDTO.setVersion(accountAmountType.getVersion());
                accountDepositIncreDTO.setMerAccountTypeCode(accountAmountType.getMerAccountTypeCode());
                list.add(accountDepositIncreDTO);
            });
        }
        return list;
    }
}
