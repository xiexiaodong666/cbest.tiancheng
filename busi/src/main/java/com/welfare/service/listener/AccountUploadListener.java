package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.util.AccountUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import com.welfare.service.*;
import com.welfare.service.dto.AccountUploadDTO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.LinkedList;
import java.util.List;

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
    Boolean validate = validationAccount(account);
    if (validate.booleanValue() == true) {
      Long accounCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());
      account.setAccountCode(accounCode);
      account.setSurplusQuota(BigDecimal.ZERO);
      account.setAccountBalance(BigDecimal.ZERO);
      account.setSurplusQuotaOverpay(BigDecimal.ZERO);
      account.setMaxQuota(BigDecimal.ZERO);
      account.setCredit(false);
      accountUploadList.add(account);
    }
  }

  private boolean validationAccount(Account account) {
    String merCode = MerchantUserHolder.getMerchantUser().getMerchantCode();
    if (!merCode.equals(account.getMerCode())) {
      uploadInfo.append("商户编码不合法:").append(account.getMerCode()).append(";");
      return false;
    }
    if (StringUtils.isEmpty(account.getPhone())
            || !AccountUtil.isNumeric(account.getPhone())
            || account.getPhone().length() != 11) {
      uploadInfo.append("手机号码不合法:").append(account.getPhone()).append(";");
      return false;
    }
    if (!AccountStatus.validStatus(account.getAccountStatus())) {
      uploadInfo.append("员工状态不合法:").append(account.getAccountStatus()).append(";");
      return false;
    }

    Merchant merchant = merchantService.detailByMerCode(account.getMerCode());
    if (null == merchant) {
      uploadInfo.append("不存在的商户:").append(account.getMerCode()).append(";");
      return false;
    }
    AccountType accountType = accountTypeService
        .queryByTypeCode(account.getMerCode(), account.getAccountTypeCode());
    if (null == accountType) {
      uploadInfo.append("不存在的员工类型编码:").append(account.getAccountTypeCode()).append(";");
      return false;
    }
    Department department = departmentService
        .getByDepartmentCodeAndMerCode(account.getDepartment(), merCode);
    if (null == department) {
      uploadInfo.append("不存在的员工部门:").append(account.getDepartment()).append(";");
      return false;
    }

    Account queryAccount = accountService.findByPhone(account.getPhone(), account.getMerCode());
    if (null != queryAccount) {
      uploadInfo.append("员工手机号已经存在:").append(account.getPhone()).append(";");
      return false;
    }
    return true;
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if (!rowHeadIsOK) {
      uploadInfo.append("模板格式(表头)有误");
    }
    if (!CollectionUtils.isEmpty(accountUploadList)) {
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
