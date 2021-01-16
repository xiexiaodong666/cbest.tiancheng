package com.welfare.service.dto.payment;

import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.BarcodeService;
import com.welfare.service.CardInfoService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
    @ApiModelProperty(value = "卡内信息")
    private String cardInsideInfo;
    @ApiModelProperty(value = "磁条号",required = true)
    private String magneticStripe;
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
        CardInfo cardInfo = cardInfoDao.getOneByMagneticStripe(magneticStripe);
        Long accountCode = cardInfo.getAccountCode();
        this.setAccountCode(accountCode);
        return accountCode;
    }
}
