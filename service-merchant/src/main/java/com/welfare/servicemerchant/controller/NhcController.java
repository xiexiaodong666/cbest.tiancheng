package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.service.dto.nhc.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:42 上午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/nhc")
@Api(tags = "卫计委相关接口")
public class NhcController {

  @PostMapping("/user/saveOrUpdate")
  @ApiOperation("新增或修改用户")
  @MerchantUser
  public R<String> saveOrUpdateUser(@Validated @RequestBody NhcUserReq userReq) {
    return null;
  }

  @GetMapping("/user/info/{userCode}")
  @ApiOperation("查询用户信息")
  @MerchantUser
  public R<NhcUserInfoDTO> getUserInfo(@PathVariable("userCode") String userCode) {
    return null;
  }

  @PostMapping("/user/bill/{userCode}")
  @ApiOperation("查询用户账户记录")
  @MerchantUser
  public R<Page<NhcAccountBillDetailDTO>> userBillPage(@Validated @RequestBody NhcUserPageReq userPageReq) {
    return null;
  }

  @PostMapping("/user/family/leave/{userCode}")
  @ApiOperation("从家庭中删除用户")
  @MerchantUser
  public R<Boolean> leaveFamily(@PathVariable("userCode") String userCode) {
    return null;
  }

  @PostMapping("/account/saveOrUpdate")
  @ApiOperation("新增或修改员工")
  @MerchantUser
  public R<String> saveOrUpdateAccount(@Validated @RequestBody NhcAccountReq nhcAccountReq) {
    return null;
  }

  @GetMapping("/account/info/{accountCode}")
  @ApiOperation("查询员工信息")
  @MerchantUser
  public R<NhcAccountInfoDTO> getAccountInfo(@PathVariable("accountCode") String accountCode) {
    return null;
  }

  @PostMapping("/account/bill/{accountCode}")
  @ApiOperation("查询员工账户记录")
  @MerchantUser
  public R<Page<NhcAccountBillDetailDTO>> accountBillPage(@Validated @RequestBody NhcUserPageReq nhcUserPageReq) {
    return null;
  }

  @PostMapping("/user/family/info/{userCode}")
  @ApiOperation("查询家庭信息")
  @MerchantUser
  public R<NhcFamilyMemberDTO> familyInfo(@PathVariable("userCode") String userCode) {
    return null;
  }
}
