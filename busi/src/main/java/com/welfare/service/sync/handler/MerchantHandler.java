package com.welfare.service.sync.handler;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.GenerateCodeUtil;
import com.welfare.service.dto.MerchantAddressDTO;
import com.welfare.service.dto.MerchantDetailDTO;
import com.welfare.service.dto.MerchantSyncDTO;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.MerchantShoppingReq;
import com.welfare.service.remote.entity.RoleConsumptionResp;
import com.welfare.service.sync.event.MerchantEvt;
import lombok.extern.slf4j.Slf4j;
import org.killbill.bus.api.PersistentBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author hao.yin
 * @version 1.0.0
 * @date 2020/12/29 10:30
 */
@Component
@Slf4j
public class MerchantHandler  {


    @Autowired
    PersistentBus persistentBus;
    @Autowired
    ShoppingFeignClient shoppingFeignClient;


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
    public void onMerchantChange(MerchantEvt evt) {
        ShoppingActionTypeEnum typeEnum=evt.getTypeEnum();
        List<MerchantSyncDTO> merchantDetailDTOList=evt.getMerchantDetailDTOList();
        if (EmptyChecker.isEmpty(merchantDetailDTOList)) {
            return;
        }
        MerchantShoppingReq req = new MerchantShoppingReq();
        req.setActionType(typeEnum.getCode());
        req.setTimestamp(new Date());
        req.setRequestId(GenerateCodeUtil.UUID());
        List<MerchantShoppingReq.ListBean> list = new ArrayList<>();
        List<String> merCodeList = new ArrayList<>();
        for (MerchantSyncDTO merchant : merchantDetailDTOList) {
            MerchantShoppingReq.ListBean listBean = new MerchantShoppingReq.ListBean();
            if(EmptyChecker.notEmpty(merchant.getSelfRecharge())){
                listBean.setCanSelfCharge(merchant.getSelfRecharge().equals("1") ? Boolean.TRUE : Boolean.FALSE);
            }
            listBean.setMerchantCode(merchant.getMerCode());
            listBean.setMerchantName(merchant.getMerName());
            if(EmptyChecker.notEmpty(merchant.getMerIdentity())){
                listBean.setIdTypes(Arrays.asList(merchant.getMerIdentity().split(",")));
            }
            List<MerchantShoppingReq.ListBean.AddressBean> addressBeans = new ArrayList<>();
            if(EmptyChecker.notEmpty(merchant.getAddressList())){
                for (MerchantAddressDTO addressDTO : merchant.getAddressList()) {
                    MerchantShoppingReq.ListBean.AddressBean addressBean = new MerchantShoppingReq.ListBean.AddressBean();
                    addressBean.setAddress(addressDTO.getAddress());
                    addressBean.setAddressType(addressDTO.getAddressType());
                    addressBean.setName(addressDTO.getAddressName());
                    addressBeans.add(addressBean);
                }
                listBean.setAddress(addressBeans);
            }
            list.add(listBean);
            merCodeList.add(merchant.getMerCode());
        }
        req.setList(list);
        RoleConsumptionResp resp = shoppingFeignClient.addOrUpdateMerchant(req);
        if (!("0000").equals(resp.getCode())) {
            throw new BusiException("同步商户数据到商城中心失败msg【"+resp.getMsg()+"】");
        }
    }
}
