package com.welfare.service.remote.service;

import com.welfare.service.remote.entity.CreateWXH5TradeReq;
import com.welfare.service.remote.entity.TradeCancelReq;
import com.welfare.service.remote.entity.TradeQueryReq;
import com.welfare.service.remote.entity.TradeRefundQueryReq;
import com.welfare.service.remote.entity.TradeRefundReq;
import com.welfare.service.remote.entity.CbestPayBaseBizResp;

public interface CbestPayService {

    /**
     * 聚合微信H5交易创建
     *
     * @param req
     * @return
     */
    CbestPayBaseBizResp createWXH5Trade(String market, CreateWXH5TradeReq req);


    /**
     * 聚合交易冲正
     *
     * @param req
     * @return
     */
    CbestPayBaseBizResp tradeCancel(String market, TradeCancelReq req);

    /**
     * 聚合交易查询
     *
     * @param req
     * @return
     */
    CbestPayBaseBizResp tradeQuery(String market, TradeQueryReq req);

    /**
     * 聚合交易退款
     *
     * @param req
     * @return
     */
    CbestPayBaseBizResp tradeRefund(String market, TradeRefundReq req);

    /**
     * 聚合交易退款查询
     *
     * @param req
     * @return
     */
    CbestPayBaseBizResp tradeRefundQuery(String market, TradeRefundQueryReq req);
}
