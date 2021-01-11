package com.welfare.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.mapper.AccountTypeMapper;
import com.welfare.service.AccountService;
import com.welfare.service.AccountTypeService;
import com.welfare.service.dto.AccountUploadDTO;
import java.sql.Wrapper;
import java.util.LinkedList;
import java.util.List;
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
public class AccountUploadListener extends AnalysisEventListener<AccountUploadDTO> {

  private List<Account> accountUploadList = new LinkedList<Account>();

  private AccountTypeMapper accountTypeMapper;

  private AccountService accountService;

  private static StringBuilder  uploadInfo = new StringBuilder();

  public AccountUploadListener(AccountTypeMapper accountTypeMapper,
      AccountService accountService) {
    this.accountTypeMapper = accountTypeMapper;
    this.accountService = accountService;
  }

  @Override
  public void invoke(AccountUploadDTO accountUploadDTO, AnalysisContext analysisContext) {
    QueryWrapper<AccountType> wrapper = new QueryWrapper<AccountType>();
    wrapper.eq(AccountType.MER_CODE,accountUploadDTO.getMerCode());
    wrapper.eq(AccountType.TYPE_CODE,accountUploadDTO.getAccountTypeCode());
    AccountType accountType = accountTypeMapper.selectOne(wrapper);
    if( null ==  accountType){
      uploadInfo.append("不存在的员工类型编码").append(accountUploadDTO.getAccountCode());
      return;
    }
    Account account = new Account();
    BeanUtils.copyProperties(accountUploadDTO,account);
    accountUploadList.add(account);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext analysisContext) {
    if(!CollectionUtils.isEmpty(accountUploadList)){
      Boolean result = accountService.batchSave(accountUploadList);
      if( result == true && StringUtils.isEmpty(uploadInfo.toString()) ){
        uploadInfo.append("导入成功");
      }
    }
  }

  public StringBuilder getUploadInfo() {
    return uploadInfo;
  }
}
