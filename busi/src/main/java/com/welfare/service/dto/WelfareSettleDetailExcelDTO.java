package com.welfare.service.dto;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/21 4:41 下午
 */
@Data
@ExcelIgnoreUnannotated
public class WelfareSettleDetailExcelDTO {

    @ExcelProperty(value = "交易流水号")
    @ApiModelProperty(value = "交易流水号")
    private String transNo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = "交易时间")
    @ApiModelProperty(value = "交易时间")
    private Date transTime;

    @ExcelProperty(value = "场店编码")
    @ApiModelProperty(value = "场店编码")
    private String storeCode;

    @ExcelProperty(value = "场店名称")
    @ApiModelProperty(value = "场店名称")
    private String storeName;

    @ExcelProperty(value = "商户编号")
    @ApiModelProperty(value = "商户编号")
    private String merCode;

    @ExcelProperty(value = "商户名称")
    @ApiModelProperty(value = "商户名称")
    private String merName;

    @ExcelProperty(value = "门店类型")
    @ApiModelProperty(value = "门店类型")
    private String type;

    @ExcelProperty(value = "福利类型")
    @ApiModelProperty(value = "福利类型")
    private String welfareTypeName;

    @ExcelProperty(value = "交易金额")
    @ApiModelProperty(value = "交易金额")
    private String payAmount;

    @ApiModelProperty("商户授信支出")
    @ExcelProperty(value = "商户授信支出")
    private BigDecimal merDeductionCreditAmount;

    @ApiModelProperty("商户余额支出")
    @ExcelProperty(value = "商户余额支出")
    private BigDecimal merDeductionAmount;


    public static List<WelfareSettleDetailExcelDTO> of(List<WelfareSettleDetailResp> resps) {
        List<WelfareSettleDetailExcelDTO> detailExcelDTOS = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(resps)) {
            resps.forEach(welfareSettleDetailResp -> {
                WelfareSettleDetailExcelDTO dto = new WelfareSettleDetailExcelDTO();
                dto.setTransNo(welfareSettleDetailResp.getTransNo());
                dto.setTransTime(welfareSettleDetailResp.getTransTime());
                dto.setStoreCode(welfareSettleDetailResp.getStoreCode());
                dto.setStoreName(welfareSettleDetailResp.getStoreName());
                dto.setMerCode(welfareSettleDetailResp.getMerCode());
                dto.setMerName(welfareSettleDetailResp.getMerName());
                dto.setType(welfareSettleDetailResp.getType());
                dto.setWelfareTypeName(welfareSettleDetailResp.getWelfareTypeName());
                dto.setPayAmount(welfareSettleDetailResp.getPayAmount());
                dto.setMerDeductionCreditAmount(welfareSettleDetailResp.getMerDeductionCreditAmount());
                dto.setMerDeductionAmount(welfareSettleDetailResp.getMerDeductionAmount());
                detailExcelDTOS.add(dto);
            });

        }
        return detailExcelDTOS;
    }
}
