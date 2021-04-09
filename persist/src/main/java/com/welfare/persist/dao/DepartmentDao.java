package com.welfare.persist.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.welfare.persist.entity.Department;
import com.welfare.persist.mapper.DepartmentMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 商户部门(department)数据DAO
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@Repository
public class DepartmentDao extends ServiceImpl<DepartmentMapper, Department> {

  public Map<String, Department> mapByDepartmentCodes(Set<String> departmentCodes) {
    Map<String, Department> map = new HashMap<>();
    if (CollectionUtils.isNotEmpty(departmentCodes)) {
      QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
      queryWrapper.in(Department.DEPARTMENT_CODE, departmentCodes);
      List<Department> list = list(queryWrapper);
      if (CollectionUtils.isNotEmpty(list)) {
        map = list.stream().collect(Collectors.toMap(Department::getDepartmentCode, a -> a,(k1, k2)->k1));
      }
    }
    return map;
  }

  public Department queryByCode(String departmentCode) {
    QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(Department.DEPARTMENT_CODE,departmentCode);
    return getOne(queryWrapper);
  }

  public Map<String, Department> mapByMerCodeAndDepartmentCodes(String merCode, Set<String> departmentCodes) {
    Map<String, Department> map = new HashMap<>();
    if (CollectionUtils.isNotEmpty(departmentCodes)) {
      QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
      queryWrapper.in(Department.DEPARTMENT_CODE, departmentCodes);
      queryWrapper.eq(Department.MER_CODE, merCode);
      List<Department> list = list(queryWrapper);
      if (CollectionUtils.isNotEmpty(list)) {
        map = list.stream().collect(Collectors.toMap(Department::getDepartmentCode, a -> a,(k1, k2)->k1));
      }
    }
    return map;
  }
}