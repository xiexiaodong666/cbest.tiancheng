package com.welfare.servicesettlement.dto.mall;

import com.welfare.persist.entity.OrderInfo;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/29/2021
 */
@Data
public class OrderMQInfo extends OrderDetailInfo implements Serializable {
    private static final long serialVersionUID = -1487457198472623355L;
    private Integer lastStatusCode;
    private BigDecimal refundAmount;
    private BigDecimal supplierRefundAmount;
    private Date refundTime;
    private List<Long> cartIds;
    private Map<String, Integer> productInfo;

    public OrderInfo toOrderInfo(){
        return null;
    }
}
