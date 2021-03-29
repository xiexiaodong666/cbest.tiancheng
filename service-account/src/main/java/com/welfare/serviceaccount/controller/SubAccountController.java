package com.welfare.serviceaccount.controller;

import com.welfare.service.SubAccountService;
import com.welfare.serviceaccount.controller.dto.SubAccountDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@Api(tags = "员工子账户管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sub-account")
public class SubAccountController implements IController {
    private final SubAccountService subAccountService;

    @PostMapping("/password-free-signature")
    @ApiOperation("维护免密支付签名")
    public R<SubAccountDTO> passwordFreeSignature(@RequestBody SubAccountDTO subAccountDTO){
        subAccountService.passwordFreeSignature(
                subAccountDTO.getAccountCode(),
                subAccountDTO.getPaymentChannel(),
                subAccountDTO.getPasswordFreeSignature()
        );
        return success(subAccountDTO);
    }
}
