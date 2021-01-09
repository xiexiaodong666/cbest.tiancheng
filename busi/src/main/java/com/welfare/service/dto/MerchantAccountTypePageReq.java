package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class MerchantAccountTypePageReq {
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("商户代码")
    private String merCode;

    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty("商户名称")
    private String merName;


    @Query(type = Query.Type.GREATER_THAN,propName = "createTime")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @Query(type = Query.Type.LESS_THAN,propName = "createTime")
    @ApiModelProperty("结束时间")
    private Date endTime;
}
