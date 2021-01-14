package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.persist.dto.AccountIncrementDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.dto.AccountSyncDTO;
import com.welfare.persist.entity.Account;
import com.welfare.service.dto.AccountBillDTO;
import com.welfare.service.dto.AccountBillDetailDTO;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountDetailDTO;
import com.welfare.service.dto.AccountIncrementReq;
import com.welfare.service.dto.AccountPageReq;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

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

  Boolean active(Long id, Integer active);

  AccountDetailDTO queryDetail(Long id);

  Boolean save(Account account);

  Boolean batchSave(List<Account> accountList);

  Boolean update(Account account);

  Page<AccountBillDetailDTO> queryAccountBillDetail(Integer currentPage, Integer pageSize,
      String accountCode, Date createTimeStart, Date createTimeEnd);

  List<AccountBillDetailDTO> exportBillDetail(String accountCode, Date createTimeStart,
      Date createTimeEnd);

  AccountBillDTO quertBill(String accountCode, Date createTimeStart, Date createTimeEnd);

  List<Long> getAccountCodeList(List<Long> accountCodes);

  public void syncAccount(ShoppingActionTypeEnum actionTypeEnum, List<AccountSyncDTO> accountSyncDTOS);
}
