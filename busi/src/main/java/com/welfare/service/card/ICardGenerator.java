package com.welfare.service.card;

import com.welfare.common.enums.CardApplyMediumEnum;
import com.welfare.persist.dto.query.CardApplyAddReq;
import com.welfare.persist.entity.CardApply;
import com.welfare.persist.entity.CardInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 制卡卡片生成
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/25/2021
 */
public interface ICardGenerator {

    Map<String,Class<? extends ICardGenerator>> GENERATOR = new HashMap<String,Class<? extends ICardGenerator>>(){
        {
            put(CardApplyMediumEnum.IC_CARD.getCode(),IcCardGenerator.class);
            put(CardApplyMediumEnum.MAGNETIC_CARD.getCode(),MagneticStripeCardGenerator.class);
        }
    };

    /**
     * 生成卡片
     * @param cardApply 制卡申请
     * @return 卡片
     */
    List<CardInfo> generate(CardApply cardApply);
}
