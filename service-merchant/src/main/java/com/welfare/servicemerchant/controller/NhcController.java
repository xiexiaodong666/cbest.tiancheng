package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountAmountTypeGroup;
import com.welfare.service.AccountAmountTypeGroupService;
import com.welfare.service.AccountAmountTypeService;
import com.welfare.service.NhcService;
import com.welfare.service.dto.nhc.*;
import com.welfare.service.remote.entity.EmployerReqDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/12 10:42 上午
 */
@Slf4j
@RestController
@RequestMapping("/nhc")
@Api(tags = "卫计委相关接口")
public class NhcController {

    @Autowired
    private NhcService nhcService;
    @Autowired
    private AccountAmountTypeService accountAmountTypeService;
    @Autowired
    private AccountAmountTypeGroupService accountAmountTypeGroupService;

    @PostMapping("/user/saveOrUpdate")
    @ApiOperation("新增或修改用户")
    @MerchantUser
    public R<EmployerReqDTO> saveOrUpdateUser(@Validated @RequestBody NhcUserReq userReq) {
        return R.success(nhcService.saveOrUpdateUser(userReq));
    }

    @PostMapping("/user/info")
    @ApiOperation("查询用户信息")
    @MerchantUser
    public R<NhcUserInfoDTO> getUserInfo(@RequestBody NhcQueryUserReq queryUserReq) {
        return R.success(nhcService.getUserInfo(queryUserReq));
    }

    @PostMapping("/user/mallPoint/recharge")
    @ApiOperation("用户积分充值")
    @MerchantUser
    public R<Boolean> rechargeMallPoint(@Validated @RequestBody NhcUserPointRechargeReq pointRechargeReq) {
        nhcService.rechargeMallPoint(pointRechargeReq);
        return R.success();
    }

    @PostMapping("/user/bill")
    @ApiOperation("查询用户账户记录")
    @MerchantUser
    public R<Page<NhcAccountBillDetailDTO>> userBillPage(@Validated @RequestBody NhcUserPageReq userPageReq) {
        return R.success(nhcService.getUserBillPage(userPageReq));
    }

    @PostMapping("/user/family/leave/{accountCode}")
    @ApiOperation("从家庭中删除用户")
    @MerchantUser
    public R<Boolean> leaveFamily(@PathVariable("accountCode") String accountCode) {
        return R.success(nhcService.leaveFamily(MerchantUserHolder.getMerchantUser().getMerchantCode(), accountCode));
    }

    @PostMapping("/user/family/info/{accountCode}")
    @ApiOperation("查询家庭信息")
    @MerchantUser
    public R<NhcFamilyMemberDTO> familyInfo(@PathVariable("accountCode") String accountCode) {
        return R.success(nhcService.getFamilyInfo(accountCode));
    }

    @PostMapping("/user/mallPoint/list")
    @ApiOperation("批量查询用户积分")
    @MerchantUser
    public R<List<NhcUserMallPointDTO>> getUserMallPointList(@RequestBody NhcBatchQueryUserReq batchQueryUserReq) {
        if (CollectionUtils.isEmpty(batchQueryUserReq.getAccountCodes())) {
            return R.success();
        }
        List<Long> accountCodes = batchQueryUserReq.getAccountCodes().stream().map(Long::valueOf).collect(Collectors.toList());
        List<AccountAmountType> accountAmountTypes = accountAmountTypeService.batchQueryByAccount(accountCodes, WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        List<AccountAmountTypeGroup> groups = accountAmountTypeGroupService.listById(accountAmountTypes.stream()
                .map(AccountAmountType::getAccountAmountTypeGroupId)
                .filter(Objects::nonNull).collect(Collectors.toList()));
        return R.success(NhcUserMallPointDTO.of(groups, accountAmountTypeService.batchQueryByAccount(accountCodes, WelfareConstant.MerAccountTypeCode.MALL_POINT.code())));
    }
}
