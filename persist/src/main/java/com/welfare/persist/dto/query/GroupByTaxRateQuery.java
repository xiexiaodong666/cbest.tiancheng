package com.welfare.persist.dto.query;

import com.welfare.persist.dto.settlement.wholesale.PlatformWholesaleSettleDetailDTO;
import com.welfare.persist.entity.WholesalePayableSettleDetail;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/5/17 9:31 下午
 */
@Data
public class GroupByTaxRateQuery {


    private String orderId;

    private String transNo;

    public static List<GroupByTaxRateQuery> of(List<WholesalePayableSettleDetail> details) {
        List<GroupByTaxRateQuery> queries = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(details)) {
            details.forEach(wholesalePayableSettleDetail -> {
                GroupByTaxRateQuery query = new GroupByTaxRateQuery();
                query.setOrderId(wholesalePayableSettleDetail.getOrderId());
                query.setTransNo(wholesalePayableSettleDetail.getTransNo());
                queries.add(query);
            });
        }
        return queries;
    }


    public static List<GroupByTaxRateQuery> ofRecieve(List<PlatformWholesaleSettleDetailDTO> details) {
        List<GroupByTaxRateQuery> queries = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(details)) {
            details.forEach(wholesalePayableSettleDetail -> {
                GroupByTaxRateQuery query = new GroupByTaxRateQuery();
                query.setOrderId(wholesalePayableSettleDetail.getOrderNo());
                query.setTransNo(wholesalePayableSettleDetail.getTransNo());
                queries.add(query);
            });
        }
        return queries;
    }
}
