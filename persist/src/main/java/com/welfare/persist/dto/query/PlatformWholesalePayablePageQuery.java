package com.welfare.persist.dto.query;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/27 8:50 下午
 */
@Data
public class PlatformWholesalePayablePageQuery extends PageReq{

    @ApiModelProperty("供应商商户名称")
    private String supplierMerName;
    @ApiParam("客户商户编码")
    private String customerMerCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间起始 yyyy-MM-dd HH:mm:ss")
    private Date transTimeStart;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("交易时间截至 yyyy-MM-dd HH:mm:ss")
    private Date transTimeEnd;
    @ApiModelProperty("合作方式(joint_venture:联营 distribution:经销)")
    private String cooperationMode;


}
