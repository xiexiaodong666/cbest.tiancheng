package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.entity.MerchantStoreRelation;
import com.welfare.service.MerchantStoreRelationService;
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
 * 商户消费场景配置服务控制器
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/merchantStoreRelation")
@Api(tags = "商户消费场景配置接口")
public class MerchantStoreRelationController implements IController {

  private final MerchantStoreRelationService merchantStoreRelationService;

  @GetMapping("/store")
  @ApiOperation("分页查询消费门店配置列表")
  public R<Page<MerchantStoreRelation>> pageQuery(
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = true) @ApiParam("商户代码") String merCode) {

    Page<MerchantStoreRelation> page = new Page(currentPage, pageSize);

    QueryWrapper<MerchantStoreRelation> queryWrapper = new QueryWrapper<>();

    queryWrapper.eq(MerchantStoreRelation.MER_CODE, merCode);
    queryWrapper.ne(MerchantStoreRelation.FLAG, 1);

    Page<MerchantStoreRelation> accountPage = merchantStoreRelationService.pageQuery(
        page, queryWrapper);
    return success(accountPage);

  }
}