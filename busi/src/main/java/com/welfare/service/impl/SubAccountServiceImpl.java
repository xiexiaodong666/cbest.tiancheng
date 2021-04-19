package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.welfare.persist.dao.SubAccountDao;
import com.welfare.persist.entity.SubAccount;
import com.welfare.service.SubAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@Service
@RequiredArgsConstructor
public class SubAccountServiceImpl implements SubAccountService {
    private final SubAccountDao subAccountDao;
    @Override
    public SubAccount passwordFreeSignature(Long accountCode, String paymentChannel, String passwordFreeSignature) {
        SubAccount subAccount = subAccountDao.getByAccountCodeAndType(accountCode, paymentChannel);
        subAccount.setPasswordFreeSignature(passwordFreeSignature);
        subAccountDao.updateById(subAccount);
        return subAccount;
    }

    @Override
    public SubAccount query(Long accountCode, String paymentChannel) {
        return subAccountDao.getOne(
                Wrappers.<SubAccount>lambdaQuery()
                        .eq(SubAccount::getAccountCode,accountCode)
                        .eq(SubAccount::getSubAccountType,paymentChannel).last("limit 1")
        );
    }
}
