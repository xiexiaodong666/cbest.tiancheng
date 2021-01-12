package com.welfare.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.DepartmentConstant;
import com.welfare.common.constants.MerchantConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.MerchantUserHolder;
import  com.welfare.persist.dao.DepartmentDao;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.mapper.DepartmentExMapper;
import com.welfare.service.DictService;
import com.welfare.service.MerchantService;
import com.welfare.service.converter.DepartmentConverter;
import com.welfare.service.converter.DepartmentTreeConverter;
import com.welfare.service.dto.DepartmentDTO;
import com.welfare.service.dto.DepartmentReq;
import com.welfare.service.dto.DepartmentTree;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final DepartmentTreeConverter departmentTreeConverter;
    private final MerchantService merchantService;
    private final DepartmentConverter departmentConverter;
    private final DepartmentExMapper departmentExMapper;
    private final DictService dictService;

    @Override
    public List<Department> list(DepartmentReq req) {
        if(EmptyChecker.isEmpty(req.getMerCode())){
            req.setMerCode(MerchantUserHolder.getDeptIds().getMerchantCode());
        }
        return departmentDao.list(QueryHelper.getWrapper(req));
    }

    @Override
    public DepartmentDTO detail(Long id) {
        DepartmentDTO department=departmentConverter.toD(departmentDao.getById(id));
        if(EmptyChecker.isEmpty(department)){
            throw new BusiException("部门不存在");
        }
        //顶级部门的父级为机构
        if(EmptyChecker.isEmpty(department.getDepartmentParent())||"0".equals(department.getDepartmentParent())){
            Merchant merchant=merchantService.getMerchantByMerCode(department.getMerCode());
            department.setDepartmentParentName(EmptyChecker.isEmpty(merchant)?"":merchant.getMerName());
        }else {
            Department department1=this.getByDepartmentCode(department.getDepartmentParent());
            department.setDepartmentParentName(EmptyChecker.isEmpty(department1)?"":department1.getDepartmentName());
        }
        dictService.trans(DepartmentDTO.class,Department.class.getSimpleName(),true,department);
        return department;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean add(Department department) {
        if(EmptyChecker.isEmpty(department.getDepartmentParent())){
            department.setDepartmentParent("0");
        }
        department.setDepartmentCode(getNextCode());
        return departmentDao.save(department);
    }
    private String getNextCode(){
        String maxCode=departmentExMapper.getMaxMerCode();
        if(EmptyChecker.isEmpty(maxCode)){
            return DepartmentConstant.INIT_DEPARTMENT_CODE;
        }
        if(DepartmentConstant.MAX_DEPARTMENT_CODE.equals(maxCode)){
            throw new BusiException("已达最大部门编码，请联系管理员");
        }
        return ""+(Integer.parseInt(maxCode)+1);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean update(Department department) {
        Department update=new Department();
        update.setId(department.getId());
        update.setDepartmentName(department.getDepartmentName());
        update.setDepartmentType(department.getDepartmentType());
        return departmentDao.updateById(update);
    }

    @Transactional(rollbackFor = Exception.class)
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

    @Override
    public Department getByDepartmentCode(String departmentCode) {
        QueryWrapper<Department> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Department.DEPARTMENT_CODE, departmentCode);
        return departmentDao.getOne(queryWrapper);
    }

    @Override
    public List<DepartmentTree> tree(String merCode) {
        DepartmentReq req=new DepartmentReq();
        req.setMerCode(merCode);
        List<DepartmentTree> treeDTOList=departmentTreeConverter.toD(this.list(req));
        treeDTOList.forEach(item-> {
            item.setCode(item.getDepartmentCode());
            item.setParentCode(item.getDepartmentParent());

        });
        TreeUtil treeUtil=new TreeUtil(treeDTOList,"0");
        return treeUtil.getTree();
    }

}