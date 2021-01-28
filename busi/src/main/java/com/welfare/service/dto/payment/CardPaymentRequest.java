package com.welfare.service.dto.payment;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.entity.CardInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@ApiModel("刷卡支付请求")
@Data
public class CardPaymentRequest extends PaymentRequest {
    @ApiModelProperty(value = "卡内信息(磁条号)",required = true)
    private String cardInsideInfo;
    @ApiModelProperty(value = "卡号")
    private String cardNo;
    @ApiModelProperty(value = "卡条码")
    private String cardBarcode;

    @Override
    public Long calculateAccountCode(){
        if(!Objects.isNull(super.getAccountCode())){
            return super.getAccountCode();
        }
        CardInfoDao cardInfoDao = SpringBeanUtils.getBean(CardInfoDao.class);
        CardInfo cardInfo = cardInfoDao.getOneByMagneticStripe(cardInsideInfo);
        Assert.notNull(cardInfo,"根据磁条号未找到卡片:" + cardInsideInfo);
        Assert.isTrue(WelfareConstant.CardEnable.ENABLE.code().equals(cardInfo.getEnabled()),"卡片已禁用");
        Long accountCode = cardInfo.getAccountCode();
        this.setAccountCode(accountCode);
        return accountCode;
    }
}
