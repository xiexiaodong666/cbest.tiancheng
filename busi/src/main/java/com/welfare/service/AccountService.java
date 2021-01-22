package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.persist.dto.AccountIncrementDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.dto.AccountSimpleDTO;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 账户信息服务接口
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
public interface AccountService {

  Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page,
      AccountPageReq accountPageReq);

  List<AccountIncrementDTO> queryIncrementDTO(AccountIncrementReq accountIncrementReq);

  Account findByPhone(String phone);

  List<AccountDTO> export(AccountPageReq accountPageReq);

  String uploadAccount(MultipartFile multipartFile);

  String accountBatchBindCard(MultipartFile multipartFile);

  /**
   * 增加员工账号余额
   */
  int increaseAccountBalance(BigDecimal increaseBalance, String updateUser, String accountCode);

  Account getByAccountCode(Long accountCode);

  Boolean delete(Long id);

  Boolean active(Long id, Integer accountStatus);

  AccountDetailDTO queryDetail(Long id);

  AccountDetailDTO queryDetailByAccountCode(String accountCode);

  AccountDetailDTO queryDetailByParam(AccountDetailParam accountDetailParam);

  Boolean save(AccountReq accountReq);

  Boolean batchSave(List<Account> accountList);

  Boolean update(AccountReq accountReq);

  Page<AccountBillDetailDTO> queryAccountBillDetail(Integer currentPage, Integer pageSize,
      String accountCode, Date createTimeStart, Date createTimeEnd);

  List<AccountBillDetailDTO> exportBillDetail(String accountCode, Date createTimeStart,
      Date createTimeEnd);

  AccountBillDTO quertBill(String accountCode, Date createTimeStart, Date createTimeEnd);

  List<Long> getAccountCodeList(List<Long> accountCodes);

  AccountSimpleDTO queryAccountInfo(Long accountCode);
  void batchUpdateChangeEventId(List<Map<String,Object>> list);
  List<Account> queryByAccountTypeCode(String accountTypeCode);
  List<Account> queryAccountByConsumeSceneId(List<Long> consumeSceneId);

  boolean bindingCard(String accountCode,String cardId);
  Account findByPhoneAndMerCode(String phone,String merCode);
  void batchBindCard(List<CardInfo> cardInfoList,List<Account> accountList);
  void batchUpload(List<Account> accountList);
  AccountDetailDTO queryDetailPhoneAndMer(String phone);
}
