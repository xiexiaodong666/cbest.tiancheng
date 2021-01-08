package com.welfare.serviceaccount.controller;

import com.welfare.serviceaccount.domain.Deposit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
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
    @PostMapping
    @ApiOperation("发起充值")
    public R<Deposit> newDeposit(@RequestBody Deposit deposit){
        return null;
    }

    @PostMapping("/batch")
    @ApiOperation("批量充值")
    public R<List<Deposit>> newDepositBatch(@RequestBody List<Deposit> deposits){
        return null;
    }

    @GetMapping("/singleQuery/{requestId}")
    @ApiOperation("根据requestId查询充值结果")
    public R<Deposit> getByRequestId(@PathVariable String requestId){
        return null;
    }
}
