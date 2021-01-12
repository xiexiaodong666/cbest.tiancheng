package com.welfare.persist.mapper;

import org.apache.ibatis.annotations.Mapper;

/**
 * 部门 数据Mapper
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface DepartmentExMapper {
    String getMaxMerCode();
}
