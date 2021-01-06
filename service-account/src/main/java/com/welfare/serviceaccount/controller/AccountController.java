package com.welfare.serviceaccount.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.Account;
import com.welfare.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账户信息服务控制器
 *
 * @author Yuxiang Li
 * @since 2021-01-06 11:08:59
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Api(tags = "员工账号管理")
public class AccountController implements IController {
    private final AccountService accountService;

    @GetMapping("/page")
    @ApiOperation("分页查询账户")
    public R<Page<Account>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
                                      @RequestParam @ApiParam("单页大小") Integer pageSize,
                                      @RequestParam(required = false) @ApiParam("员工姓名") String accountName,
                                      @RequestParam(required = false) @ApiParam("商户编码") String merCode,
                                      @RequestParam(required = false) @ApiParam("账号状态") Integer accountStatus,
                                      @RequestParam(required = false) @ApiParam("删除标记") Boolean flag){
        return null;
    }
}