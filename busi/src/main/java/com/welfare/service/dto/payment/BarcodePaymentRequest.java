package com.welfare.service.dto.payment;

import com.welfare.common.util.SpringBeanUtils;
import com.welfare.service.BarcodeService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@ApiModel("扫码支付请求")
@Data
public class BarcodePaymentRequest extends PaymentRequest {
    @ApiModelProperty("条码")
    private String barcode;
    @ApiModelProperty("扫描日期，yyyy-MM-ddTHH:mm:ss+08:00,例:2021-01-01T12:00:00+08:00")
    private Date scanDate;

    @Override
    public Long calculateAccountCode(){
        if(!Objects.isNull(super.getAccountCode())){
            return super.getAccountCode();
        }
        BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
        Long accountCode = barcodeService.parseAccountFromBarcode(barcode, scanDate,getOffline());
        this.setAccountCode(accountCode);
        return accountCode;
    }


}
