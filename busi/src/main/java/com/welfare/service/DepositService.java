package com.welfare.service;

import com.welfare.service.dto.Deposit;

import java.util.List;

/**
 * Description: 充值相关服务
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/9/2021
 */
public interface DepositService {
    /**
     * 充值
     * @param deposit
     */
    void deposit(Deposit deposit);

    /**
     * 个人第三方充值
     * @param deposit
     */
    void personalDeposit(Deposit deposit);

    /**
     * 充值 批量
     * @param deposits
     */
    void deposit(List<Deposit> deposits);

    /**
     * 根据交易号查询充值状态
     * @param transNo
     * @return
     */
    Deposit getByTransNo(String transNo);

    /**
     * 充值 批量
     * @param deposits
     */
    void batchDeposit(List<Deposit> deposits);

    /**
     * 充值 批量(如果员工属于某个组，则金额加到该组上)
     * @param deposits
     */
    void batchDepositToGroup(List<Deposit> deposits);
}
