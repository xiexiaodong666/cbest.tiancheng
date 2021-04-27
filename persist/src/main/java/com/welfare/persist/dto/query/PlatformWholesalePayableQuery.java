package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 8:54 下午
 */
@Data
public class PlatformWholesalePayableQuery {
    @ApiModelProperty("供应商商户名称")
    String supplierMerName;
    @ApiParam("客户商户编码")
    String customerMerCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间起始 yyyy-MM-dd HH:mm:ss")
    Date transTimeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间截至 yyyy-MM-dd HH:mm:ss")
    Date transTimeEnd;

}
