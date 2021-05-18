package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 9:28 下午
 */
@Data
public class PlatformWholesalePayableDetailQuery {

    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("交易流水号")
    private String transNo;
    @ApiModelProperty("结算状态")
    private String settleFlag;
    @ApiModelProperty("客户商户编码")
    private String customerMerCode;
    @ApiModelProperty("门店号")
    private List<String> storeCodes;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间起始 yyyy-MM-dd HH:mm:ss")
    private Date transTimeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间截至 yyyy-MM-dd HH:mm:ss")
    private Date transTimeEnd;

    @ApiModelProperty("电话号码")
    private String phone;
    @ApiModelProperty("排除的结算明细id")
    private List<Long> excludeIds;

    private Integer limit;

    private String cooperationMode;
}
