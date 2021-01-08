package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class MerchantReq {
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("商户类型")
    private String merType;

    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("身份属性")
    private String merIdentity;

    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("合作方式")
    private String merCooperationMode;
}
