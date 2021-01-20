package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.util.EmptyChecker;
import com.welfare.service.dto.SupplierStoreSyncDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;

import java.util.List;
import java.util.UUID;

/**
 * Created by hao.yin on 2021/1/14.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SupplierStoreEvt implements BusEvent {
    protected ShoppingActionTypeEnum typeEnum;
    protected List<SupplierStoreSyncDTO> supplierStoreDetailDTOS;
    @Override
    public Long getSearchKey1() {
        return typeEnum.getEvtType();
    }

    @Override
    public Long getSearchKey2() {
        if(EmptyChecker.notEmpty(supplierStoreDetailDTOS)
                &&supplierStoreDetailDTOS.size()==1){
            return supplierStoreDetailDTOS.get(0).getId();
        }
        return null;
    }

    @Override
    public UUID getUserToken() {
        return UUID.randomUUID();
    }
}
