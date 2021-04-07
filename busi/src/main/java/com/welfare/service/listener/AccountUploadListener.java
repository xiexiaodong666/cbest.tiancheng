package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizException;
import com.welfare.common.util.AccountUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.AccountTypeDao;
import com.welfare.persist.dao.DepartmentDao;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.*;
import com.welfare.service.dto.AccountUploadDTO;
import com.welfare.service.dto.BatchSequence;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

  private static StringBuilder uploadInfo = new StringBuilder();

  private boolean rowHeadIsOK = true;

  private final AccountTypeDao accountTypeDao;

  private final DepartmentDao departmentDao;

  private final AccountDao accountDao;


  @Override
  public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
    if (!headMap.get(0).equals("商户编码") ||
        !headMap.get(1).equals("员工名称") ||
        !headMap.get(2).equals("手机号") ||
        !headMap.get(3).equals("账号状态(1正常2锁定)") ||
        !headMap.get(4).equals("员工类型编码") ||
        !headMap.get(5).equals("所属部门(代码)")) {
      rowHeadIsOK = false;
      return;
    }
  }

  @Override
  public void invoke(AccountUploadDTO accountUploadDTO, AnalysisContext analysisContext) {
    if (!rowHeadIsOK) {
      return;
    }
    Account account = new Account();
    account.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
    BeanUtils.copyProperties(accountUploadDTO, account);
    account.setDepartment(accountUploadDTO.getStoreCode());
    account.setSurplusQuota(BigDecimal.ZERO);
    account.setAccountBalance(BigDecimal.ZERO);
    account.setSurplusQuotaOverpay(BigDecimal.ZERO);
    account.setMaxQuota(BigDecimal.ZERO);
    account.setCredit(false);
    account.setDepartment(accountUploadDTO.getStoreCode());
    accountUploadList.add(account);
  }

  private void validationAccount(Account account, Merchant merchant, Map<String, AccountType> accountTypeMap,
                                    Map<String, Department> departmentMap, Map<String, Account> accountMap) {
    String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
    if (!merCode.equals(account.getMerCode())) {
      //uploadInfo.append("商户编码不合法:").append(account.getMerCode()).append(";");
      throw new BizException(String.format("商户编码不合法:%s", account.getMerCode()));
    }
    if (StringUtils.isEmpty(account.getPhone())) {
      throw new BizException(String.format("手机号码不合法:%s", account.getPhone()));
    }
    if (!AccountStatus.validStatus(account.getAccountStatus())) {
      //uploadInfo.append("员工状态不合法:").append(account.getAccountStatus()).append(";");
      throw new BizException(String.format("员工状态不合法:%s", account.getAccountStatus()));
    }

    if (Objects.isNull(merchant)) {
     // uploadInfo.append("不存在的商户:").append(account.getMerCode()).append(";");
      throw new BizException(String.format("不存在的商户:%s", merCode));
    }

    if (!merchant.getMerCode().equals(account.getMerCode())) {
      //uploadInfo.append("不存在的员工商户:").append(account.getMerCode()).append(";");
      throw new BizException(String.format("不存在的员工商户:%s", account.getMerCode()));
    }

    if (!accountTypeMap.containsKey(account.getAccountTypeCode())) {
      //uploadInfo.append("不存在的员工类型编码:").append(account.getAccountTypeCode()).append(";");
      throw new BizException(String.format("不存在的员工类型编码:%s", account.getAccountTypeCode()));
    }

    if (!departmentMap.containsKey(account.getDepartment())) {
      //uploadInfo.append("不存在的员工部门:").append(account.getDepartment()).append(";");
      throw new BizException(String.format("不存在的员工部门:%s", account.getDepartment()));
    }

    if (accountMap.containsKey(account.getPhone())) {
      //uploadInfo.append("员工手机号已经存在:").append(account.getPhone()).append(";");
      throw new BizException(String.format("员工手机号已经存在:%s", account.getPhone()));
    }
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if (!rowHeadIsOK) {
      uploadInfo.append("模板格式(表头)有误");
    }
    if (!CollectionUtils.isEmpty(accountUploadList)) {

      String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
      Set<String> accountTypeCodes = accountUploadList.stream().map(Account::getAccountTypeCode).collect(Collectors.toSet());
      Set<String> departmentCodes = accountUploadList.stream().map(Account::getDepartment).collect(Collectors.toSet());
      Set<String> phones = accountUploadList.stream().map(Account::getPhone).collect(Collectors.toSet());

      Merchant merchant = merchantService.getMerchantByMerCode(merCode);
      Map<String, AccountType> accountTypeMap = accountTypeDao.mapByMerCodeAndCodes(merCode, accountTypeCodes);
      Map<String, Department> departmentMap = departmentDao.mapByMerCodeAndDepartmentCodes(merCode, departmentCodes);
      Map<String, Account> accountMap = accountDao.mapByMerCodeAndPhones(merCode, phones);

      accountUploadList.forEach(account -> {
        validationAccount(account, merchant, accountTypeMap, departmentMap, accountMap);
      });

      BatchSequence batchGenerate = sequenceService.batchGenerate(WelfareConstant.SequenceType.ACCOUNT_CODE.code(), accountUploadList.size());
      for (int i = 0; i < accountUploadList.size(); i++) {
        Account account = accountUploadList.get(i);
        account.setAccountCode(batchGenerate.getSequences().get(i).getSequenceNo());
      }

      //数据以500等分分批同步
      List<List<Account>> averageAccountList = AccountUtil.averageAssign(accountUploadList);
      for (List<Account> accountList : averageAccountList) {
        accountService.batchUpload(accountList);
      }
      if (StringUtils.isEmpty(uploadInfo.toString())) {
        uploadInfo.append("导入成功");
      }
    }
  }

  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
