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
 * @date 2021/1/8 6:14 PM
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/cardApply")
@Api(tags = "卡片信息接口")
public class CardApplyController implements IController {

  private final CardApplyService cardApplyService;

  @GetMapping("/list")
  @ApiOperation("分页查询卡片列表")
  public R<Page<CardApply>> apiPageQuery() {

    return null;
  }

  @GetMapping("/detail")
  @ApiOperation("api分页查询消费门店配置列表")
  public R<CardApply> detail() {

    return null;
  }

  @PostMapping
  @ApiOperation("新增卡片")
  public R<Boolean> add() {

    return null;
  }

  @PutMapping
  @ApiOperation("修改卡片")
  public R<Boolean> update() {

    return null;
  }

  @PostMapping("/export")
  @ApiOperation("导出卡片列表(返回文件下载地址)")
  @MerchantUser
  public R<String> export() {
    return null;
  }

}
