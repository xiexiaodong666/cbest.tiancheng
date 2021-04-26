package com.welfare.service.card;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.SequenceTypeEnum;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.SequenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/25/2021
 */
@Component
@RequiredArgsConstructor
public class IcCardGenerator implements ICardGenerator {

    private final SequenceService sequenceService;

    /**
     *  卡片规则 TC01 + 客户商户编号（4位）+  9位自增（从100000001开始
     */
    private final static String PREFIX = "TC01";
    private final static Long START_ID = 100000000L;

    @Override
    public List<CardInfo> generate(CardApply cardApply) {
        List<CardInfo> cardInfoList = new ArrayList<>();
        Integer cardNum = cardApply.getCardNum();
        Long writeCardId = sequenceService.nextNo(
                SequenceTypeEnum.CARDID.getCode(),
                cardApply.getMerCode(), START_ID,
                cardNum
        );
        for (int i = 0; i < cardNum; i++) {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setApplyCode(cardApply.getApplyCode());
            cardInfo.setCardId(PREFIX + cardApply.getMerCode() + writeCardId);
            cardInfo.setMagneticStripe(PREFIX + GenerateCodeUtil.UUID());
            cardInfo.setCardStatus(WelfareConstant.CardStatus.NEW.code());
            cardInfo.setDeleted(false);
            cardInfo.setEnabled(WelfareConstant.CardEnable.ENABLE.code());
            cardInfoList.add(cardInfo);
            writeCardId--;
        }
        return cardInfoList;
    }
}
