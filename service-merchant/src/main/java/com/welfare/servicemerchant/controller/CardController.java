package com.welfare.servicemerchant.controller;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.CardInfoService;
import com.welfare.servicemerchant.dto.LoginInfo;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.common.support.IController;
import net.dreamlu.mica.core.result.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public R<CardInfo> queryCardInfo(@PathVariable(value = "cardNo") @ApiParam("卡号") String cardNo){
        CardInfo cardInfo = cardInfoService.getByCardNo(cardNo);
        return success(cardInfo);
    }

    @GetMapping("/apply/{applyCode}")
    @ApiOperation("根据批次号获取卡信息")
    public R<List<CardInfo>> queryCardInfoByBatchNo(@PathVariable(value = "applyCode") @ApiParam("制卡申请号（批次号）") String applyCode){
        List<CardInfo> cardInfos = cardInfoService.listByApplyCode(applyCode, WelfareConstant.CardStatus.NEW.code());
        return success(cardInfos);
    }

    @PutMapping("/written")
    @ApiOperation("更新卡片写入成功")
    public R<CardInfo> updateToWritten(@RequestBody CardInfo cardInfo){
        CardInfo result = cardInfoService.updateWritten(cardInfo);
        return success(result);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public R<Boolean> login(@RequestBody LoginInfo loginInfo ){
        return null;
    }
}
