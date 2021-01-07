package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.servicemerchant.dto.AccountInfo;
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
 * @author duanhy
 * @version 1.0.0
 * @description
 * @date 2021/1/6  5:16 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Api(tags = "员工账号管理")
public class AccountController implements IController {


  @GetMapping("/incremental-page")
  @ApiOperation("增量查询账户(支持离线消费场景)")
  public R<Page<AccountInfo>> pageQuery(@RequestParam @ApiParam("当前页") Integer currentPage,
                                        @RequestParam @ApiParam("单页大小") Integer pageSize,
                                        @RequestParam(required = true) @ApiParam("门店编码") String storeCode,
                                        @RequestParam(required = true) @ApiParam("查询开始的id > 0)") Long startId){
    return null;
  }
}