package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.CardApplyMediumEnum;
import com.welfare.common.enums.CardApplyTypeEnum;
import com.welfare.persist.dto.CardInfoApiDTO;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.CardApplyService;
import com.welfare.service.CardInfoService;
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
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/12/2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/card-info")
@Api(tags = "制卡相关接口")
public class CardController implements IController {

  private final CardInfoService cardInfoService;

  private final MerchantService merchantService;

  private final CardApplyService applyService;

  private final FileUploadService fileUploadService;

  @GetMapping("/{cardNo}")
  @ApiOperation("根据卡号获取卡信息")
  public R<CardInfoApiDTO> queryCardInfo(
      @PathVariable(value = "cardNo") @ApiParam("卡号") String cardNo) {
    CardInfo cardInfo = cardInfoService.getByCardNo(cardNo);
    CardApply cardApply = applyService.queryByApplyCode(cardInfo.getApplyCode());
    Merchant merchant = merchantService.queryByCode(cardApply.getMerCode());
    CardInfoApiDTO cardInfoApiDTO = tranferToCardInfoApiDTO(cardInfo, cardApply);

    cardInfoApiDTO.setMerName(merchant.getMerName());
    return success(cardInfoApiDTO);
  }

  @GetMapping("/byMagneticStripe")
  @ApiOperation("根据卡号获取卡信息")
  public R<CardInfoApiDTO> queryCardInfoByMagneticStripe(
      @RequestParam("magneticStripe") @ApiParam("磁条号") String magneticStripe) {
    CardInfo cardInfo = cardInfoService.getByMagneticStripe(magneticStripe);
    CardApply cardApply = applyService.queryByApplyCode(cardInfo.getApplyCode());
    Merchant merchant = merchantService.queryByCode(cardApply.getMerCode());
    CardInfoApiDTO cardInfoApiDTO = tranferToCardInfoApiDTO(cardInfo, cardApply);

    cardInfoApiDTO.setMerName(merchant.getMerName());
    return success(cardInfoApiDTO);
  }

  @GetMapping("/apply/{applyCode}")
  @ApiOperation("根据批次号获取卡信息")
  public R<List<CardInfoApiDTO>> queryCardInfoByBatchNo(
      @PathVariable(value = "applyCode") @ApiParam("制卡申请号（批次号）") String applyCode) {
    List<CardInfoApiDTO> cardInfos = cardInfoService.listByApplyCode(
        applyCode, WelfareConstant.CardStatus.NEW.code());
    return success(cardInfos);
  }

  @PutMapping("/written")
  public R<CardInfoApiDTO> updateToWritten(@RequestBody CardInfo cardInfo) {
    CardInfo result = cardInfoService.updateWritten(cardInfo);
    CardApply cardApply = applyService.queryByApplyCode(result.getApplyCode());
    Merchant merchant = merchantService.queryByCode(cardApply.getMerCode());
    CardInfoApiDTO cardInfoApiDTO = tranferToCardInfoApiDTO(result, cardApply);
    cardInfoApiDTO.setMerName(merchant.getMerName());

    return success(cardInfoApiDTO);
  }

  @GetMapping("/admin/list")
  @ApiOperation("根据卡号获取卡信息")
  public R<Page<CardInfoDTO>> queryCardInfo(
      @RequestParam @ApiParam("当前页") Integer current,
      @RequestParam @ApiParam("单页大小") Integer size,
      @RequestParam(required = false) @ApiParam("申请卡片管理传applyCode") String applyCode,
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片状态") Integer cardStatus,
      @RequestParam(required = false) @ApiParam("入库查询开始时间") Date writtenStartTime,
      @RequestParam(required = false) @ApiParam("入库查询结束时间") Date writtenEndTime,
      @RequestParam(required = false) @ApiParam("申请开始时间") Date startTime,
      @RequestParam(required = false) @ApiParam("申请结束时间") Date endTime,
      @RequestParam(required = false) @ApiParam("绑定查询开始时间") Date bindStartTime,
      @RequestParam(required = false) @ApiParam("绑定查询结束时间") Date bindEndTime) {

    return success(cardInfoService.list(current, size, applyCode, cardName,
                                        merCode, cardType, cardMedium,
                                        cardStatus, writtenStartTime,
                                        writtenEndTime, startTime,
                                        endTime, bindStartTime, bindEndTime
    ));

  }

  @GetMapping("/export")
  @ApiOperation("导出卡片信息")
  public R<String> exportCardInfo(
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片状态") Integer cardStatus,
      @RequestParam(required = false) @ApiParam("入库查询开始时间") Date writtenStartTime,
      @RequestParam(required = false) @ApiParam("入库查询结束时间") Date writtenEndTime,
      @RequestParam(required = false) @ApiParam("申请开始时间") Date startTime,
      @RequestParam(required = false) @ApiParam("申请结束时间") Date endTime,
      @RequestParam(required = false) @ApiParam("绑定查询开始时间") Date bindStartTime,
      @RequestParam(required = false) @ApiParam("绑定查询结束时间") Date bindEndTime) throws IOException {

    List<CardInfoDTO> exportList = cardInfoService.exportCardInfo(cardName,
                                                                  merCode, cardType, cardMedium,
                                                                  cardStatus, writtenStartTime,
                                                                  writtenEndTime, startTime,
                                                                  endTime, bindStartTime,
                                                                  bindEndTime
    );
    for (CardInfoDTO cardInfoDTO:
    exportList) {
      if(Strings.isNotEmpty(cardInfoDTO.getCardMedium())) {
        cardInfoDTO.setCardMedium(CardApplyMediumEnum.valueOf(cardInfoDTO.getCardMedium()).getDesc());
      }

      if(Strings.isNotEmpty(cardInfoDTO.getCardType())) {
        cardInfoDTO.setCardType(CardApplyTypeEnum.valueOf(cardInfoDTO.getCardType()).getDesc());
      }
    }


    String path = fileUploadService.uploadExcelFile(exportList, CardInfoDTO.class, "卡片信息");
    // wait gc
    exportList.clear();
    return success(fileUploadService.getFileServerUrl(path));

  }

  @GetMapping("/admin/{cardNo}")
  @ApiOperation("后台根据卡号获取卡信息")
  public R<CardInfoDTO> queryCardInfoAdmin(
      @PathVariable(value = "cardNo") @ApiParam("卡号") String cardNo) {
    CardInfo cardInfo = cardInfoService.getByCardNo(cardNo);

    CardApply cardApply = applyService.queryByApplyCode(cardInfo.getApplyCode());

    QueryWrapper<Merchant> queryWrapperM = new QueryWrapper<>();

    queryWrapperM.eq(Merchant.MER_CODE, cardApply.getMerCode());

    Merchant merchant = merchantService.getMerchantByMerCode(queryWrapperM);

    CardInfoDTO cardInfoDTO = new CardInfoDTO();
    cardInfoDTO.setCardId(cardInfo.getCardId());
    cardInfoDTO.setCardName(cardApply.getCardName());
    cardInfoDTO.setCardType(cardApply.getCardType());
    cardInfoDTO.setCardMedium(cardApply.getCardMedium());
    cardInfoDTO.setCardStatus(cardInfo.getCardStatus());
    if (merchant != null) {
      cardInfoDTO.setMerName(merchant.getMerName());
    }
    cardInfoDTO.setCreateTime(cardInfo.getCreateTime());
    cardInfoDTO.setWrittenTime(cardInfo.getWrittenTime());
    cardInfoDTO.setBindTime(cardInfo.getBindTime());
    cardInfoDTO.setAccountCode(cardInfo.getAccountCode());

    return success(cardInfoDTO);
  }

  private CardInfoApiDTO tranferToCardInfoApiDTO(CardInfo cardInfo, CardApply cardApply) {
    CardInfoApiDTO cardInfoApiDTO = new CardInfoApiDTO();

    cardInfoApiDTO.setId(cardInfo.getId());
    cardInfoApiDTO.setApplyCode(cardInfo.getApplyCode());
    cardInfoApiDTO.setCardId(cardInfo.getCardId());
    cardInfoApiDTO.setCardName(cardApply.getCardName());
    cardInfoApiDTO.setCardType(cardApply.getCardType());
    cardInfoApiDTO.setCardStatus(cardInfo.getCardStatus());
    cardInfoApiDTO.setDeleted(cardInfo.getDeleted());
    cardInfoApiDTO.setCreateUser(cardInfo.getCreateUser());
    cardInfoApiDTO.setCreateTime(cardInfo.getCreateTime());
    cardInfoApiDTO.setUpdateUser(cardInfo.getUpdateUser());
    cardInfoApiDTO.setUpdateTime(cardInfo.getUpdateTime());
    cardInfoApiDTO.setAccountCode(cardInfo.getAccountCode());
    cardInfoApiDTO.setVersion(cardInfo.getVersion());
    cardInfoApiDTO.setMagneticStripe(cardInfo.getMagneticStripe());
    cardInfoApiDTO.setWrittenTime(cardInfo.getWrittenTime());
    cardInfoApiDTO.setBindTime(cardInfo.getBindTime());
    cardInfoApiDTO.setCardMedium(cardApply.getCardMedium());
    cardInfoApiDTO.setMerCode(cardApply.getMerCode());

    return cardInfoApiDTO;
  }

}
