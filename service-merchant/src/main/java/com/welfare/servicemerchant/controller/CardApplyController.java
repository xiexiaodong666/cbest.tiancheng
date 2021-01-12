package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.persist.dto.query.CardApplyAddReq;
import com.welfare.persist.dto.query.CardApplyUpdateReq;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.CardApplyService;
import com.welfare.service.MerchantService;
import com.welfare.servicemerchant.service.FileUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
  private final MerchantService merchantService;
  private final FileUploadService fileUploadService;

  @GetMapping("/list")
  @ApiOperation("分页查询卡片列表")
  public R<Page<CardApply>> apiPageQuery(
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片介质") Integer status,
      @RequestParam(required = false) @ApiParam("使用状态") Date startTime,
      @RequestParam(required = false) @ApiParam("使用状态") Date endTime) {

    Page<CardApply> page = new Page(currentPage, pageSize);

    return success(cardApplyService.pageQuery(page, cardName, merCode, cardType, cardMedium,
                                              status, startTime, endTime
    ));
  }

  @GetMapping("/detail")
  @ApiOperation("api分页查询消费门店配置列表")
  public R<CardApply> detail(@RequestParam(required = true) @ApiParam("消费场景门店id") Long id) {
    QueryWrapper<CardApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(CardApply.ID, id);
    CardApply cardApply = cardApplyService.getMerchantStoreRelationById(queryWrapper);
    QueryWrapper<Merchant> queryWrapperM = new QueryWrapper();

    queryWrapperM.eq(Merchant.MER_CODE, cardApply.getMerCode());

    Merchant merchant = merchantService.getMerchantByMerCode(queryWrapperM);

    cardApply.setMerName(merchant.getMerName());

    return success(cardApply);
  }

  @PostMapping
  @ApiOperation("新增卡片")
  public R<Boolean> add(@RequestBody CardApplyAddReq cardApplyAddReq) {

    return success(cardApplyService.add(cardApplyAddReq));
  }

  @PutMapping
  @ApiOperation("修改卡片")
  public R<Boolean> update(@RequestBody CardApplyUpdateReq cardApplyUpdateReq) {

    return success(cardApplyService.update(cardApplyUpdateReq));
  }

  @PutMapping("/status")
  @ApiOperation("删除/禁用/启动卡片")
  public R<Boolean> updateStatus(
      @RequestParam(required = true) @ApiParam("id") Long id,
      @RequestParam(required = false) @ApiParam("1删除") Integer delete,
      @RequestParam(required = false) @ApiParam("状态 0 正常,1 禁用") Integer status) {

    return success(cardApplyService.updateStatus(id, delete, status));
  }


  @PostMapping("/export")
  @ApiOperation("导出卡片列表(返回文件下载地址)")
  @MerchantUser
  public R<String> export(
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片介质") Integer status,
      @RequestParam(required = false) @ApiParam("使用状态") Date startTime,
      @RequestParam(required = false) @ApiParam("使用状态") Date endTime) throws IOException {

    List<CardApply> exportList = cardApplyService.exportCardApplys(cardName, merCode, cardType,
                                                                   cardMedium,
                                                                   status, startTime, endTime
    );
    String path = fileUploadService.uploadExcelFile(exportList, CardApply.class, "账户明细");
    return success(fileUploadService.getFileServerUrl(path));
  }

}
