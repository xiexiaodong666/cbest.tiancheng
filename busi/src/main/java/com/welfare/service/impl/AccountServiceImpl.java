package com.welfare.service.impl;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.constants.AccountBindStatus;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.CardStatus;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.*;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountChangeEventRecordCustomizeMapper;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.persist.mapper.AccountMapper;
import com.welfare.service.*;
import com.welfare.service.converter.AccountConverter;
import com.welfare.service.dto.*;
import com.welfare.service.listener.AccountBatchBindCardListener;
import com.welfare.service.listener.AccountUploadListener;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账户信息服务接口实现
 *
 * @author Yuxiang Li
 * @description 由 Mybatisplus Code Generator 创建
 * @since 2021-01-06 13:49:25
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

  private final AccountDao accountDao;
  private final AccountMapper accountMapper;
  private final AccountCustomizeMapper accountCustomizeMapper;
  private final AccountConverter accountConverter;
  @Autowired
  private AccountTypeService accountTypeService;
  private final CardInfoDao cardInfoDao;
  private final CardApplyDao cardApplyDao;
  private final ShoppingFeignClient shoppingFeignClient;
  private final ObjectMapper mapper;
  private final MerchantService merchantService;
  private final DepartmentService departmentService;
  private final SequenceService sequenceService;
  private final CardInfoService cardInfoService;
  @Autowired
  private  AccountChangeEventRecordService accountChangeEventRecordService;
  private final AccountChangeEventRecordCustomizeMapper accountChangeEventRecordCustomizeMapper;
  private final ApplicationContext applicationContext;


  @Override
  public Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page, AccountPageReq accountPageReq) {
    IPage<AccountPageDTO> iPage = accountCustomizeMapper
        .queryPageDTO(page, accountPageReq.getMerCode(), accountPageReq.getAccountName(),
            accountPageReq.getDepartmentCodeList(), accountPageReq.getAccountStatus(),
            accountPageReq.getAccountTypeCode(),accountPageReq.getBinding(),accountPageReq.getCardId());
    return accountConverter.toPage(iPage);
  }

  @Override
  public List<AccountIncrementDTO> queryIncrementDTO(AccountIncrementReq accountIncrementReq) {
    return accountCustomizeMapper.queryIncrementDTO(accountIncrementReq.getStoreCode(),accountIncrementReq.getSize(),accountIncrementReq.getChangeEventId());
  }

  @Override
  public Account findByPhone(String phone) {
    QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
    accountQueryWrapper.eq(Account.PHONE,phone);
    Account acccount = accountDao.getOne(accountQueryWrapper);
    return acccount;
  }

  @Override
  public List<AccountDTO> export(AccountPageReq accountPageReq) {
    List<AccountPageDTO> list = accountCustomizeMapper
        .queryPageDTO(accountPageReq.getMerCode(), accountPageReq.getAccountName(),
            accountPageReq.getDepartmentCodeList(), accountPageReq.getAccountStatus(),
            accountPageReq.getAccountTypeCode(),accountPageReq.getBinding(),accountPageReq.getCardId());
    return accountConverter.toAccountDTOList(list);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String uploadAccount(MultipartFile multipartFile) {
    try {
      AccountUploadListener listener = new AccountUploadListener(accountTypeService,this,
          merchantService,departmentService,sequenceService);
      EasyExcel.read(multipartFile.getInputStream(), AccountUploadDTO.class, listener).sheet()
          .doRead();
      String result = listener.getUploadInfo().toString();
      listener.getUploadInfo().delete(0, listener.getUploadInfo().length());
      return result;
    } catch (Exception e) {
      log.info("批量新增员工解析 Excel exception:{}", e.getMessage());
    }
    return "解析失败";
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public String accountBatchBindCard(MultipartFile multipartFile) {
    try {
      AccountBatchBindCardListener accountBatchBindCardListener = new AccountBatchBindCardListener(
          accountDao,cardInfoService,this);
      EasyExcel.read(multipartFile.getInputStream(), AccountBindCardDTO.class,
          accountBatchBindCardListener).sheet().doRead();
      String result = accountBatchBindCardListener.getUploadInfo().toString();
      accountBatchBindCardListener.getUploadInfo()
          .delete(0, accountBatchBindCardListener.getUploadInfo().length());
      return result;
    } catch (Exception e) {
      log.info("员工批量绑卡 Excel exception:{}", e.getMessage());
    }
    return "解析失败";
  }

  @Override
  public int increaseAccountBalance(BigDecimal increaseBalance, String updateUser,
      String accountCode) {
    return accountMapper.increaseAccountBalance(increaseBalance, updateUser, accountCode);
  }

  @Override
  public Account getByAccountCode(Long accountCode) {
    QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq(Account.ACCOUNT_CODE, accountCode);
    return accountDao.getOne(queryWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean delete(Long id) {
    Account syncAccount = accountMapper.selectById(id);
    if( null ==  syncAccount){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工账户不存在",null);
    }
    boolean result = accountDao.removeById(id);
    AccountChangeEventRecord accountChangeEventRecord = AccountUtils.assemableChangeEvent(AccountChangeType.ACCOUNT_DELETE, syncAccount.getAccountCode(),"员工删除");
    accountChangeEventRecordService.save(accountChangeEventRecord);
    syncAccount.setDeleted(true);
    applicationContext.publishEvent( AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.DELETE).accountList(Arrays.asList(syncAccount)).build());
    return result;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean active(Long id, Integer accountStatus) {
    Account syncAccount = accountMapper.selectById(id);
    if( null ==  syncAccount){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工账户不存在",null);
    }
    Account account = new Account();
    account.setId(id);
    account.setAccountStatus(accountStatus);
    boolean result = accountDao.updateById(account);
    AccountChangeType accountChangeType = AccountChangeType.getByAccountStatus(accountStatus);
    AccountChangeEventRecord accountChangeEventRecord = AccountUtils.assemableChangeEvent(accountChangeType, syncAccount.getAccountCode(),"员工修改状态");
    accountChangeEventRecordService.save(accountChangeEventRecord);
    syncAccount.setAccountStatus(accountStatus);
    applicationContext.publishEvent( AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).accountList(Arrays.asList(syncAccount)).build());
    return result;
  }

  @Override
  public AccountDetailDTO queryDetail(Long id) {
    AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper.queryDetail(id);
    AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
    BeanUtils.copyProperties(accountDetailMapperDTO, accountDetailDTO);
    return accountDetailDTO;
  }

  @Override
  public AccountDetailDTO queryDetailByAccountCode(String accountCode) {
    AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper.queryDetailByParam(null,Long.parseLong(accountCode),null,null);
    AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
    BeanUtils.copyProperties(accountDetailMapperDTO, accountDetailDTO);
    return accountDetailDTO;
  }

  @Override
  public AccountDetailDTO queryDetailByParam(AccountDetailParam accountDetailParam) {
    AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper.queryDetailByParam(accountDetailParam.getId(),accountDetailParam.getAccountCode(),
        accountDetailParam.getPhone(),accountDetailParam.getMerCode());
    if( null == accountDetailMapperDTO ){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"该员工账号不存在",null);
    }
    AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
    BeanUtils.copyProperties(accountDetailMapperDTO, accountDetailDTO);
    return accountDetailDTO;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean save(Account account) {
    validationAccount(account,true);
    Long accounCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());

    AccountChangeEventRecord accountChangeEventRecord = AccountUtils.assemableChangeEvent(AccountChangeType.ACCOUNT_NEW, accounCode,account.getCreateUser());
    accountChangeEventRecordService.save(accountChangeEventRecord);

    account.setAccountCode(accounCode);
    account.setChangeEventId(accountChangeEventRecord.getId());
    boolean result = accountDao.save(account);

    applicationContext.publishEvent( AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD).accountList(Arrays.asList(account)).build());
    return result;
  }

  private void validationAccount(Account account,Boolean isNew){
    Merchant merchant = merchantService.detailByMerCode(account.getMerCode());
    if( null == merchant ) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"商户不存在",null);
    }
    AccountType accountType = accountTypeService.queryByTypeCode(account.getMerCode(),account.getAccountTypeCode());
    if( null == accountType ){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工类型不存在",null);
    }
    Department department = departmentService.getByDepartmentCode(account.getStoreCode());
    if( null ==  department){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工部门不存在",null);
    }
    if(isNew){
      Account queryAccount = this.findByPhone(account.getPhone());
      if( null != queryAccount ){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工手机号已经存在",null);
      }
    }
    else{
      Account syncAccount = accountMapper.selectById(account.getId());
      if( null ==  syncAccount){
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS,"员工账户不存在",null);
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean batchSave(List<Account> accountList) {
    return accountDao.saveBatch(accountList);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean update(Account account) {
    validationAccount(account,false);
    boolean result = accountDao.updateById(account);
    account = accountDao.getById(account.getId());
    applicationContext.publishEvent( AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE).accountList(Arrays.asList(account)).build());
    return result;
  }

  @Override
  public Page<AccountBillDetailDTO> queryAccountBillDetail(Integer currentPage, Integer pageSize,
      String accountCode, Date createTimeStart, Date createTimeEnd) {
    Page<AccountBillDetailMapperDTO> page = new Page<>(currentPage, pageSize);
    IPage<AccountBillDetailMapperDTO> iPage = accountCustomizeMapper
        .queryAccountBillDetail(page, accountCode, createTimeStart, createTimeEnd);
    return accountConverter.toBillDetailPage(iPage);
  }

  @Override
  public List<AccountBillDetailDTO> exportBillDetail(String accountCode, Date createTimeStart,
      Date createTimeEnd) {
    List<AccountBillDetailMapperDTO> accountBillDetailMapperDTOList = accountCustomizeMapper
        .queryAccountBillDetail(accountCode, createTimeStart, createTimeEnd);
    return accountConverter.toAccountBillDetailDTOList(accountBillDetailMapperDTOList);
  }

  @Override
  public AccountBillDTO quertBill(String accountCode, Date createTimeStart, Date createTimeEnd) {
    AccountBillDTO accountBillDTO = new AccountBillDTO();
    AccountBillMapperDTO accountBillMapperDTO = accountCustomizeMapper
        .queryBill(accountCode, createTimeStart, createTimeEnd);
    BeanUtils.copyProperties(accountBillMapperDTO, accountBillDTO);
    return accountBillDTO;
  }

  @Override
  public List<Long> getAccountCodeList(List<Long> accountCodes) {
    QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
    queryWrapper.in(Account.ACCOUNT_CODE, accountCodes);
    List<Account> accounts = accountDao.list(queryWrapper);
    List<Long> codes = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(accounts)) {
      codes = accounts.stream().map(Account::getAccountCode).collect(Collectors.toList());
    }
    return codes;
  }

  @Override
  public AccountSimpleDTO queryAccountInfo(Long accountCode) {
    Account account = getByAccountCode(accountCode);
    AccountSimpleDTO accountSimpleDTO = new AccountSimpleDTO();
    String merCode = account.getMerCode();
    Merchant merchant = merchantService.getMerchantByMerCode(merCode);
    accountSimpleDTO.setMerName(merchant.getMerName());
    accountSimpleDTO.setAccountCode(account.getAccountCode());
    accountSimpleDTO.setAccountName(account.getAccountName());
    accountSimpleDTO.setAccountBalance(account.getAccountBalance());
    accountSimpleDTO.setSurplusQuota(account.getSurplusQuota());
    return accountSimpleDTO;
  }

  @Override
  public void batchUpdateChangeEventId(List<Map<String, Object>> list) {
    accountCustomizeMapper.batchUpdateChangeEventId(list);
    return;
  }

  @Override
  public List<Account> queryByAccountTypeCode(String accountTypeCode) {
    QueryWrapper<Account> queryWrapper = new QueryWrapper<Account>();
    queryWrapper.eq(Account.ACCOUNT_TYPE_CODE,accountTypeCode);
    return accountDao.list(queryWrapper);
  }

  @Override
  public List<Account> queryAccountByConsumeSceneId(List<Long> consumeSceneId) {
    return null;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public boolean bindingCard(String accountCode, String cardId) {
    Account account = this.getByAccountCode(Long.parseLong(accountCode));
    if (null == account) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工账户不存在", null);
    }
    CardInfo cardInfo = cardInfoService.getByCardNo(cardId);
    if (null == cardInfo) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "该卡号不存在", null);
    }
    if (cardInfoService.cardIsBind(cardId)) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "该卡号已经绑定其他账号", null);
    }
    if( cardInfo.getCardStatus().intValue() != CardStatus.WRITTEN.code().intValue() ){
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "请确认该卡片状态", null);
    }
    //绑定创建卡号信息
    cardInfo.setAccountCode(account.getAccountCode());
    cardInfo.setCardStatus(CardStatus.BIND.code());
    cardInfo.setBindTime(new Date());
    boolean updateResult = cardInfoDao.updateById(cardInfo);
    if (updateResult) {
      account.setBinding(AccountBindStatus.BIND.getCode());
    }
    boolean accountUpdate = accountDao.updateById(account);
    return accountUpdate && updateResult;
  }
  public Account findByPhoneAndMerCode(String phone, String merCode) {
    QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
    accountQueryWrapper.eq(Account.PHONE,phone);
    accountQueryWrapper.eq(Account.MER_CODE, merCode);
    return accountDao.getOne(accountQueryWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchBindCard(List<CardInfo> cardInfoList, List<Account> accountList) {
    if( CollectionUtils.isEmpty(cardInfoList) ||
        CollectionUtils.isEmpty(accountList)){
      return;
    }
    cardInfoDao.saveBatch(cardInfoList);
    accountDao.updateBatchById(accountList);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchUpload(List<Account> accountList) {
    Boolean result = this.batchSave(accountList);
    if (result == true) {
      List<AccountChangeEventRecord> recordList = AccountUtils
          .getEventList(accountList, AccountChangeType.ACCOUNT_NEW);
      accountChangeEventRecordService.batchSave(recordList, AccountChangeType.ACCOUNT_NEW);
      //批量回写
      List<Map<String, Object>> mapList = AccountUtils.getMaps(recordList);
      this.batchUpdateChangeEventId(mapList);
      applicationContext.publishEvent(AccountEvt
          .builder().typeEnum(ShoppingActionTypeEnum.BATCH_ADD).accountList(accountList).build());
    }
  }

  @Override
  public AccountDetailDTO queryDetailPhoneAndMer(String phone) {
    AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper.queryDetailPhoneAndMer(phone,
            MerchantUserHolder.getMerchantUser().getMerchantCode());
    if (accountDetailMapperDTO == null) {
      return null;
    }
    AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
    BeanUtils.copyProperties(accountDetailMapperDTO, accountDetailDTO);
    return accountDetailDTO;
  }
}