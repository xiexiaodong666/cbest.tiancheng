package com.welfare.service.utils;

import com.welfare.common.constants.AccountChangeType;
import com.welfare.persist.dto.AccountSyncDTO;
import com.welfare.persist.entity.*;
import com.welfare.service.remote.entity.EmployerDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 14:04
 */
public class AccountUtils {

  public static List<AccountSyncDTO> getSyncDTO(List<Account> accountList,
                                                Map<String, Merchant> merchantMap,
                                                Map<String, Department> departmentMap,
                                                Map<String, List<AccountType>> accountTypeMap){
    if( CollectionUtils.isEmpty(accountList) ){
      return null;
    }
    List<AccountSyncDTO> accountSyncDTOList = new LinkedList<>();
    accountList.forEach(account -> {
      AccountSyncDTO accountSyncDTO = new AccountSyncDTO();
      BeanUtils.copyProperties(account,accountSyncDTO);
      Long merId = merchantMap.get(account.getMerCode()).getId();
      accountSyncDTO.setMerchantId(String.valueOf(merId));
      accountSyncDTO.setDepartmentCode(account.getStoreCode());
      accountSyncDTO.setDepartmentName(departmentMap.get(account.getStoreCode()).getDepartmentName());
      List<AccountType> accountTypes = accountTypeMap.get(account.getAccountTypeCode());
      if (!CollectionUtils.isEmpty(accountTypes)) {
        if (accountTypes.size() == 1) {
          accountSyncDTO.setRoleName(accountTypes.get(0).getTypeName());
        } else {
          for (AccountType accountType : accountTypes) {
            if (accountType.getMerCode().equals(account.getMerCode())) {
              accountSyncDTO.setRoleName(accountType.getTypeName());
              break;
            }
          }
        }
       }
      accountSyncDTOList.add(accountSyncDTO);
    });
    return accountSyncDTOList;
  }

  public static List<EmployerDTO> assemableEmployerDTOList(
      List<AccountSyncDTO> accountSyncDTOList) {
    List<EmployerDTO> employerDTOList = new LinkedList<EmployerDTO>();
    if (CollectionUtils.isEmpty(accountSyncDTOList)) {
      return employerDTOList;
    }
    accountSyncDTOList.forEach(accountSyncDTO -> {
      EmployerDTO employerDTO = assemableEmployerDTO(accountSyncDTO);
      employerDTOList.add(employerDTO);
    });
    return employerDTOList;
  }

  private static EmployerDTO assemableEmployerDTO(AccountSyncDTO accountSyncDTO) {
    EmployerDTO employerDTO = new EmployerDTO();
    employerDTO.setEmployerId(String.valueOf(accountSyncDTO.getAccountCode()));
    employerDTO.setEmployerRole(accountSyncDTO.getAccountTypeCode());
    employerDTO.setPartnerCode(accountSyncDTO.getMerCode());
    //accountSyncDTO.getMerchantId()
    employerDTO.setMerchantId("0");
    employerDTO.setMobile(accountSyncDTO.getPhone());
    employerDTO.setName(accountSyncDTO.getAccountName());
    employerDTO.setRemark(accountSyncDTO.getRemark());
    employerDTO.setStatus(accountSyncDTO.getAccountStatus());
    employerDTO.setRoleName(accountSyncDTO.getRoleName());
    employerDTO.setDepartmentName(accountSyncDTO.getDepartmentName());
    employerDTO.setDepartmentCode(accountSyncDTO.getDepartmentCode());
    return employerDTO;
  }

  public static List<AccountChangeEventRecord> getEventList(List<Account> accountList,
      AccountChangeType accountChangeType) {
    if (CollectionUtils.isEmpty(accountList)) {
      return null;
    }
    List<AccountChangeEventRecord> records = new LinkedList<>();
    accountList.forEach(account -> {
      AccountChangeEventRecord accountChangeEventRecord = AccountUtils
          .assemableChangeEvent(accountChangeType, account.getAccountCode(),
              account.getCreateUser());
      records.add(accountChangeEventRecord);
    });
    return records;
  }


  public static AccountChangeEventRecord assemableChangeEvent(AccountChangeType accountChangeType,
      Long accounCode, String createUser) {
    AccountChangeEventRecord accountChangeEventRecord = new AccountChangeEventRecord();
    accountChangeEventRecord.setAccountCode(accounCode);
    accountChangeEventRecord.setChangeType(accountChangeType.getChangeType());
    accountChangeEventRecord.setChangeValue(accountChangeType.getChangeValue());
    accountChangeEventRecord.setCreateTime(new Date());
    accountChangeEventRecord.setCreateUser(createUser);
    return accountChangeEventRecord;
  }

  public static List<Map<String, Object>> getMaps(
      List<AccountChangeEventRecord> accountChangeEventRecordList) {
    List<Map<String, Object>> list = new LinkedList<>();
    accountChangeEventRecordList.forEach(accountChangeEventRecord -> {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("accountCode", accountChangeEventRecord.getAccountCode());
      map.put("changeEventId", accountChangeEventRecord.getId());
      list.add(map);
    });
    return list;
  }

}
