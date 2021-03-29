package com.welfare.service.remote.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSON;
import com.welfare.common.config.CbestPayConfig;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.service.remote.CbestPayFeign;
import com.welfare.service.remote.entity.*;
import com.welfare.service.remote.service.CbestPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CbestPayServiceImpl implements CbestPayService {

    private final CbestPayConfig cbestPayConfig;
    @Autowired(required = false)
    private CbestPayFeign cbestPayFeign;

    @Override
    public CbestPayBaseBizResp createWXH5Trade(String market, CreateWXH5TradeReq req) {
        CbestPayBaseBizResp resp = request("create.wx.h5.trade", market, req);
        return resp;
    }

    @Override
    public CbestPayBaseBizResp tradeCancel(String market, TradeCancelReq req) {
        CbestPayBaseBizResp resp = request("trade.cancel", market, req);
        return resp;
    }

    @Override
    public CbestPayBaseBizResp tradeQuery(String market, TradeQueryReq req) {
        CbestPayBaseBizResp resp = request("trade.query", market, req);
        return resp;
    }

    @Override
    public CbestPayBaseBizResp marketCreate(String market, CbestPayCreateMarketReq req) {
        CbestPayBaseBizResp resp = request("market.create", market, req);
        return resp;
    }

    @Override
    public CbestPayBaseBizResp tradeRefund(String market, TradeRefundReq req) {
        CbestPayBaseBizResp resp = request("trade.refund", market, req);
        return resp;
    }

    @Override
    public CbestPayBaseBizResp tradeRefundQuery(String market, TradeRefundQueryReq req) {
        CbestPayBaseBizResp resp = request("trade.refund.query", market, req);
        return resp;
    }

    private <T> CbestPayBaseBizResp request(String method, String market, T bizContent) {
        CbestPayBaseReq req = buildBaseReq(method, market, bizContent);
        CbestPayBaseResp resp = cbestPayFeign.exec(req);
        if (!CbestPayRespStatusConstant.SUCCESS.equals(resp.getStatus())) {
            log.error(StrUtil
                .format("调用重百付{}接口异常-req: {}, resp: {}", method, JSON.toJSONString(req),
                    JSON.toJSONString(resp)));
            throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
        }
        log.info(StrUtil.format("调用重百付{}接口-req: {}, resp: {}", method, JSON.toJSONString(req),
            JSON.toJSONString(resp)));
        CbestPayBaseBizResp cbestPayBaseBizResp = new CbestPayBaseBizResp();
        BeanUtil.copyProperties(resp, cbestPayBaseBizResp);
        return cbestPayBaseBizResp;
    }

    private <T> CbestPayBaseReq buildBaseReq(String method, String market, T bizContent) {
        CbestPayBaseReq req = new CbestPayBaseReq();
        req.setAppId(cbestPayConfig.getAppId());
        req.setMarket(market);
        req.setMethod(method);
        req.setVersion("1.1");
        req.setFormat("json");
        req.setCharset("utf-8");
        req.setSignType("MD5");
        req.setTimestamp(DateUtil.now());
        req.setBizContent(JSON.toJSONString(bizContent));
        String sign = generateSign(req);
        req.setSign(sign);
        return req;
    }

    private String generateSign(CbestPayBaseReq req) {
        Map<String, Object> map = BeanUtil.beanToMap(req, true, true);
        String params = map.entrySet().stream()
            .sorted(Entry.comparingByKey())
            .map(item -> item.getKey() + "=" + item.getValue())
            .collect(Collectors.joining("&"))
            .concat(cbestPayConfig.getAppKey());
        String sign = SecureUtil.md5(params);
        return sign;
    }

    @Override
    public AlipayUserAgreementSignResp alipayUserAgreementSign(String market,
        AlipayUserAgreementSignReq req) {
        CbestPayBaseBizResp resp = request("sjgoalipay.user.agreement.sign", market, req);
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            log.error(
                StrUtil.format("调用重百付支付宝代扣签约接口失败-req: {}, resp: {}", JSON.toJSONString(req),
                    JSON.toJSONString(resp)));
            throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
        }
        AlipayUserAgreementSignResp alipayUserAgreementSignResp = JSON
            .parseObject(resp.getBizContent(), AlipayUserAgreementSignResp.class);
        return alipayUserAgreementSignResp;
    }

    @Override
    public AlipayUserAgreementQueryResp alipayUserAgreementQuery(String market,
        AlipayUserAgreementQueryReq req) {
        CbestPayBaseBizResp resp = request("sjgoalipay.user.agreement.query", market, req);
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            log.error(
                StrUtil.format("调用重百付支付宝代扣签约查询接口失败-req: {}, resp: {}", JSON.toJSONString(req),
                    JSON.toJSONString(resp)));
            throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
        }
        AlipayUserAgreementQueryResp alipayUserAgreementQueryResp = JSON
            .parseObject(resp.getBizContent(), AlipayUserAgreementQueryResp.class);
        return alipayUserAgreementQueryResp;
    }

    @Override
    public AlipayUserAgreementUnsignResp alipayUserAgreementUnsign(String market,
        AlipayUserAgreementUnsignReq req) {
        CbestPayBaseBizResp resp = request("sjgoalipay.user.agreement.unsign", market, req);
        String bizStatus = resp.getBizStatus();
        if (!CbestPayRespStatusConstant.SUCCESS.equals(bizStatus)) {
            log.error(
                StrUtil.format("调用重百付支付宝代扣解约接口失败-req: {}, resp: {}", JSON.toJSONString(req),
                    JSON.toJSONString(resp)));
            throw new BusiException(ExceptionCode.UNKNOWON_EXCEPTION, "系统异常", null);
        }
        AlipayUserAgreementUnsignResp alipayUserAgreementUnsignResp = JSON
            .parseObject(resp.getBizContent(), AlipayUserAgreementUnsignResp.class);
        return alipayUserAgreementUnsignResp;
    }
}
