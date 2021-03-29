package com.welfare.persist.dto.commodityOfflineOrder;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @author gaorui
 * @version 1.0.0
 * @date 2021/3/18 3:46 PM
 */
@Data
public class CommodityOfflineOrderBasicExtResponse {

    /**
     * 总销量
     */
    @ApiModelProperty("总销量")
    private Integer saleTotalCount;

    /**
     * 总销售额
     */
    @ApiModelProperty("总销售额")
    private BigDecimal salesTotal;

    /**
     * 总消费人次
     */
    @ApiModelProperty("总消费人次")
    private Integer saleTotalPeopleCount;

}
