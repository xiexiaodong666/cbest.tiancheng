package com.welfare.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dto.*;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.CardInfo;
import com.welfare.service.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

  AccountPageExtDTO getPageExtDTO(AccountPageReq accountPageReq);

  List<AccountIncrementDTO> queryIncrementDTO(AccountIncrementReq accountIncrementReq);

  Account findByPhone(String phone,String merCode);

  List<AccountDTO> export(AccountPageReq accountPageReq);

  String uploadAccount(MultipartFile multipartFile)throws IOException;

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

  void batchSyncData(Integer staffStatus);

  AccountBatchImgDTO uploadBatchImg(AccountBatchImgReq accountBatchImgReq);

  Boolean batchSave(List<Account> accountList);

  Boolean update(AccountReq accountReq);

  Page<AccountBillDetailDTO> queryAccountBillDetail(Integer currentPage, Integer pageSize,
      String accountCode, Date createTimeStart, Date createTimeEnd);

  List<AccountBillDetailDTO> exportBillDetail(String accountCode, Date createTimeStart,
      Date createTimeEnd);

  AccountBillDTO quertBill(String accountCode, Date createTimeStart, Date createTimeEnd);

  List<Long> getAccountCodeList(List<Long> accountCodes);

  AccountSimpleDTO queryAccountInfo(Long accountCode);

  AccountOverviewDTO queryAccountOverview(Long accountCode, String paymentChannel);

  List<AccountPaymentChannelDTO> queryPaymentChannelList(Long accountCode);

  void batchUpdateChangeEventId(List<Map<String,Object>> list);
  List<Account> queryByAccountTypeCode(String accountTypeCode);
  List<Account> queryByAccountTypeCode(List<String> accountTypeCode);
  List<Account> queryAccountByConsumeSceneId(List<Long> consumeSceneId);

  boolean bindingCard(String accountCode,String cardId);
  Account findByPhoneAndMerCode(String phone,String merCode);
  void batchBindCard(List<CardInfo> cardInfoList,List<Account> accountList);
  void batchUpload(List<Account> accountList);
  AccountDetailDTO queryDetailPhoneAndMer(String phone);

  /**
   * 查询消费场景DO
   * @param storeCode
   * @param queryType
   * @param queryInfo
   * @return
   */
  AccountConsumeSceneDO queryAccountConsumeSceneDO(String storeCode, WelfareConstant.ConsumeQueryType queryType, String queryInfo);

  /**
   * 根据queryInfo和queryType查询账户
   * @param queryInfo
   * @param queryType
   * @return
   */
  AccountDO queryByQueryInfo(String queryInfo,String queryType,Date transDate);

  /**
   * 按组织机构分组统计各层人员数量
   * @param merCode
   * @return
   */
  List<DepartmentAndAccountTreeResp> groupByDepartment(String merCode);

  /**
   * 批量恢复员工授信额度，如果超过最大授信额度将多余部分汇入溢缴款账户
   * @param creditLimitReq
   */
  void batchRestoreCreditLimit(AccountRestoreCreditLimitReq creditLimitReq);

  /**
   * 免密签约(页面跳转方式）
   * @param accountCode
   * @param paymentChannel
   * @return
   */
  AccountPasswordFreePageSignDTO passwordFreePageSign(Long accountCode, String paymentChannel);

  /**
   * 免密签约(APP、小程序或JSAPI）
   * @param accountCode
   * @param paymentChannel
   * @return
   */
  AccountPasswordFreeSignDTO passwordFreeSign(Long accountCode, String paymentChannel);

  /**
   * 免密解约(APP、小程序或JSAPI）
   * @param accountCode
   * @param paymentChannel
   * @return
   */
  AccountPasswordFreeSignDTO passwordFreeUnsign(Long accountCode, String paymentChannel);
}
