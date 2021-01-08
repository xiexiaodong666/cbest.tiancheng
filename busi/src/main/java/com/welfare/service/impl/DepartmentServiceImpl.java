package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.MerchantUserHolder;
import  com.welfare.persist.dao.DepartmentDao;
import com.welfare.persist.entity.Department;
import com.welfare.service.dto.DepartmentReq;
import com.welfare.service.helper.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 商户部门服务接口实现
 *
 * @author Yuxiang Li
 * @since 2021-01-06 13:49:25
 * @description 由 Mybatisplus Code Generator 创建
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentDao departmentDao;

    @Override
    public List<Department> list(DepartmentReq req) {
        if(EmptyChecker.isEmpty(req.getMerCode())){
            req.setMerCode(MerchantUserHolder.getDeptIds().getMerchantCode());
        }
        return departmentDao.list(QueryHelper.getWrapper(req));
    }

    @Override
    public Department detail(Long id) {
        return departmentDao.getById(id);
    }

    @Override
    public boolean add(Department department) {
        return departmentDao.save(department);
    }

    @Override
    public boolean batchAdd(List<Department> list) {
        return departmentDao.saveBatch(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String  departmentCode) {
        QueryWrapper<Department> queryWrapper=new QueryWrapper();
        queryWrapper.eq("department_code",departmentCode);
        //删除子级
        QueryWrapper<Department> queryWrapper2=new QueryWrapper();
        queryWrapper.eq("department_parent",departmentCode);
        return departmentDao.remove(queryWrapper)&&departmentDao.remove(queryWrapper2);
    }
}