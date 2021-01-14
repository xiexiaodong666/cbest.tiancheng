package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.CardApplyService;
import com.welfare.service.CardInfoService;
import com.welfare.service.MerchantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
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
  @GetMapping("/{cardNo}")
  @ApiOperation("根据卡号获取卡信息")
  public R<CardInfo> queryCardInfo(@PathVariable(value = "cardNo") @ApiParam("卡号") String cardNo) {
    CardInfo cardInfo = cardInfoService.getByCardNo(cardNo);
    return success(cardInfo);
  }

  @GetMapping("/apply/{applyCode}")
  @ApiOperation("根据批次号获取卡信息")
  public R<List<CardInfo>> queryCardInfoByBatchNo(
      @PathVariable(value = "applyCode") @ApiParam("制卡申请号（批次号）") String applyCode) {
    List<CardInfo> cardInfos = cardInfoService.listByApplyCode(
        applyCode, WelfareConstant.CardStatus.NEW.code());
    return success(cardInfos);
  }

  @PutMapping("/written")
  public R<CardInfo> updateToWritten(@RequestBody CardInfo cardInfo) {
    CardInfo result = cardInfoService.updateWritten(cardInfo);
    return success(result);
  }

  @GetMapping("/admin/list")
  @ApiOperation("根据卡号获取卡信息")
  public R<Page<CardInfoDTO>> queryCardInfo(
      @RequestParam @ApiParam("当前页") Integer currentPage,
      @RequestParam @ApiParam("单页大小") Integer pageSize,
      @RequestParam(required = false) @ApiParam("申请卡片管理传applyCode") String applyCode,
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片状态") String cardStatus,
      @RequestParam(required = false) @ApiParam("入库查询开始时间") Date writtenStartTime,
      @RequestParam(required = false) @ApiParam("入库查询结束时间") Date writtenEndTime,
      @RequestParam(required = false) @ApiParam("申请开始时间") Date startTime,
      @RequestParam(required = false) @ApiParam("申请结束时间") Date endTime,
      @RequestParam(required = false) @ApiParam("绑定查询开始时间") Date bindStartTime,
      @RequestParam(required = false) @ApiParam("绑定查询结束时间") Date bindEndTime) {

    return success(cardInfoService.list(currentPage, pageSize, applyCode, cardName,
                                        merCode, cardType, cardMedium,
                                        cardStatus, writtenStartTime,
                                        writtenEndTime, startTime,
                                        endTime, bindStartTime, bindEndTime
    ));

  }

  @GetMapping("/admin/{cardNo}")
  @ApiOperation("后台根据卡号获取卡信息")
  public R<CardInfoDTO> queryCardInfoAdmin(
      @PathVariable(value = "cardNo") @ApiParam("卡号") String cardNo) {
    CardInfo cardInfo = cardInfoService.getByCardNo(cardNo);

    CardApply cardApply = applyService.queryByApplyCode(cardInfo.getApplyCode());

    QueryWrapper<Merchant> queryWrapperM = new QueryWrapper<>();

    queryWrapperM.eq(Merchant.MER_CODE, cardApply.getApplyCode());

    Merchant merchant = merchantService.getMerchantByMerCode(queryWrapperM);

    CardInfoDTO cardInfoDTO = new CardInfoDTO();
    cardInfoDTO.setCardId(cardInfo.getCardId());
    cardInfoDTO.setCardName(cardApply.getCardName());
    cardInfoDTO.setCardType(cardInfo.getCardType());
    cardInfoDTO.setCardMedium(cardApply.getCardMedium());
    cardInfoDTO.setCardStatus(cardInfo.getCardStatus());
    if(merchant != null) {
      cardInfoDTO.setMerName(merchant.getMerName());
    }
    cardInfoDTO.setCreateTime(cardInfo.getCreateTime());
    cardInfoDTO.setWrittenTime(cardInfo.getWrittenTime());
    cardInfoDTO.setBindTime(cardInfo.getBindTime());
    cardInfoDTO.setAccountCode(String.valueOf(cardInfo.getAccountCode()));

    return success(cardInfoDTO);
  }

}
