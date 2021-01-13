package com.welfare.servicemerchant.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dto.CardInfoDTO;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.CardInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
      @RequestParam(required = false) @ApiParam("卡片名称") String cardName,
      @RequestParam(required = false) @ApiParam("所属商户") String merCode,
      @RequestParam(required = false) @ApiParam("卡片类型") String cardType,
      @RequestParam(required = false) @ApiParam("卡片介质") String cardMedium,
      @RequestParam(required = false) @ApiParam("卡片状态") String cardStatus,
      @RequestParam(required = false) @ApiParam("入库查询开始时间") String writtenStartTime,
      @RequestParam(required = false) @ApiParam("入库查询结束时间") String writtenEndTime,
      @RequestParam(required = false) @ApiParam("申请开始时间") String startTime,
      @RequestParam(required = false) @ApiParam("申请结束时间") String endTime,
      @RequestParam(required = false) @ApiParam("绑定查询开始时间") String bindStartTime,
      @RequestParam(required = false) @ApiParam("绑定查询结束时间") String bindEndTime) {

    return success(cardInfoService.list(currentPage, pageSize, cardName,
                                        merCode, cardType, cardMedium,
                                        cardStatus, writtenStartTime,
                                        writtenEndTime, startTime,
                                        endTime, bindStartTime, bindEndTime
    ));
  }

}
