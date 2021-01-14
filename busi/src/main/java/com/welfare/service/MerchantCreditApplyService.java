package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.domain.UserInfo;
import com.welfare.persist.dto.query.MerchantCreditApplyQueryReq;
import com.welfare.persist.entity.MerchantCreditApply;
import com.welfare.service.dto.merchantapply.*;

import java.util.List;

/**
 * 商户金额申请服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface MerchantCreditApplyService {

  /**
   * 新增员工额度申请
   * @param apply
   * @return
   */
  String save(MerchantCreditApplyRequest apply, UserInfo user);

  /**
   * 通过requestId获取申请
   * @param requestId
   * @return
   */
  MerchantCreditApply getByRequestId(String requestId);

  /**
   * 修改员工额度申请
   * @param apply
   * @return
   */
  String update(MerchantCreditApplyUpdateReq apply, UserInfo user);

  /**
   * 审批
   * @param apply
   * @return
   */
  String approval(MerchantCreditApprovalReq apply, UserInfo user);

  /**
   * 通过id查询详情
   * @param id
   * @return
   */
  MerchantCreditApplyInfo detail(Long id);

  /**
   *
   * 分页查询申请列表
   * @param current
   * @param size
   * @param query
   * @return
   */
  Page<MerchantCreditApplyInfo> page(Integer current, Integer size, MerchantCreditApplyQueryReq query, UserInfo user);

  /**
   * 通过条件查询申请列表（不分页）
   * @param query
   * @return
   */
  List<MerchantCreditApplyExcelInfo> list(MerchantCreditApplyQueryReq query, UserInfo user);
}
