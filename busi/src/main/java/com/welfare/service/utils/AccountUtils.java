package com.welfare.service.utils;

import com.welfare.common.constants.AccountChangeType;
import com.welfare.persist.dto.AccountSyncDTO;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.service.remote.entity.EmployerDTO;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.util.CollectionUtils;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/12 14:04
 */
public class AccountUtils {
  public static List<EmployerDTO> assemableEmployerDTOList(List<AccountSyncDTO> accountSyncDTOList){
    List<EmployerDTO> employerDTOList = new LinkedList<EmployerDTO>();
    if(CollectionUtils.isEmpty(accountSyncDTOList)){
      return employerDTOList;
    }
    accountSyncDTOList.forEach(accountSyncDTO -> {
      EmployerDTO employerDTO = assemableEmployerDTO(accountSyncDTO);
      employerDTOList.add(employerDTO);
    });
    return employerDTOList;
  }

  private static EmployerDTO assemableEmployerDTO(AccountSyncDTO accountSyncDTO){
    EmployerDTO employerDTO = new EmployerDTO();
    employerDTO.setEmployerId(String.valueOf(accountSyncDTO.getId()));
    employerDTO.setEmployerRole(accountSyncDTO.getAccountTypeCode());
    employerDTO.setMerchantCode(accountSyncDTO.getMerCode());
    employerDTO.setMerchantId(accountSyncDTO.getMerchantId());
    employerDTO.setMobile(accountSyncDTO.getPhone());
    employerDTO.setName(accountSyncDTO.getAccountName());
    employerDTO.setRemark(accountSyncDTO.getRemark());
    employerDTO.setStatus(accountSyncDTO.getAccountStatus());
    return employerDTO;
  }

  public static AccountChangeEventRecord assemableChangeEvent(AccountChangeType accountChangeType, Long accounCode,String createUser) {
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
    List<Map<String,Object>> list = new LinkedList<>();
    accountChangeEventRecordList.forEach(accountChangeEventRecord -> {
      Map<String,Object> map = new HashMap<String,Object>();
      map.put("accountCode",accountChangeEventRecord.getAccountCode());
      map.put("changeEventId",accountChangeEventRecord.getId());
      list.add(map);
    });
    return list;
  }

}
