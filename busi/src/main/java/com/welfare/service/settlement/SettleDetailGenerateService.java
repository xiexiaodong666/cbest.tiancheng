package com.welfare.service.settlement;

import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
public interface SettleDetailGenerateService {
    /**
     * 根据扣款明细id生成结算明细
     * @param accountDeductionDetailId 扣款明细id列表
     */
    void generateWholesaleDetails(List<Long> accountDeductionDetailId);
}
