package com.welfare.serviceaccount.controller;

import com.welfare.service.dto.account.AccountAmountTypeGroupDO;
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

    @GetMapping("/by-account-code")
    public R<AccountAmountTypeGroupDO> queryByAccountCode(Long AccountCode){

        return success();
    }
}
