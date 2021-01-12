package com.welfare.serviceaccount.controller;

import com.welfare.service.DepositService;
import com.welfare.service.dto.Deposit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/deposit")
@Api(tags = "充值相关接口")
public class DepositController implements IController {

    private final DepositService depositService;

    @PostMapping
    @ApiOperation("发起充值")
    public R<Deposit> newDeposit(@RequestBody Deposit deposit){
        depositService.deposit(deposit);
        return success(deposit);
    }

    @PostMapping("/batch")
    @ApiOperation("批量充值")
    public R<List<Deposit>> newDepositBatch(@RequestBody List<Deposit> deposits){
        depositService.deposit(deposits);
        return success(deposits);
    }

    @GetMapping("/singleQuery/{transNo}")
    @ApiOperation("根据交易流水号查询充值结果")
    public R<Deposit> getByTransNo(@PathVariable String transNo){
        Deposit deposit = depositService.getByTransNo(transNo);
        return success(deposit);
    }
}
