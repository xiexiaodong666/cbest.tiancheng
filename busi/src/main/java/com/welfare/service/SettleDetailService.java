package com.welfare.service;

import com.welfare.common.base.BasePageVo;
import com.welfare.persist.dto.WelfareSettleSumDTO;
import com.welfare.persist.dto.query.WelfareSettleQuery;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantBillDetail;
import com.welfare.persist.entity.MerchantCredit;
import com.welfare.persist.entity.SettleDetail;
import com.welfare.service.dto.*;

import java.util.List;
import java.util.Map;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/16 10:43 上午
 * @desc
 */
public interface SettleDetailService {

    /**
     * 分页查询商户未结算信息
     * @param welfareSettlePageReq
     * @return
     */
    BasePageVo<WelfareSettleResp> queryWelfareSettlePage(WelfareSettlePageReq welfareSettlePageReq);

    /**
     * 查询商户结算信息summary
     * @param welfareSettleQuery
     * @return
     */
    WelfareSettleSumDTO queryWelfareSettleSum(WelfareSettleQuery welfareSettleQuery);
    /**
     * 查询商户未结算明细信息列表
     * @param welfareSettleDetailReq
     * @return
     */
    List<WelfareSettleDetailResp> queryWelfareSettleDetail(WelfareSettleDetailReq welfareSettleDetailReq);

    /**
     * 分页查询商户未结算明细信息
     * @param welfareSettleDetailPageReq
     * @return
     */
    BasePageVo<WelfareSettleDetailResp> queryWelfareSettleDetailPage(WelfareSettleDetailPageReq welfareSettleDetailPageReq);

    /**
     * 分页查询商户未结算明细额外信息
     * @param welfareSettleDetailPageReq
     * @return
     */
    Map<String, Object> queryWelfareSettleDetailExt(WelfareSettleDetailPageReq welfareSettleDetailPageReq);

    void buildSettle(WelfareSettleDetailReq welfareSettleDetailReq);

    SettleMerInfoResp getMerAccountInfo(String id);

    BasePageVo<SettleMerTransDetailResp> getMerAccountTransPageDetail(String id, SettleMerTransDetailPageReq settleMerTransDetailReq);

    List<SettleMerTransDetailResp> getMerAccountTransDetail(String id, SettleMerTransDetailReq settleMerTransDetailReq);

    void merRebate(Merchant merchant);

    /**
     * 计算并赋值返点金额
     * @param merchantCredit
     * @param settleDetails
     * @return
     */
    List<MerchantBillDetail> calculateAndSetRebate(MerchantCredit merchantCredit, List<SettleDetail> settleDetails);
}
