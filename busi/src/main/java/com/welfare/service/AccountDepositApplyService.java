package com.welfare.service;


import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.service.dto.AccountDepositApprovalRequest;
import com.welfare.service.dto.AccountDepositRequest;
import com.welfare.service.dto.DepositApplyRequest;
import com.welfare.service.dto.MerchantCreditApprovalReq;
import com.welfare.service.enums.ApprovalType;

import java.util.List;

/**
 * 账户充值申请服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface AccountDepositApplyService {

  /**
   * 新增员工账号充值申请
   * @param request 申请基础信息
   * @param accountAmounts 员工充值金额
   * @param merchantUserInfo 商户信息
   * @param approvalType 批量/单个
   * @return
   */
  Long save(DepositApplyRequest request, List<AccountDepositRequest> accountAmounts,
            MerchantUserInfo merchantUserInfo, ApprovalType approvalType);

  /**
   * 通过requestId查询申请信息
   * @param requestId
   * @return
   */
  AccountDepositApply getByRequestId(String requestId);

  /**
   * 审批
   * @param req
   * @return
   */
  Long approval(AccountDepositApprovalRequest req);
}
