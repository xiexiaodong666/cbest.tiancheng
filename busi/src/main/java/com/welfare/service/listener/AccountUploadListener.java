package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountService;
import com.welfare.service.AccountTypeService;
import com.welfare.service.DepartmentService;
import com.welfare.service.MerchantService;
import com.welfare.service.SequenceService;
import com.welfare.service.dto.AccountUploadDTO;
import com.welfare.service.utils.AccountUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * @author yaoxiao
 * @version 0.0.1
 * @date 2021/1/11 11:10
 */
@Slf4j
@RequiredArgsConstructor
public class AccountUploadListener extends AnalysisEventListener<AccountUploadDTO> {

  private List<Account> accountUploadList = new LinkedList<Account>();

  private final AccountTypeService accountTypeService;

  private final AccountService accountService;

  private final MerchantService merchantService;

  private final DepartmentService departmentService;

  private final SequenceService sequenceService;

  private final AccountChangeEventRecordService accountChangeEventRecordService;

  private static StringBuilder uploadInfo = new StringBuilder();


  @Override
  public void invoke(AccountUploadDTO accountUploadDTO, AnalysisContext analysisContext) {
    Account account = new Account();
    BeanUtils.copyProperties(accountUploadDTO, account);
    Boolean validate = validationAccount(account);
    if( validate.booleanValue() == true ){
      Long accounCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());
      account.setAccountCode(accounCode);
      accountUploadList.add(account);
    }
  }

  private boolean validationAccount(Account account){
    Merchant merchant = merchantService.detailByMerCode(account.getMerCode());
    if( null == merchant ) {
      uploadInfo.append("不存在的商户:").append(account.getMerCode()).append(";");
      return false;
    }
    AccountType accountType = accountTypeService.queryByTypeCode(account.getMerCode(),account.getAccountTypeCode());
    if( null == accountType ){
      uploadInfo.append("不存在的员工类型编码:").append(account.getAccountCode()).append(";");
      return false;
    }
    Department department = departmentService.getByDepartmentCode(account.getStoreCode());
    if( null ==  department){
      uploadInfo.append("不存在的员工部门::").append(account.getStoreCode()).append(";");
      return false;
    }
    Account queryAccount = accountService.findByPhone(account.getPhone());
    if( null != queryAccount ){
      uploadInfo.append("员工手机号已经存在:").append(account.getAccountCode()).append(";");
      return false;
    }
    return true;
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if (!CollectionUtils.isEmpty(accountUploadList)) {
      Boolean result = accountService.batchSave(accountUploadList);
      if (result == true && StringUtils.isEmpty(uploadInfo.toString())) {
        List<AccountChangeEventRecord> recordList = getEventList(accountUploadList);
        accountChangeEventRecordService.batchSave(recordList, AccountChangeType.ACCOUNT_TYPE_DELETE);
        //批量回写
        List<Map<String,Object>> mapList = AccountUtils.getMaps(recordList);
        accountService.batchUpdateChangeEventId(mapList);
        uploadInfo.append("导入成功");
      }
    }
  }
  private List<AccountChangeEventRecord> getEventList(List<Account> accountList){
    if( CollectionUtils.isEmpty(accountList)){
      return null;
    }
    List<AccountChangeEventRecord> records = new LinkedList<>();
    accountList.forEach(account -> {
      AccountChangeEventRecord accountChangeEventRecord = AccountUtils.assemableChangeEvent(
          AccountChangeType.ACCOUNT_NEW, account.getAccountCode(),account.getCreateUser());
      records.add(accountChangeEventRecord);
    });
    return records;
  }


  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
