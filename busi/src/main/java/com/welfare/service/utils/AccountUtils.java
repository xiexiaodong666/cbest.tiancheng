package com.welfare.service.utils;

import com.welfare.persist.dto.AccountSyncDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.remote.entity.EmployerDTO;
import java.util.LinkedList;
import java.util.List;
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
}
