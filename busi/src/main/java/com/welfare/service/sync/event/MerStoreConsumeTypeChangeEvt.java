package com.welfare.service.sync.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.util.ConsumeTypesUtils;
import com.welfare.persist.dto.AdminMerchantStore;
import com.welfare.persist.dto.query.MerchantStoreRelationAddReq;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author duanhy
 * @title: MerStoreConsumeTypeAddEvt
 * @description: TODO
 * @date 2021/3/2819:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class MerStoreConsumeTypeChangeEvt {

    private String merCode;

    private List<StoreConsumeType> changeStoreConsumes;

    /**
     * 变更类型
     */
    private ShoppingActionTypeEnum actionType;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class StoreConsumeType {
        private String storeCode;
        private List<ConsumeTypeEnum> consumeType;

        public static List<StoreConsumeType> of(List<String> storeCodes) {
            List<StoreConsumeType> changeStoreConsumes = new ArrayList<>();
            if (!CollectionUtils.isEmpty(storeCodes)) {
                storeCodes.forEach(s ->  {
                    StoreConsumeType storeConsumeType = new StoreConsumeType();
                    storeConsumeType.setStoreCode(s);
                    changeStoreConsumes.add(storeConsumeType);
                });
            }
            return changeStoreConsumes;
        }
    }

    public static MerStoreConsumeTypeChangeEvt of(MerchantStoreRelationAddReq relationAddReq) {
        Assert.notNull(relationAddReq, "新增商户门店消费方式请求对象不能为空");
        Assert.hasText(relationAddReq.getMerCode(), "商户编码不能为空");
        Assert.notEmpty(relationAddReq.getAdminMerchantStoreList(), "新增商户门店消费方式不能为空");
        MerStoreConsumeTypeChangeEvt evt = new MerStoreConsumeTypeChangeEvt();
        evt.setMerCode(relationAddReq.getMerCode());
        evt.setActionType(ShoppingActionTypeEnum.ADD);
        List<StoreConsumeType> storeConsumeTypes = new ArrayList<>();
        relationAddReq.getAdminMerchantStoreList().forEach(adminMerchantStore -> {
            Assert.hasText(adminMerchantStore.getStoreCode(), "门店编码不能为空");
            StoreConsumeType storeConsumeType = new StoreConsumeType();
            storeConsumeType.setStoreCode(adminMerchantStore.getStoreCode());
            storeConsumeType.setConsumeType(ConsumeTypesUtils.getByConsumeTypeJson(adminMerchantStore.getConsumType()));
            storeConsumeTypes.add(storeConsumeType);
        });
        evt.setChangeStoreConsumes(storeConsumeTypes);
        return evt;
    }
}
