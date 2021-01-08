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
public class DepartmentReq {
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("商户代码")
    private String merCode;
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("部门父级")
    private String departmentParent;
    @Query(type = Query.Type.EQUAL)
    @ApiModelProperty("部门层级")
    private String departmentLevel;
}
