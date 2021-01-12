package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class DictReq {
    @Query(type = Query.Type.EQUAL)
    @NotNull
    @ApiModelProperty("码表类型")
    private String dictType;
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("码表code")
    private String dictCode;
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("状态")
    private String status;
}
