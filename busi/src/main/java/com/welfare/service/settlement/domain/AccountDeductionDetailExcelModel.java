package com.welfare.service.settlement.domain;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.AccountBillDetail;
import com.welfare.persist.entity.AccountDeductionDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/8/2021
 */
@Data
public class AccountDeductionDetailExcelModel implements Serializable {
    private String transNo;
    private BigDecimal transAmount;
    private String transType;
    private String merCode;
    private String accountCode;
    private String transDate;
    private String storeCode;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String CONSUME = "Pay";
    private static final String REFUND = "Refund";

    public static AccountDeductionDetailExcelModel of(AccountDeductionDetail accountDeductionDetail, Map<String,String> storeMerCodeMap){
        AccountDeductionDetailExcelModel accountDeductionDetailExcelModel = new AccountDeductionDetailExcelModel();
        accountDeductionDetailExcelModel.setTransAmount(accountDeductionDetail.getTransAmount());
        if(WelfareConstant.TransType.CONSUME.code().equals(accountDeductionDetail.getTransType())){
            accountDeductionDetailExcelModel.setTransType(CONSUME);
            accountDeductionDetailExcelModel.setTransNo(accountDeductionDetail.getTransNo());
        }else if(WelfareConstant.TransType.REFUND.code().equals(accountDeductionDetail.getTransType())){
            accountDeductionDetailExcelModel.setTransType(REFUND);
            accountDeductionDetailExcelModel.setTransNo(accountDeductionDetail.getRelatedTransNo());
        }else{
            accountDeductionDetailExcelModel.setTransType(accountDeductionDetail.getTransType());
        }
        accountDeductionDetailExcelModel.setAccountCode(accountDeductionDetail.getAccountCode().toString());
        accountDeductionDetailExcelModel.setMerCode(storeMerCodeMap.get(accountDeductionDetail.getStoreCode()));
        String transTimeStr = format(accountDeductionDetail.getTransTime());
        accountDeductionDetailExcelModel.setTransDate(transTimeStr);
        accountDeductionDetailExcelModel.setStoreCode(accountDeductionDetail.getStoreCode());

        return accountDeductionDetailExcelModel;
    }

    private static synchronized String format(Date date){
        return dateFormat.format(date);
    }
}
