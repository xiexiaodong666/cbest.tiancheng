package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.DepartmentTypeEnum;
import com.welfare.common.util.EmptyChecker;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.DepartmentService;
import com.welfare.service.MerchantService;
import com.welfare.service.SequenceService;
import com.welfare.service.dto.DepartmentImportDTO;
import com.welfare.service.dto.DepartmentReq;
import com.welfare.service.dto.MerchantReq;
import jodd.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hao.yin
 * @version 0.0.1
 * @date 2021/1/11 11:10
 */
@Slf4j
@RequiredArgsConstructor
public class DepartmentListener extends AnalysisEventListener<DepartmentImportDTO> {

  private List<Department> list = new LinkedList<Department>();

  private List<String> merCodeList = new LinkedList();
  private List<String> parenCodeList = new LinkedList();

  private final MerchantService merchantService;

  private final DepartmentService departmentService;
  private final SequenceService sequenceService;

  private static StringBuilder uploadInfo = new StringBuilder();


  @Override
  public void invoke(DepartmentImportDTO departmentImportDTO, AnalysisContext analysisContext) {
    Department department = new Department();
    BeanUtils.copyProperties(departmentImportDTO, department);
    department.setDepartmentType(DepartmentTypeEnum.getTypeByExcelType(departmentImportDTO.getDepartmentType()));
    merCodeList.add(department.getMerCode());
    if(department.getDepartmentParent().equals(department.getMerCode())){
      parenCodeList.add(department.getDepartmentParent());
    }
    list.add(department);
  }


  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if (!CollectionUtils.isEmpty(list)) {
      boolean flag = check();
      for(Department department:list){
        String departmentCode=sequenceService.nextNo(WelfareConstant.SequenceType.DEPARTMENT_CODE.code()).toString();
        Map<String,String> pathMap=new HashMap<>();
        if(department.getDepartmentParent().equals(department.getMerCode())){
          department.setDepartmentPath(departmentCode);
        }else{
          if(EmptyChecker.isEmpty(pathMap.get(department.getDepartmentParent()))){
            Department parent=departmentService.getByDepartmentCode(department.getDepartmentParent());
            if(EmptyChecker.notEmpty(parent)){
              pathMap.put(department.getDepartmentParent(),parent.getDepartmentPath());
              department.setDepartmentPath(parent.getDepartmentPath()+"-"+department.getDepartmentCode());
            }
          }else{
            department.setDepartmentPath(pathMap.get(department.getDepartmentParent())+"-"+department.getDepartmentCode());
          }
        }
      }

      if(flag){
      Boolean result = departmentService.batchAdd(list);
        if (result == false) {
          uploadInfo.append("入库失败");
        }
        if( StringUtils.isEmpty(uploadInfo.toString())) {
          uploadInfo.append("导入成功");
        }
      }

    }
  }

  private boolean check() {
    DepartmentReq departmentReq=new DepartmentReq();
    departmentReq.setDepartmentCodeList(parenCodeList);
    List<Department> departments=departmentService.list(departmentReq);
    parenCodeList.removeAll(departments.stream().map(item->item.getDepartmentCode()).collect(Collectors.toList())) ;
    boolean flag=true;
    if(EmptyChecker.notEmpty(parenCodeList)){
      uploadInfo.append("上级机构不存在:").append(StringUtil.join(parenCodeList,",")).append(";");
      flag=false;
    }
    MerchantReq req=new MerchantReq() ;
    req.setMerCodeList(merCodeList);
    List<Merchant> merchants=merchantService.list(req);
    merCodeList.removeAll(merchants.stream().map(item->item.getMerCode()).collect(Collectors.toList())) ;
    if(EmptyChecker.notEmpty(merCodeList)){
      uploadInfo.append("商户不存在:").append(StringUtil.join(merCodeList,",")).append(";");
      flag=false;
    }
    return flag;
  }

  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
