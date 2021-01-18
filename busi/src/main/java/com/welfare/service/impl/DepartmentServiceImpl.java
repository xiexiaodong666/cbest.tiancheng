package com.welfare.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.util.EmptyChecker;
import com.welfare.common.util.MerchantUserHolder;
import  com.welfare.persist.dao.DepartmentDao;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.mapper.DepartmentExMapper;
import com.welfare.service.DictService;
import com.welfare.service.MerchantService;
import com.welfare.service.SequenceService;
import com.welfare.service.converter.DepartmentConverter;
import com.welfare.service.converter.DepartmentTreeConverter;
import com.welfare.service.dto.DepartmentAddDTO;
import com.welfare.service.dto.DepartmentDTO;
import com.welfare.service.dto.DepartmentImportDTO;
import com.welfare.service.dto.DepartmentReq;
import com.welfare.service.dto.DepartmentTree;
import com.welfare.service.dto.DepartmentUpdateDTO;
import com.welfare.service.helper.QueryHelper;
import com.welfare.service.listener.DepartmentListener;
import com.welfare.service.utils.TreeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.welfare.service.DepartmentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final SequenceService sequenceService;
    private final DictService dictService;
    private final DepartmentExMapper  departmentExMapper;

    @Override
    public List<Department> list(DepartmentReq req) {
        if(EmptyChecker.isEmpty(req.getMerCode())){
            req.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
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
    @Override
    public boolean add(DepartmentAddDTO department) {
        if(EmptyChecker.isEmpty(department.getDepartmentParent())){
            throw new BusiException("上级编码不能为空");
        }
        Department save=new Department();
        save.setDepartmentType(department.getDepartmentType());
        save.setDepartmentName(department.getDepartmentName());
        save.setMerCode(department.getMerCode());
        save.setDepartmentParent(department.getDepartmentParent());
        String departmentCode;
        //构建path
        departmentCode=sequenceService.nextNo(WelfareConstant.SequenceType.DEPARTMENT_CODE.code()).toString();
        if(department.getDepartmentParent().equals(department.getMerCode())){
            save.setDepartmentPath(department.getDepartmentParent()+"-"+departmentCode);
        }else{
            Department parent=this.getByDepartmentCode(department.getDepartmentParent());
            if(EmptyChecker.isEmpty(parent)){
                throw new BusiException("上级编码不存在");
            }
            save.setDepartmentPath(parent.getDepartmentPath()+"-"+departmentCode);
        }
        save.setDepartmentCode(departmentCode);
        return departmentDao.save(save);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean update(DepartmentUpdateDTO department) {
        Department update=new Department();
        update.setId(department.getId());
        update.setDepartmentName(department.getDepartmentName());
        update.setDepartmentType(department.getDepartmentType());
        return departmentDao.updateById(update);
    }

    @Transactional(rollbackFor = Exception.class)
    public String upload(MultipartFile multipartFile) {
        try {
            DepartmentListener listener = new DepartmentListener(merchantService,this,sequenceService);
            EasyExcel.read(multipartFile.getInputStream(), DepartmentImportDTO.class, listener).sheet()
                    .doRead();
            String result = listener.getUploadInfo().toString();
            listener.getUploadInfo().delete(0, listener.getUploadInfo().length());
            return result;
        } catch (Exception e) {
            log.info("批量新增部门解析 Excel exception:{}", e.getMessage());
        }
        return "解析失败";
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
        List<DepartmentTree> treeDTOList=departmentTreeConverter.toD(departmentExMapper.listUnionMerchant(merCode));
        treeDTOList.forEach(item-> {
            item.setCode(item.getDepartmentCode());
            item.setParentCode(item.getDepartmentParent());

        });
        TreeUtil treeUtil=new TreeUtil(treeDTOList,"0");
        return treeUtil.getTree();
    }

}