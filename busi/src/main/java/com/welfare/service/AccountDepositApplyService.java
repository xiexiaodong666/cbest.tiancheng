package com.welfare.service;


import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.service.dto.*;
import com.welfare.service.enums.ApprovalType;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestParam;

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
   * 新增员工账号充值申请
   * @param request
   * @param merchantUserInfo
   * @return
   */
  Long saveOne(DepositApplyRequest request, MerchantUserInfo merchantUserInfo);

  /**
   * 新增员工账号充值申请
   * @param request 申请基础信息
   * @param fileId fileId
   * @param merchantUserInfo 商户信息
   * @return
   */
  Long saveBatch(DepositApplyRequest request, String fileId, MerchantUserInfo merchantUserInfo);


  /**
   * 修改员工账号充值申请
   * @param request 修改申请基础信息
   * @param accountAmounts 员工充值金额
   * @param merchantUserInfo 商户信息
   * @return
   */
  Long update(DepositApplyUpdateRequest request, List<AccountDepositRequest> accountAmounts, MerchantUserInfo merchantUserInfo);

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

  /**
   * 分页查询申请
   * @param currentPage
   * @param pageSize
   * @param query
   * @return
   */
  Page<AccountDepositApplyInfo> page(Integer currentPage, Integer pageSize, AccountDepositApplyQuery query);
//
//  /**
//          * 查询详情
//   * @param id
//   * @return
//           */
//  AccountDepositApplyDetailInfo detail(Long id);
}
