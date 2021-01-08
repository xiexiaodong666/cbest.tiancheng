package com.welfare.service.dto;

import com.welfare.common.annotation.Query;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by hao.yin on 2021/1/8.
 */
@Data
@NoArgsConstructor
public class DepartmentReq {
    @Query(type = Query.Type.EQUAL)
    private String merCode;
    @Query(type = Query.Type.EQUAL)
    private String departmentParent;
    @Query(type = Query.Type.EQUAL)
    private String departmentLevel;
}
