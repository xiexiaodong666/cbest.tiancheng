package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.persist.entity.CardApply;
import com.welfare.service.CardApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/8 6:33 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/passport")
@Api(tags = "超级管理员接口")
public class AdminPassportController implements IController {

  @GetMapping("/list")
  @ApiOperation("分页查询管理员账户列表")
  public R<Page<?>> apiPageQuery() {

    return null;
  }

  @GetMapping("/detail")
  @ApiOperation("管理员账户详情")
  public R<CardApply> detail() {

    return null;
  }

  @PostMapping
  @ApiOperation("新增管理员账户")
  public R<Boolean> add() {

    return null;
  }

  @PutMapping
  @ApiOperation("修改管理员账户")
  public R<Boolean> update() {

    return null;
  }


  @PutMapping("/updateStatus")
  @ApiOperation("锁定/解除 管理员账户")
  public R<Boolean> updateStatus() {

    return null;
  }

}
