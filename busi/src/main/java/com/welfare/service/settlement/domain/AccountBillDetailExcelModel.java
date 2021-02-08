package com.welfare.service.settlement.domain;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.AccountBillDetail;
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
public class AccountBillDetailExcelModel implements Serializable {
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

    public static AccountBillDetailExcelModel of(AccountBillDetail accountBillDetail, Map<String,String> storeMerCodeMap){
        AccountBillDetailExcelModel accountBillDetailExcelModel = new AccountBillDetailExcelModel();
        accountBillDetailExcelModel.setTransNo(accountBillDetail.getTransNo());
        accountBillDetailExcelModel.setTransAmount(accountBillDetail.getTransAmount());
        if(WelfareConstant.TransType.CONSUME.code().equals(accountBillDetail.getTransType())){
            accountBillDetailExcelModel.setTransType(CONSUME);
        }else if(WelfareConstant.TransType.REFUND.code().equals(accountBillDetail.getTransType())){
            accountBillDetailExcelModel.setTransType(REFUND);
        }else{
            accountBillDetailExcelModel.setTransType(accountBillDetail.getTransType());
        }
        accountBillDetailExcelModel.setAccountCode(accountBillDetail.getAccountCode().toString());
        accountBillDetailExcelModel.setMerCode(storeMerCodeMap.get(accountBillDetail.getStoreCode()));
        String transTimeStr = format(accountBillDetail.getTransTime());
        accountBillDetailExcelModel.setTransDate(transTimeStr);
        accountBillDetailExcelModel.setStoreCode(accountBillDetail.getStoreCode());

        return accountBillDetailExcelModel;
    }

    private static synchronized String format(Date date){
        return dateFormat.format(date);
    }
}
