package com.welfare.servicemerchant.service.sync.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.common.util.EmptyChecker;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.StoreConsumeTypeDTO;
import com.welfare.service.dto.SupplierStoreSyncDTO;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.remote.entity.StoreShoppingReq;
import com.welfare.service.remote.entity.StoreShoppingReq.ListBean.ConsumeSettingsBean;
import com.welfare.service.sync.event.SupplierStoreEvt;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Autowired(required = false)
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
    public void onSupplierStoreChange(SupplierStoreEvt evt) {
        log.info("同步门店到商城中台，event【{}】", JSON.toJSONString(evt));
        ShoppingActionTypeEnum typeEnum=evt.getTypeEnum();
        List<SupplierStoreSyncDTO> supplierStoreDetailDTOS=evt.getSupplierStoreDetailDTOS();
        if (EmptyChecker.isEmpty(supplierStoreDetailDTOS)) {
            return;
        }

        StoreShoppingReq storeShoppingReq = new StoreShoppingReq();
        List<StoreShoppingReq.ListBean> listBeans = new ArrayList<>();
        List<String> storeCodeList = new ArrayList<>();
        for (SupplierStoreSyncDTO supplierStoreDetailDTO : supplierStoreDetailDTOS) {
            List<ConsumeSettingsBean> consumeSettings = new ArrayList<>();

            StoreShoppingReq.ListBean listBean = new StoreShoppingReq.ListBean();
            Map<String, Boolean> consumeTypeMap = null;
            try {
                consumeTypeMap = mapper.readValue(
                        supplierStoreDetailDTO.getConsumType(), Map.class);
            } catch (JsonProcessingException e) {
                throw new BusiException("同步门店信息到商城中心，消费类型转换失败【"+supplierStoreDetailDTO.getConsumType()+"】");
            }
            if(consumeTypeMap!= null && consumeTypeMap.get(ConsumeTypeEnum.SHOP_SHOPPING.getCode())) {
                ConsumeSettingsBean consumeSettingsBean = new ConsumeSettingsBean();
                consumeSettingsBean.setConsumeType(ConsumeTypeEnum.SHOP_SHOPPING.getCode());
                consumeSettings.add(consumeSettingsBean);
                listBean.setConsumeSettings(consumeSettings);
            }
            listBean.setMerchantCode(supplierStoreDetailDTO.getMerCode());
            listBean.setStoreName(supplierStoreDetailDTO.getStoreName());
            listBean.setStoreCode(supplierStoreDetailDTO.getStoreCode());
            listBean.setPhone(supplierStoreDetailDTO.getMobile());
            if(EmptyChecker.notEmpty(supplierStoreDetailDTO.getStoreConsumeTypeList())) {

                for (StoreConsumeTypeDTO storeConsumeTypeDTO:
                supplierStoreDetailDTO.getStoreConsumeTypeList()) {
                    ConsumeSettingsBean consumeSettingsBean = new ConsumeSettingsBean();
                    consumeSettingsBean.setCashierNo(storeConsumeTypeDTO.getCashierNo());
                    consumeSettingsBean.setConsumeType(storeConsumeTypeDTO.getConsumeType());
                    consumeSettings.add(consumeSettingsBean);
                }
                listBean.setConsumeSettings(consumeSettings);
            }
            // listBean.setCashierNo(supplierStoreDetailDTO.getCashierNo());
            listBean.setEnabled(supplierStoreDetailDTO.getStatus().equals(1));
            //门店相关地址
            List<StoreShoppingReq.ListBean.AddressBean> addressBeans = new ArrayList<>();
            if(EmptyChecker.notEmpty(supplierStoreDetailDTO.getAddressList())){
                for (MerchantAddressDTO addressDTO : supplierStoreDetailDTO.getAddressList()) {
                    StoreShoppingReq.ListBean.AddressBean addressBean = new StoreShoppingReq.ListBean.AddressBean();
                    addressBean.setAddress(addressDTO.getAddress());
                    // addressBean.setAddressType(addressDTO.getAddressType());
                    addressBean.setName(addressDTO.getAddressName());
                    addressBeans.add(addressBean);
                }
                listBean.setAddress(addressBeans);
            }
            listBeans.add(listBean);
            storeCodeList.add(supplierStoreDetailDTO.getStoreCode());
        }
        storeShoppingReq.setActionType(typeEnum.getCode());
        storeShoppingReq.setRequestId(evt.getUserToken().toString());
        storeShoppingReq.setTimestamp(evt.getTimestamp());
        storeShoppingReq.setList(listBeans);
        log.info("同步门店到商城中台，req【{}】", JSON.toJSONString(storeShoppingReq));
        RoleConsumptionResp resp = shoppingFeignClient.addOrUpdateStore(storeShoppingReq);
        log.info("同步门店到商城中台，res【{}】req【{}】", JSON.toJSONString(resp), JSON.toJSONString(storeShoppingReq));
        if (!("0000").equals(resp.getCode())) {
            throw new BusiException("同步门店数据到商城中心失败msg【"+resp.getMsg()+"】");
        }
    }
}
