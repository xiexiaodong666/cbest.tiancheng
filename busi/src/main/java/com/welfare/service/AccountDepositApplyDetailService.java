package com.welfare.service;


import com.welfare.persist.entity.AccountDepositApplyDetail;

import java.util.List;

/**
 * 充值申请明细服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountDepositApplyDetailService {


  List<AccountDepositApplyDetail> listByApplyCode(String applyCode);

}
