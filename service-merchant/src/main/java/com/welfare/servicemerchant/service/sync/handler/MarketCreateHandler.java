package com.welfare.servicemerchant.service.sync.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.service.dto.SupplierStoreSyncDTO;
import com.welfare.service.remote.entity.CbestPayBaseBizResp;
import com.welfare.service.remote.entity.CbestPayCreateMarketReq;
import com.welfare.service.remote.entity.CbestPayRespRetryConstant;
import com.welfare.service.remote.entity.CbestPayRespStatusConstant;
import com.welfare.service.remote.service.CbestPayService;
import com.welfare.service.sync.event.SupplierStoreEvt;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 *
 * @author hao.yin
 * @version 1.0.0
 * @date 2020/12/29 10:30
 */
@Component
@Slf4j
public class MarketCreateHandler {


    @Autowired
    PersistentBus persistentBus;
    @Autowired
    CbestPayService cbestPayService;
    @Autowired
    ObjectMapper mapper;

    private static final String retryField="retry";


    @PostConstruct
    private void register() {
        try {
            persistentBus.register(this);
        } catch (PersistentBus.EventBusException e) {
            log.error(e.getMessage(), e);
        }
    }

    @AllowConcurrentEvents
    @Subscribe
    public void onMarketCreateAdd(SupplierStoreEvt evt) {
        if(EmptyChecker.isEmpty(evt.getSupplierStoreDetailDTOS())){
            return;
        }
        if(!evt.getTypeEnum().getCode().equals(ShoppingActionTypeEnum.ADD.getCode())){
            return;
        }
        log.info("同步门店到重百付，event【{}】", JSON.toJSONString(evt));
        SupplierStoreSyncDTO syncDTO=evt.getSupplierStoreDetailDTOS().get(0);
        if(EmptyChecker.notEmpty(syncDTO)){
            CbestPayCreateMarketReq req=new CbestPayCreateMarketReq();
            req.setMarketName(syncDTO.getStoreName());
            log.info("同步门店到重百付，req【{}】，storeCode【{}】", JSON.toJSONString(req),syncDTO.getStoreCode());
            CbestPayBaseBizResp resp= cbestPayService.marketCreate(syncDTO.getStoreCode(),req);
            log.info("同步门店到重百付，resp【{}】", JSON.toJSONString(resp));
            String bizStatus = resp.getBizStatus();
            switch (bizStatus) {
                case CbestPayRespStatusConstant
                        .SUCCESS:
                     return;
                case CbestPayRespStatusConstant
                        .FAIL:
                    Map<String,String> map= null;
                    try {
                        map = mapper.readValue(resp.getBizContent(), Map.class);
                    } catch (JsonProcessingException e) {
                        throw new BizException("重百付创建门店接口，response转换失败" + resp);
                    }
                    if (CbestPayRespRetryConstant.Y.equals(map.get(retryField))){
                        throw new BizException("重百付创建门店未成功，需要重试，" + resp.getBizMsg());
                    }else{
                        //如果传入的门店编号，重百付已存在，或者为重百门店，或者符合重百编码规则，则重百付不会创建，且不需要重试
                        log.info("重百付创建门店未成功,不需要重试【{}】",resp.getBizMsg());
                    }
                    break;
                default:
                    throw new BizException("重百付创建门店失败【"+resp.getBizMsg()+"】，未知返回状态" + bizStatus);
            }
        }
    }
}
