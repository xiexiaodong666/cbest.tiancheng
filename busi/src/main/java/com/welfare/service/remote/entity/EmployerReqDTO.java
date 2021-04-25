package com.welfare.service.remote.entity;

import com.google.common.collect.Lists;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Department;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 10:57
 */
@Data
public class EmployerReqDTO {
  private ShoppingActionTypeEnum actionType;
  private List<EmployerDTO> list;
  private String requestId;
  private Date timestamp;

  public static EmployerReqDTO of(ShoppingActionTypeEnum actionType, Account account, AccountType accountType, Department department) {
    EmployerDTO employerDTO = new EmployerDTO();
    employerDTO.setEmployerId(String.valueOf(account.getAccountCode()));
    employerDTO.setEmployerRole(account.getAccountTypeCode());
    employerDTO.setPartnerCode(account.getMerCode());
    employerDTO.setMerchantId("0");
    employerDTO.setMobile(account.getPhone());
    employerDTO.setName(account.getAccountName());
    employerDTO.setRemark(account.getRemark());
    employerDTO.setStatus(account.getAccountStatus());
    employerDTO.setRoleName(accountType.getTypeName());
    employerDTO.setDepartmentName(department.getDepartmentName());
    employerDTO.setDepartmentCode(account.getDepartment());
    EmployerReqDTO reqDTO = new EmployerReqDTO();
    reqDTO.setActionType(actionType);
    reqDTO.setList(Lists.newArrayList(employerDTO));
    reqDTO.setRequestId(UUID.randomUUID().toString());
    reqDTO.setTimestamp(new Date());
    return reqDTO;
  }
}
