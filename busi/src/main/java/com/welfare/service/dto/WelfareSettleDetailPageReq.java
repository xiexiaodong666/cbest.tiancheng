package com.welfare.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.welfare.service.utils.PageReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author qiang.deng
 * @version 1.0.0
 * @date 2021/1/7 4:48 下午
 * @desc 账单明细响应dto
 */
@Data
public class WelfareSettleDetailPageReq  extends PageReq {
    @ApiModelProperty(value = "商户编号", required = true)
    private String merCode;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value = "门店名称")
    private String storeName;

    @ApiModelProperty(value = "门店编号")
    private List<String> storeCodes;

    @ApiModelProperty(value = "供应商编码")
    private String supplierCode;

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

    @ApiModelProperty(value = "需要剔除的id列表")
    private List<Long> excludeIdList;

    @ApiModelProperty(value = "结算状态")
    private String settleFlag;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "部门编码列表")
    private List<String> departmentCodes;
}
