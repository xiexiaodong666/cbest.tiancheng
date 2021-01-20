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

  public static  final String success="导入成功";
  public static  final String fail="入库失败";

  private static StringBuilder uploadInfo = new StringBuilder();


  @Override
  public void invoke(DepartmentImportDTO departmentImportDTO, AnalysisContext analysisContext) {
    Department department = new Department();
    BeanUtils.copyProperties(departmentImportDTO, department);
    Integer row=analysisContext.readRowHolder().getRowIndex()+1;
    if(EmptyChecker.isEmpty(departmentImportDTO.getMerCode())){
      uploadInfo.append("第").append(row.toString()).append("行").append("商户编码不能为空").append(";");
    }
    if(EmptyChecker.isEmpty(departmentImportDTO.getDepartmentParent())){
      uploadInfo.append("第").append(row.toString()).append("行").append("上级编码不能为空").append(";");
    }
    if(EmptyChecker.isEmpty(departmentImportDTO.getDepartmentName())){
      uploadInfo.append("第").append(row.toString()).append("行").append("机构名称不能为空").append(";");
    }
    if(EmptyChecker.isEmpty(departmentImportDTO.getDepartmentType())){
      uploadInfo.append("第").append(row.toString()).append("行").append("机构类型不能为空").append(";");
    }
    String type=DepartmentTypeEnum.getTypeByExcelType(departmentImportDTO.getDepartmentType());
    if(EmptyChecker.isEmpty(type)){
      uploadInfo.append("第").append(row.toString()).append("行").append("机构类型错误").append(";");
    }else{
      department.setDepartmentType(type);
    }
    if(!department.getDepartmentParent().equals(department.getMerCode())){
      parenCodeList.add(department.getDepartmentParent());
    }
    merCodeList.add(department.getMerCode());
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
          department.setDepartmentPath(department.getMerCode()+"-"+departmentCode);
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
        department.setDepartmentCode(departmentCode);
      }

      if(flag){
      Boolean result = departmentService.batchAdd(list);
        if (result == false) {
          uploadInfo.append(fail);
        }
        if( StringUtils.isEmpty(uploadInfo.toString())) {
          uploadInfo.append(success);
        }
      }

    }
  }

  private boolean check() {
    boolean flag=true;
    if(EmptyChecker.notEmpty(parenCodeList)){
      DepartmentReq departmentReq=new DepartmentReq();
      departmentReq.setDepartmentCodeList(parenCodeList);
      List<Department> departments=departmentService.list(departmentReq);
      parenCodeList.removeAll(departments.stream().map(item->item.getDepartmentCode()).collect(Collectors.toList())) ;
      if(EmptyChecker.notEmpty(parenCodeList)){
        uploadInfo.append("上级机构不存在:").append(StringUtil.join(parenCodeList,",")).append(";");
        flag=false;
      }
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
