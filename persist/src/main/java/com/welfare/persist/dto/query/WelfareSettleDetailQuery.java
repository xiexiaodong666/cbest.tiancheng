package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细响应dto
 */
@Data
public class WelfareSettleDetailQuery {
    @ApiModelProperty(value = "商户编号")
    private String merCode;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编号")
    private String storeCode;

    @ApiModelProperty(value = "起始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "福利类型")
    private String welfareTypeCode;

    @ApiModelProperty(value = "门店类型 自营：self，第三方：third")
    private String storeType;

    @ApiModelProperty(value = "消费流水号")
    private String transNo;

    @ApiModelProperty(value = "需要剔除的id列表")
    private List<Long> excludeIdList;

    /**
     * 最小id
     */
    private Long minId;

    /**
     * 查询数据量限制
     */
    private Integer limit;

    /**
     * 线上的pos
     */
    private String posOnlines;
}
