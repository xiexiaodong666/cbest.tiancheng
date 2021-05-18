package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 3:29 下午
 */
@Data
public class WholesalePayableSummaryQueryDTO extends PageReq{

    @ApiModelProperty("商户编码")
    private String merCode;

    @ApiModelProperty("客户商户编码")
    private String customerMerCode;

    @ApiModelProperty("合作方式")
    private String wholesaleCooperationMode;

    @ApiModelProperty("消费起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transTimeStart;

    @ApiModelProperty("消费结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date transTimeEnd;
}
