package com.welfare.persist.dao;

import com.welfare.persist.entity.Department;
import com.welfare.persist.mapper.DepartmentMapper;
import lombok.extern.slf4j.Slf4j;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Repository;

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

}