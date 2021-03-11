package com.welfare.serviceaccount.controller;

import com.welfare.service.AccountService;
import com.welfare.service.dto.AccountRestoreCreditLimitReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/3/10 12:56 下午
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/accountCredit")
@Api(tags = "员工授信额度管理")
public class AccountCreditLimitController implements IController {

  @Autowired
  private AccountService accountService;

  @PostMapping("/batchRestore")
  @ApiOperation("批量充值")
  public R newDepositBatch(@RequestBody @Valid AccountRestoreCreditLimitReq req){
    accountService.batchRestoreCreditLimit(req);
    return success();
  }
}
