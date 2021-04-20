package com.welfare.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.service.dto.nhc.*;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/13 9:43 上午
 */
public interface NhcService {
  String DEFAULT_PHONE_PREFIX = "M";

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
  void rechargeMallPoint(NhcUserPointRechargeReq pointRechargeReq);

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
  Boolean leaveFamily(String merCode, String userCode);

  /**
   * 查询家庭信息
   * @param userCode
   * @return
   */
  NhcFamilyMemberDTO getFamilyInfo(String userCode);
}
