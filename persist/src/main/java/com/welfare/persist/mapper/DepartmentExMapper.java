package com.welfare.persist.mapper;

import com.welfare.persist.dto.DepartmentUnionMerchant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 部门 数据Mapper
 *
 * @author hao.yin
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
*/
@Mapper
public interface DepartmentExMapper {
    List<DepartmentUnionMerchant> listUnionMerchant(@Param("merCode") String merCode);

}
