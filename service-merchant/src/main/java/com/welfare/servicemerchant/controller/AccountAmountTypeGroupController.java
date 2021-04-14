package com.welfare.servicemerchant.controller;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.service.AccountAmountTypeGroupService;
import com.welfare.service.dto.account.AccountAmountTypeGroupDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/14/2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account-amount-type-group")
public class AccountAmountTypeGroupController implements IController {

    private final AccountAmountTypeGroupService accountAmountTypeGroupService;

    @GetMapping("/dto")
    @ApiOperation("根据账号查询组信息")
    public R<AccountAmountTypeGroupDTO> queryByAccountCode(@RequestParam Long accountCode){
        AccountAmountTypeGroupDTO accountAmountTypeGroupDTO = accountAmountTypeGroupService.queryDO(accountCode);
        return success(accountAmountTypeGroupDTO);
    }

    @GetMapping("/count")
    @ApiOperation("根据商户号统计分组数")
    public R<Long> countGroups(@RequestParam String merCode){
        Long count = accountAmountTypeGroupService.countGroups(merCode, WelfareConstant.MerAccountTypeCode.MALL_POINT.code());
        return success(count);
    }
}
