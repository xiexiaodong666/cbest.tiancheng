package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.service.dto.SupplierStoreDetailDTO;
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
    protected List<SupplierStoreDetailDTO> supplierStoreDetailDTOS;
    @Override
    public Long getSearchKey1() {
        return null;
    }

    @Override
    public Long getSearchKey2() {
        return null;
    }

    @Override
    public UUID getUserToken() {
        return null;
    }
}
