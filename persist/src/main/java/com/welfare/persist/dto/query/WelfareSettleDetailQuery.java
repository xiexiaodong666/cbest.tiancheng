package com.welfare.persist.dto.query;

import com.alibaba.excel.annotation.ExcelProperty;
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
    private List<String> storeCodes;

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

    @ApiModelProperty(value = "结算状态")
    private String settleFlag;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "部门编码列表")
    private List<String> departmentCodes;

    @ApiModelProperty(value = "消费类型,online offline  线上消费 线下消费")
    private String consumeType;

    @ApiModelProperty("商户支出方式")
    private String merDeductionType;

    @ApiModelProperty("供应商编码")
    private String supplierCode;

}
