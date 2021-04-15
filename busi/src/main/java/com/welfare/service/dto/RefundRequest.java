package com.welfare.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Data
@ApiModel("退款请求")
public class RefundRequest {
    @ApiModelProperty("请求id")
    private String requestId;
    @ApiModelProperty("重百付流水号")
    private String transNo;
    @ApiModelProperty("重百付正向支付流水号")
    private String originalTransNo;
    @ApiModelProperty("退款金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "退款处理状态,1:新增, 2:处理中, 3:处理成功 -1:处理失败")
    private Integer refundStatus;
    @ApiModelProperty("退款日期")
    private Date refundDate;
    @ApiModelProperty("账户编码(返回参数)")
    private Long accountCode;
    @ApiModelProperty("账户姓名(返回参数)")
    private String accountName;
    @ApiModelProperty("账户电话(返回参数)")
    private String phone;
    @ApiModelProperty("账户余额(返回参数)")
    private BigDecimal accountBalance;
    @ApiModelProperty("账户信用额度(返回参数)")
    private BigDecimal accountCredit;
    @ApiModelProperty("商户编码")
    private String merCode;
    @ApiModelProperty("账户所在商家，返回参数")
    private String accountMerCode;
    @ApiModelProperty("沃生活馆线上退款请求商品行数据")
    private List<String> saleUnIds;

}
