package com.welfare.servicemerchant.dto;

import com.welfare.persist.entity.CardInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/13/2021
 */
@Data
public class CardSimpleDTO {
    @ApiModelProperty("账户号")
    @NotNull(message = "账户号不能为空")
    private Long accountCode;
    @ApiModelProperty("卡id")
    @NotNull(message = "卡号不能为空")
    private String cardId;
    @ApiModelProperty("卡内信息")
    @NotNull(message = "卡内磁条信息不能为空")
    private String magneticStripe;

    public CardInfo toCardInfo(){
        CardInfo cardInfo = new CardInfo();
        cardInfo.setCardId(this.cardId);
        cardInfo.setAccountCode(this.accountCode);
        cardInfo.setMagneticStripe(this.magneticStripe);
        return  cardInfo;
    }
}
