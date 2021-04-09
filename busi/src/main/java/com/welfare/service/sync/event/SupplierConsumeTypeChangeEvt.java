package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author duanhy
 * @title: SupplierConsumeTypeDelEvt
 * @description: TODO
 * @date 2021/3/2818:12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SupplierConsumeTypeChangeEvt {

    private String storeCode;

    /**
     * 删除门店消费方式
     */
    private List<ConsumeTypeEnum> delConsumeTypes;

    /**
     * 变更类型
     */
    private ShoppingActionTypeEnum actionType;

}
