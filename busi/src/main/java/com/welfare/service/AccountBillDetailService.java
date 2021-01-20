package com.welfare.service;


import com.welfare.persist.dto.AccountBillDetailSimpleDTO;
import com.welfare.persist.dto.query.AccountBillDetailSimpleReq;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.service.dto.Deposit;

import java.util.List;

/**
 * 用户流水明细服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountBillDetailService {
    /**
     * 保存新的AccountBillDetail
     * @param deposit
     * @param accountAmountType
     */
    void saveNewAccountBillDetail(Deposit deposit, AccountAmountType accountAmountType, Account account);

    /**
     * 根据流水号查询
     * @param transNo
     * @return
     */
    AccountBillDetail queryByTransNo(String transNo);

    List<AccountBillDetailSimpleDTO> queryAccountBillDetailSimpleList(
        AccountBillDetailSimpleReq accountBillDetailSimpleReq);
}
