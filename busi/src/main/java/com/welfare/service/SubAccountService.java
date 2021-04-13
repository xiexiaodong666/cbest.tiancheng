package com.welfare.service;

import com.welfare.persist.entity.SubAccount;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
public interface SubAccountService {
    /**
     * 免密支付签名设置
     * @param accountCode 账户号
     * @param paymentChannel 支付渠道
     * @param passwordFreeSignature 免密支付签名
     * @return
     */
    SubAccount passwordFreeSignature(Long accountCode,String paymentChannel,String passwordFreeSignature);

    /**
     * 查询subAccount
     * @param accountCode 账户号
     * @param paymentChannel 支付渠道
     * @return 对应子账户
     */
    SubAccount query(Long accountCode, String paymentChannel);
}
