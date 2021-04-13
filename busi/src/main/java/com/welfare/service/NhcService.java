package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.nhc.*;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 9:43 上午
 */
public interface NhcService {

  /**
   * 新增或修改用户
   * @param userReq
   * @return
   */
  String saveOrUpdateUser(NhcUserReq userReq);

  /**
   * 查询用户信息
   * @param queryUserReq
   * @return
   */
  NhcUserInfoDTO getUserInfo(NhcQueryUserReq queryUserReq);

  /**
   * 用户积分充值
   * @param pointRechargeReq
   * @return
   */
  Boolean rechargeMallPoint(NhcUserPointRechargeReq pointRechargeReq);

  /**
   * 查询用户账户记录
   * @param userPageReq
   * @return
   */
  Page<NhcAccountBillDetailDTO> getUserBillPage(NhcUserPageReq userPageReq);

  /**
   * 离开家庭
   * @param userCode
   * @return
   */
  Boolean leaveFamily(String userCode);

  /**
   * 新增或修改员工
   * @param nhcAccountReq
   * @return
   */
  String saveOrUpdateAccount(NhcAccountReq nhcAccountReq);

  /**
   * 查询员工信息
   * @param accountCode
   * @return
   */
  NhcAccountInfoDTO getAccountInfo(String accountCode);

  /**
   * 查询员工账户记录
   * @param nhcUserPageReq
   * @return
   */
  Page<NhcAccountBillDetailDTO> getAccountBillPage(NhcUserPageReq nhcUserPageReq);

  /**
   * 查询家庭信息
   * @param userCode
   * @return
   */
  NhcFamilyMemberDTO getFamilyInfo(String userCode);
}
