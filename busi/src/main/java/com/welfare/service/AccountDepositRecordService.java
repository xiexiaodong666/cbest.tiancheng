package com.welfare.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.welfare.persist.entity.AccountDepositRecord;
import com.welfare.service.dto.AccountDepositDTO;
import com.welfare.service.dto.AccountDepositReq;
import com.welfare.service.dto.AccountPayResultQueryDTO;
import com.welfare.service.dto.AccountPayResultQueryReq;
import com.welfare.service.remote.entity.CbestPayBaseResp;

/**
 * 账号充值记录表服务接口
 *
 * @author kancy
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-11 09:20:53
 */
public interface AccountDepositRecordService extends IService<AccountDepositRecord> {

    AccountDepositDTO getPayInfo(AccountDepositReq req);

    AccountPayResultQueryDTO queryPayResult(AccountPayResultQueryReq req);

    void payNotify(CbestPayBaseResp resp);

    void execPendingPaymentList();

    void execPendingAndFailureRechargeList();

}
