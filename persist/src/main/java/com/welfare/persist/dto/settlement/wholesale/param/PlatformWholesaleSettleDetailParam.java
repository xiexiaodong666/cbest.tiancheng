package com.welfare.persist.dto.settlement.wholesale.param;

import com.welfare.persist.dto.query.PageReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 4/27/2021
 */
@Data
@ApiModel("平台应收结算明细")
public class PlatformWholesaleSettleDetailParam  extends PageReq {
    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("订单号")
    private String orderNo;
    @ApiModelProperty("交易流水号")
    private String transNo;
    @ApiModelProperty("结算状态")
    private String settleFlag;
    @ApiModelProperty("供应商编码")
    private String supplierCode;
    @ApiModelProperty("门店号")
    private String storeCode;
    @ApiModelProperty("交易时间起始")
    private Date transTimeStart;
    @ApiModelProperty("交易时间截至")
    private Date transTimeEnd;
    @ApiModelProperty("电话号码")
    private String phone;
    @ApiModelProperty("结算单号")
    private String settleNo;
    @ApiModelProperty("排除的结算明细id")
    private List<Long> excludeIds;
}
