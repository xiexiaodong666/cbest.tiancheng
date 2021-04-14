package com.welfare.servicemerchant.controller;

import com.welfare.service.AccountAmountTypeGroupService;
import com.welfare.service.dto.account.AccountAmountTypeGroupDTO;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public R<AccountAmountTypeGroupDTO> queryByAccountCode(Long accountCode){
        AccountAmountTypeGroupDTO accountAmountTypeGroupDTO = accountAmountTypeGroupService.queryDO(accountCode);
        return success(accountAmountTypeGroupDTO);
    }

    @GetMapping("/count")
    public R<Integer> countGroups(){
        Integer count = accountAmountTypeGroupService.countGroups();
        return success(count);
    }
}
