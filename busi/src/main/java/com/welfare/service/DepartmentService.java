package com.welfare.service;


import com.welfare.persist.entity.Department;
import com.welfare.service.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 商户部门服务接口
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
public interface DepartmentService {
    /**
     * 根据商户代码查询商户部门列表（不分页）
     * @param req
     * @return
     */
    List<Department> list(DepartmentReq req);

    /**
     * 转换树形结构
     * @param merCode
     * @return
     */
    List<DepartmentTree> tree(String merCode);

    /**
     * 查询商户详情
     * @param id
     * @return
     */
    DepartmentDTO detail(Long id);

    /**
     * 新增商户
     * @param department 实体
     */
    boolean add(DepartmentAddDTO department);

    /**
     * 修改部门
     * @param department
     * @return
     */
    boolean update(DepartmentUpdateDTO department);

    /**
     * 批量新增
     * @param list 批量实体
     */
    boolean batchAdd(List<Department> list);

    public String upload(MultipartFile multipartFile);

    /**
     * 删除子机构
     * @param departmentCode
     */
    boolean delete(String  departmentCode);

    /**
     * 根据机构代码查询机构
     * @param departmentCode
     * @return
     */
    Department getByDepartmentCode(String departmentCode);
}
