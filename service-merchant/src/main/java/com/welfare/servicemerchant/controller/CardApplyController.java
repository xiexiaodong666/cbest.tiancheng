package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.annotation.MerchantUser;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.CardApplyMediumEnum;
import com.welfare.common.enums.CardApplyTypeEnum;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.CardApplyDTO;
import com.welfare.persist.dto.query.CardApplyAddReq;
import com.welfare.persist.dto.query.CardApplyUpdateReq;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.CardApplyService;
import com.welfare.service.MerchantService;
import com.welfare.servicemerchant.converter.CardApplyConverter;
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
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
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
  private final CardApplyConverter cardApplyConverter;
  private final CardInfoDao cardInfoDao;

  @GetMapping("/list")
  @ApiOperation("分页查询卡片列表")
  public R<Page<CardApplyDTO>> apiPageQuery(
      @RequestParam @ApiParam("当前页") Integer current,
      @RequestParam @ApiParam("单页大小") Integer size,
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片介质") Integer status,
      @RequestParam(required = false) @ApiParam("使用状态") Date startTime,
      @RequestParam(required = false) @ApiParam("使用状态") Date endTime) {

    Page<CardApply> page = new Page<>(current, size);

    return success(cardApplyService.pageQuery(page, cardName, merCode, cardType, cardMedium,
                                              status, startTime, endTime
    ));
  }

  @GetMapping("/detail")
  @ApiOperation("查看申请卡片详情")
  public R<CardApplyDTO> detail(@RequestParam(required = true) @ApiParam("申请卡片id") Long id) {
    QueryWrapper<CardApply> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(CardApply.ID, id);
    CardApply cardApply = cardApplyService.getCardApplyById(queryWrapper);
    QueryWrapper<Merchant> queryWrapperM = new QueryWrapper<>();

    queryWrapperM.eq(Merchant.MER_CODE, cardApply.getMerCode());

    Merchant merchant = merchantService.getMerchantByMerCode(queryWrapperM);
    CardApplyDTO cardApplyDTO = cardApplyConverter.toE(cardApply);

    if (merchant != null) {
      cardApplyDTO.setMerName(merchant.getMerName());
    }
    QueryWrapper<CardInfo> queryWrapperCardInfo = new QueryWrapper<>();
    queryWrapperCardInfo.eq(CardInfo.APPLY_CODE, cardApply.getApplyCode());
    queryWrapperCardInfo.ne(CardInfo.CARD_STATUS, WelfareConstant.CardStatus.NEW.code());
    List<CardInfo> cardInfoList = cardInfoDao.list(queryWrapperCardInfo);
    if (CollectionUtils.isEmpty(cardInfoList)) {
      cardApplyDTO.setCanChange(Boolean.TRUE);
    }

    return success(cardApplyDTO);
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
      @RequestParam(required = false) @ApiParam("1 删除") Integer delete,
      @RequestParam(required = false) @ApiParam("状态 1 启用  0禁用") Integer status) {

    return success(cardApplyService.updateStatus(id, delete, status));
  }


  @GetMapping("/export")
  @ApiOperation("导出卡片列表(返回文件下载地址)")
  public R<String> export(
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片介质") Integer status,
      @RequestParam(required = false) @ApiParam("使用状态") Date startTime,
      @RequestParam(required = false) @ApiParam("使用状态") Date endTime) throws IOException {

    List<CardApplyDTO> exportList = cardApplyService.exportCardApplys(cardName, merCode, cardType,
                                                                      cardMedium,
                                                                      status, startTime, endTime
    );
    if(CollectionUtils.isNotEmpty(exportList)) {
      for (CardApplyDTO cardApplyDTO:
      exportList) {
        if(Strings.isNotEmpty(cardApplyDTO.getCardMedium())) {
          cardApplyDTO.setCardMedium(CardApplyMediumEnum.valueOf(cardApplyDTO.getCardMedium()).getDesc());
        }

        if(Strings.isNotEmpty(cardApplyDTO.getCardType())) {
          cardApplyDTO.setCardType(CardApplyTypeEnum.valueOf(cardApplyDTO.getCardType()).getDesc());
        }

      }
    }
    String path = fileUploadService.uploadExcelFile(exportList, CardApplyDTO.class, "申请卡片列表");
    return success(fileUploadService.getFileServerUrl(path));
  }

}
