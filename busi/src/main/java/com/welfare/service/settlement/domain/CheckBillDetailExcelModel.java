package com.welfare.service.settlement.domain;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dto.CheckBillDetail;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/8/2021
 */
@Data
public class CheckBillDetailExcelModel implements Serializable {
    private String transNo;
    private BigDecimal transAmount;
    private String transType;
    private String merCode;
    private String merName;
    private String phone;
    private String accountCode;
    private String transDate;
    private String storeCode;
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String CONSUME = "Pay";
    private static final String REFUND = "Refund";

    public static CheckBillDetailExcelModel of(CheckBillDetail checkBillDetail){
        CheckBillDetailExcelModel checkBillDetailExcelModel = new CheckBillDetailExcelModel();
        checkBillDetailExcelModel.setTransAmount(checkBillDetail.getTransAmount());
        if(WelfareConstant.TransType.CONSUME.code().equals(checkBillDetail.getTransType())){
            checkBillDetailExcelModel.setTransType(CONSUME);
            checkBillDetailExcelModel.setTransNo(checkBillDetail.getTransNo());
        }else if(WelfareConstant.TransType.REFUND.code().equals(checkBillDetail.getTransType())){
            checkBillDetailExcelModel.setTransType(REFUND);
            checkBillDetailExcelModel.setTransNo(checkBillDetail.getRelatedTransNo());
        }else{
            checkBillDetailExcelModel.setTransType(checkBillDetail.getTransType());
        }
        checkBillDetailExcelModel.setAccountCode(checkBillDetail.getAccountCode().toString());
        checkBillDetailExcelModel.setMerCode(checkBillDetail.getMerCode());
        checkBillDetailExcelModel.setMerName(checkBillDetail.getMerName());
        checkBillDetailExcelModel.setPhone(checkBillDetail.getPhone());
        String transTimeStr = format(checkBillDetail.getTransTime());
        checkBillDetailExcelModel.setTransDate(transTimeStr);
        checkBillDetailExcelModel.setStoreCode(checkBillDetail.getStoreCode());

        return checkBillDetailExcelModel;
    }

    private static synchronized String format(Date date){
        return dateFormat.format(date);
    }
}
