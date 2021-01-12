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
}
