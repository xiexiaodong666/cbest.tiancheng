package com.welfare.persist.dto.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 8:27 下午
 */
@Data
public class WholesaleReceiveSettleDetailPageQuery  extends  PageReq{
    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编号")
    private List<String> storeCodes;

    @ApiModelProperty(value = "起始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty(value = "福利类型")
    private String welfareTypeCode;

    @ApiModelProperty(value = "门店类型 自营：self，第三方：third")
    private String storeType;

    @ApiModelProperty(value = "消费流水号")
    private String transNo;


    @ApiModelProperty("电话号码")
    private String phone;

    @ApiModelProperty("部门编码")
    private List<String> departmentCodes;

    @ApiModelProperty("消费类型")
    private String consumeType;

    @ApiModelProperty("结算状态")
    private String settleFlag;

    @ApiModelProperty("商户支出方式")
    private String merDeductionType;

    @ApiModelProperty("供应商")
    private String supplierCode;

    @ApiModelProperty("支付渠道")
    private String paymentChannel;

    @ApiModelProperty("结算编号")
    private String settleNo;
}
