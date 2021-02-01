package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class SupplierStoreListReq {
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("商户代码")
    private String merCode;
    @Query(type = Query.Type.INNER_LIKE)
    @ApiModelProperty("门店名称")
    private String storeName;
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("门店编码")
    private String storeCode;

    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("门店状态")
    private Integer status;
    @Query(type = Query.Type.IN,propName ="storeCode" )
    @ApiModelProperty("门店code集合")
    private List<String> storeCodeList;

    @ApiModelProperty("来源")
    private String source;
}
