package com.welfare.service.card;

import cn.hutool.core.util.RandomUtil;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.SequenceTypeEnum;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.entity.Sequence;
import com.welfare.service.SequenceService;
import com.welfare.service.dto.BatchSequence;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/25/2021
 */
@Component
@RequiredArgsConstructor
public class MagneticStripeCardGenerator implements ICardGenerator {
    private final SequenceService sequenceService;
    private static final String PREFIX = "100";

    @Override
    public List<CardInfo> generate(CardApply cardApply) {
        Integer cardNum = cardApply.getCardNum();
        BatchSequence batchSequence = sequenceService.batchGenerate(SequenceTypeEnum.MAGNETIC_STRIPE_CARD_ID.getCode(), cardNum);
        List<Sequence> sequences = batchSequence.getSequences();
        return sequences.parallelStream().map(sequence -> {
            CardInfo cardInfo = new CardInfo();
            cardInfo.setApplyCode(cardApply.getApplyCode());
            cardInfo.setCardStatus(WelfareConstant.CardStatus.NEW.code());
            cardInfo.setDeleted(false);
            cardInfo.setEnabled(WelfareConstant.CardEnable.ENABLE.code());
            generateCardId(cardInfo,sequence.getSequenceNo());
            return cardInfo;
        }).collect(Collectors.toList());
    }

    private void generateCardId(CardInfo cardInfo, Long sequenceNo){
        long checkCode = RandomUtil.randomLong(10000000L, 99999999L);
        long cardId = sequenceNo + checkCode;
        String fullCardId = PREFIX + cardId;
        cardInfo.setCardId(fullCardId);
        cardInfo.setMagneticStripe(fullCardId);
        cardInfo.setCheckCode(checkCode);
    }
}
