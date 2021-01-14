package com.welfare.service.sync.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.persist.entity.SupplierStore;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantDetailDTO;
import com.welfare.service.dto.SupplierStoreDetailDTO;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.MerchantShoppingReq;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.remote.entity.StoreShoppingReq;
import com.welfare.service.sync.event.SupplierStoreEvt;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author hao.yin
 * @version 1.0.0
 * @date 2020/12/29 10:30
 */
@Component
@Slf4j
public class SupplierStoreHandler {


    @Autowired
    PersistentBus persistentBus;
    @Autowired
    ShoppingFeignClient shoppingFeignClient;
    @Autowired
    ObjectMapper mapper;


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
    public void onMerchantChange(SupplierStoreEvt evt) {
        ShoppingActionTypeEnum typeEnum=evt.getTypeEnum();
        List<SupplierStoreDetailDTO> supplierStoreDetailDTOS=evt.getSupplierStoreDetailDTOS();
        if (EmptyChecker.isEmpty(supplierStoreDetailDTOS)) {
            return;
        }

        StoreShoppingReq storeShoppingReq = new StoreShoppingReq();
        List<StoreShoppingReq.ListBean> listBeans = new ArrayList<>();
        List<String> storeCodeList = new ArrayList<>();
        for (SupplierStoreDetailDTO supplierStoreDetailDTO : supplierStoreDetailDTOS) {
            StoreShoppingReq.ListBean listBean = new StoreShoppingReq.ListBean();
            Map<String, Boolean> consumeTypeMap = null;
            try {
                consumeTypeMap = mapper.readValue(
                        supplierStoreDetailDTO.getConsumType(), Map.class);
            } catch (JsonProcessingException e) {
                throw new BusiException("同步门店信息到商城中心，消费类型转换失败【{supplierStoreDetailDTO.getConsumType()}】");
            }
            listBean.setConsumeTypes(ConsumeTypesUtils.transfer(consumeTypeMap));
            listBean.setMerchantCode(supplierStoreDetailDTO.getMerCode());
            listBean.setStoreName(supplierStoreDetailDTO.getStoreName());
            listBean.setStoreCode(supplierStoreDetailDTO.getStoreCode());
            listBean.setEnabled(supplierStoreDetailDTO.getStatus().equals(1));
            //门店相关地址
            List<StoreShoppingReq.ListBean.AddressBean> addressBeans = new ArrayList<>();
            for (MerchantAddressDTO addressDTO : supplierStoreDetailDTO.getAddressList()) {
                StoreShoppingReq.ListBean.AddressBean addressBean = new StoreShoppingReq.ListBean.AddressBean();
                addressBean.setAddress(addressDTO.getAddress());
                addressBean.setAddressType(addressDTO.getAddressType());
                addressBean.setName(addressDTO.getAddressName());
            }
            listBean.setAddress(addressBeans);
            listBeans.add(listBean);
            storeCodeList.add(supplierStoreDetailDTO.getStoreCode());
        }
        storeShoppingReq.setActionType(typeEnum.getCode());
        storeShoppingReq.setRequestId(GenerateCodeUtil.UUID());
        storeShoppingReq.setTimestamp(new Date());
        storeShoppingReq.setList(listBeans);
        RoleConsumptionResp resp = shoppingFeignClient.addOrUpdateStore(storeShoppingReq);
        if (!("0000").equals(resp.getCode())) {
            throw new BusiException("同步门店数据到商城中心失败msg【"+resp.getMsg()+"】");
        }
    }
}
