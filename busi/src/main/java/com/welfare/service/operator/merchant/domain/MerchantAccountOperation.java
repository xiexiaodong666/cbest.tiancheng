package com.welfare.service.operator.merchant.domain;

import com.welfare.common.constants.WelfareConstant.MerCreditType;
import com.welfare.service.enums.IncOrDecType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Description: 表示某种类型的MerchantAccountType操作了多少
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/11/2021
 */
@Data
public class MerchantAccountOperation {
    private MerCreditType type;
    private BigDecimal amount;
    private IncOrDecType incOrDecType;

    /**
     * 返回一个MerchantAccountOperation
     * @param operateType
     * @param amount
     * @param incOrDecType
     * @return
     */
    public static MerchantAccountOperation of(MerCreditType operateType, BigDecimal amount, IncOrDecType incOrDecType){
        MerchantAccountOperation merchantAccountOperation = new MerchantAccountOperation();
        merchantAccountOperation.setType(operateType);
        merchantAccountOperation.setAmount(amount);
        merchantAccountOperation.setIncOrDecType(incOrDecType);
        return merchantAccountOperation;
    }
}
