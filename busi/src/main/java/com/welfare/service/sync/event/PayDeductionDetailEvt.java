package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.killbill.bus.api.BusEvent;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/29/2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class PayDeductionDetailEvt implements BusEvent {
    private static final Long SEARCH_KEY1 = 1L;
    /**
     * 付款产生的流水明细id
     */
    private List<Long> accountDeductionDetailIds;

    @Override
    public Long getSearchKey1() {
        return SEARCH_KEY1;
    }

    @Override
    public Long getSearchKey2() {
        return null;
    }

    @Override
    public UUID getUserToken() {
        return UUID.randomUUID();
    }
}
