package com.welfare.persist.dto.query;

import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.entity.CardApply;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/1/9 6:33 PM
 */
@Data
public class CardApplyAddReq {

    /**
     * 卡片名称
     */
    @ApiModelProperty("卡片名称")
    private String cardName;

    /**
     * 卡片类型
     */
    @ApiModelProperty("卡片类型")
    private String cardType;

    /**
     * 卡片介质
     */
    @ApiModelProperty("卡片介质:MAGNETIC_CARD-磁条卡,IC_CARD-ic卡")
    private String cardMedium;

    /**
     * 卡片数量
     */
    @ApiModelProperty("卡片数量")
    private Integer cardNum;


    /**
     * 识别码方法
     */
    @ApiModelProperty("识别码方法")
    private String identificationCode;
    /**
     * 识别码长度
     */
    @ApiModelProperty("识别码长度")
    private Integer identificationLength;

    /**
     * 商户代码
     */
    @ApiModelProperty("商户代码")
    private String merCode;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    public static CardApply toCardApply(CardApplyAddReq cardApplyAddReq) {
        CardApply cardApply = new CardApply();
        // uuid 生成16位不重复的code, 暂不考虑分布式情况下的并发
        cardApply.setApplyCode(GenerateCodeUtil.getAccountIdByUUId());
        cardApply.setMerCode(cardApplyAddReq.getMerCode());
        cardApply.setCardName(cardApplyAddReq.getCardName());
        cardApply.setCardType(cardApplyAddReq.getCardType());
        cardApply.setCardMedium(cardApplyAddReq.getCardMedium());
        cardApply.setCardNum(cardApplyAddReq.getCardNum());
        cardApply.setIdentificationCode(cardApplyAddReq.getIdentificationCode());
        cardApply.setIdentificationLength(cardApplyAddReq.getIdentificationLength());
        cardApply.setRemark(cardApplyAddReq.getRemark());
        cardApply.setDeleted(false);
        cardApply.setStatus(1);
        return cardApply;
    }

}
