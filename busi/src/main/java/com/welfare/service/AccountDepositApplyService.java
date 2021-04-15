package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.persist.entity.AccountDepositApply;
import com.welfare.service.dto.accountapply.*;

import java.math.BigDecimal;
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
     * @param request
     * @param merchantUserInfo
     * @return
     */
    Long saveOne(DepositApplyRequest request, MerchantUserInfo merchantUserInfo);

    /**
     * 批量新增员工账号充值申请
     * @param request 申请基础信息
     * @param fileId fileId
     * @param merchantUserInfo 商户信息
     * @return
     */
    Long saveBatch(DepositApplyRequest request, String fileId, MerchantUserInfo merchantUserInfo);

    /**
     * 修改员工账号充值申请
     * @param request 修改申请基础信息
     * @param merchantUserInfo 商户信息
     * @return
     */
    Long updateOne(DepositApplyUpdateRequest request,MerchantUserInfo merchantUserInfo);

    /**
     * 批量修改员工账号充值申请
     * @param request 修改申请基础信息
     * @param fileId fileId
     * @param merchantUserInfo 商户信息
     * @return
     */
    Long updateBatch(DepositApplyUpdateRequest request, String fileId, MerchantUserInfo merchantUserInfo);

    /**
     * 通过requestId查询申请信息
     * @param requestId
     * @return
     */
    AccountDepositApply getByRequestId(String requestId);

    /**
     * 通过requestId查询申请信息
     * @param requestId
     * @return
     */
    List<AccountDepositApply> listByRequestId(String requestId);

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
    Page<AccountDepositApplyInfo> page(int currentPage, int pageSize, AccountDepositApplyQuery query);

    /**
     * 查询申请(不分页)
     * @param query
     * @return
     */
    List<AccountDepositApplyExcelInfo> list(AccountDepositApplyQuery query);

    /**
     * 查询详情
     * @param id
     * @return
     */
    AccountDepositApplyDetailInfo detail(Long id);

    /**
     * 汇总商户某个福利的充值总额
     * @param merCode 商户编码
     * @param merAccountTypeCode 福利类型编码
     * @return 总充值金额
     */
    BigDecimal sumDepositDetailAmount(String merCode, String merAccountTypeCode);
}
