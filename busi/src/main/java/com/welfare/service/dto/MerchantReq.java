package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import java.util.Set;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class MerchantReq {
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("商户类型")
    private String merType;

    @ApiModelProperty("身份属性")
    private String merIdentity;

    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("合作方式")
    private String merCooperationMode;

    @Query(type = Query.Type.IN,propName = "merCode")
    @ApiModelProperty("商户code集合")
    private List<String> merCodeList;

    /**
     * 来源
     */
    @ApiModelProperty("来源")
    private String source;

    /**
     * 是否需要过滤已经有福利类型的商户
     */
    @ApiModelProperty("是否需要过滤已经有福利类型的商户")
    private boolean merAccountTypeFlag;

}
