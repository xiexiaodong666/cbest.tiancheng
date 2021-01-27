package com.welfare.service.impl;


import static com.welfare.common.constants.RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welfare.common.constants.AccountBindStatus;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.CardStatus;
import com.welfare.common.constants.WelfareConstant.MerAccountTypeCode;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.dao.CardApplyDao;
import com.welfare.persist.dao.CardInfoDao;
import com.welfare.persist.dto.AccountBillDetailMapperDTO;
import com.welfare.persist.dto.AccountBillMapperDTO;
import com.welfare.persist.dto.AccountDetailMapperDTO;
import com.welfare.persist.dto.AccountIncrementDTO;
import com.welfare.persist.dto.AccountPageDTO;
import com.welfare.persist.dto.AccountSimpleDTO;
import com.welfare.persist.entity.Account;
import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.AccountChangeEventRecord;
import com.welfare.persist.entity.AccountType;
import com.welfare.persist.entity.CardInfo;
import com.welfare.persist.entity.Department;
import com.welfare.persist.entity.Merchant;
import com.welfare.persist.entity.MerchantAccountType;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.persist.mapper.AccountChangeEventRecordCustomizeMapper;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.persist.mapper.AccountMapper;
import com.welfare.service.AccountChangeEventRecordService;
import com.welfare.service.AccountService;
import com.welfare.service.AccountTypeService;
import com.welfare.service.CardInfoService;
import com.welfare.service.DepartmentService;
import com.welfare.service.MerchantAccountTypeService;
import com.welfare.service.MerchantService;
import com.welfare.service.SequenceService;
import com.welfare.service.converter.AccountConverter;
import com.welfare.service.dto.AccountBillDTO;
import com.welfare.service.dto.AccountBillDetailDTO;
import com.welfare.service.dto.AccountBindCardDTO;
import com.welfare.service.dto.AccountDTO;
import com.welfare.service.dto.AccountDetailDTO;
import com.welfare.service.dto.AccountDetailParam;
import com.welfare.service.dto.AccountIncrementReq;
import com.welfare.service.dto.AccountPageReq;
import com.welfare.service.dto.AccountReq;
import com.welfare.service.dto.AccountUploadDTO;
import com.welfare.service.listener.AccountBatchBindCardListener;
import com.welfare.service.listener.AccountUploadListener;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
  private AccountChangeEventRecordService accountChangeEventRecordService;
  private final AccountChangeEventRecordCustomizeMapper accountChangeEventRecordCustomizeMapper;
  private final ApplicationContext applicationContext;
  private final MerchantAccountTypeService merchantAccountTypeService;
  private final AccountAmountTypeMapper accountAmountTypeMapper;
  private final RedissonClient redissonClient;


  @Override
  public Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page, AccountPageReq accountPageReq) {
    IPage<AccountPageDTO> iPage = accountCustomizeMapper
        .queryPageDTO(page, accountPageReq.getMerCode(), accountPageReq.getAccountName(),
            accountPageReq.getDepartmentPathList(), accountPageReq.getAccountStatus(),
            accountPageReq.getAccountTypeCode(), accountPageReq.getBinding(),
            accountPageReq.getCardId());
    return accountConverter.toPage(iPage);
  }

  @Override
  public List<AccountIncrementDTO> queryIncrementDTO(AccountIncrementReq accountIncrementReq) {
    return accountCustomizeMapper
        .queryIncrementDTO(accountIncrementReq.getStoreCode(), accountIncrementReq.getSize(),
            accountIncrementReq.getChangeEventId(),ConsumeTypeEnum.SHOP_SHOPPING.getCode());
  }

  @Override
  public Account findByPhone(String phone, String merCode) {
    QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
    accountQueryWrapper.eq(Account.PHONE, phone);
    accountQueryWrapper.eq(Account.MER_CODE, merCode);
    Account acccount = accountDao.getOne(accountQueryWrapper);
    return acccount;
  }

  @Override
  public List<AccountDTO> export(AccountPageReq accountPageReq) {
    List<AccountPageDTO> list = accountCustomizeMapper
        .queryPageDTO(accountPageReq.getMerCode(), accountPageReq.getAccountName(),
            accountPageReq.getDepartmentPathList(), accountPageReq.getAccountStatus(),
            accountPageReq.getAccountTypeCode(), accountPageReq.getBinding(),
            accountPageReq.getCardId());
    return accountConverter.toAccountDTOList(list);
  }

  @Override
  public String uploadAccount(MultipartFile multipartFile) {
    try {
      AccountUploadListener listener = new AccountUploadListener(accountTypeService, this,
          merchantService, departmentService, sequenceService);
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
  public String accountBatchBindCard(MultipartFile multipartFile) {
    try {
      AccountBatchBindCardListener accountBatchBindCardListener = new AccountBatchBindCardListener(
          accountDao, cardInfoService, this);
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
    if (null == syncAccount) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工账户不存在", null);
    }
    boolean result = accountDao.removeById(id);

    accountChangeEvtRecoed(AccountChangeType.ACCOUNT_DELETE, syncAccount.getAccountCode());
    syncAccount.setDeleted(true);
    applicationContext.publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.DELETE)
        .accountList(Arrays.asList(syncAccount)).build());
    return result;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean active(Long id, Integer accountStatus) {
    Account syncAccount = accountMapper.selectById(id);
    if (null == syncAccount) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工账户不存在", null);
    }
    Account account = new Account();
    account.setId(id);
    account.setAccountStatus(accountStatus);
    boolean result = accountDao.updateById(account);
    AccountChangeType accountChangeType = AccountChangeType.getByAccountStatus(accountStatus);
    accountChangeEvtRecoed(accountChangeType, syncAccount.getAccountCode());
    syncAccount.setAccountStatus(accountStatus);
    applicationContext.publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE)
        .accountList(Arrays.asList(syncAccount)).build());
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
    AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper
        .queryDetailByParam(null, Long.parseLong(accountCode), null, null);
    AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
    BeanUtils.copyProperties(accountDetailMapperDTO, accountDetailDTO);
    return accountDetailDTO;
  }

  @Override
  public AccountDetailDTO queryDetailByParam(AccountDetailParam accountDetailParam) {
    AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper
        .queryDetailByParam(accountDetailParam.getId(), accountDetailParam.getAccountCode(),
            accountDetailParam.getPhone(), accountDetailParam.getMerCode());
    if (null == accountDetailMapperDTO) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "该员工账号不存在", null);
    }
    AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
    BeanUtils.copyProperties(accountDetailMapperDTO, accountDetailDTO);
    return accountDetailDTO;
  }

  @Override
  public void batchSyncData(Integer accountStatus) {
    QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<Account>();
    accountQueryWrapper.eq(Account.ACCOUNT_STATUS,accountStatus);
    List<Account> accountList = accountDao.list(accountQueryWrapper);

    List<AccountChangeEventRecord> recordList = AccountUtils
        .getEventList(accountList, AccountChangeType.ACCOUNT_NEW);
    accountChangeEventRecordService.batchSave(recordList, AccountChangeType.ACCOUNT_NEW);

    for( Account account :  accountList){
      account.setAccountStatus(AccountStatus.ENABLE.getCode());
    }
    accountDao.updateBatchById(accountList);

    applicationContext.publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD)
        .accountList(accountList).build());
  }

  private void assemableAccount(Account account) {
    //修改account 成正常的状态
    AccountChangeEventRecord accountChangeEventRecord = AccountUtils
        .assemableChangeEvent(AccountChangeType.ACCOUNT_NEW, account.getAccountCode(),
            account.getCreateUser());
    accountChangeEventRecordService.save(accountChangeEventRecord);
    account.setChangeEventId(accountChangeEventRecord.getId());
    account.setAccountStatus(AccountStatus.ENABLE.getCode());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public Boolean save(AccountReq accountReq) {
    Account account = assemableAccount(accountReq);
    validationAccount(account, true);
    AccountChangeEventRecord accountChangeEventRecord = AccountUtils
        .assemableChangeEvent(AccountChangeType.ACCOUNT_NEW, account.getAccountCode(),
            account.getCreateUser());
    accountChangeEventRecordService.save(accountChangeEventRecord);
    if (accountReq.getCredit()) {
      //授信额度
      AccountAmountType accountAmountType = getAccountAmountType(account.getAccountCode(),
          account.getMaxQuota(), account.getMerCode());
      accountAmountTypeMapper.insert(accountAmountType);
      account.setSurplusQuota(account.getMaxQuota());
    }
    account.setChangeEventId(accountChangeEventRecord.getId());
    boolean result = accountDao.save(account);

    applicationContext.publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD)
        .accountList(Arrays.asList(account)).build());
    return result;
  }

  private Account assemableAccount(AccountReq accountReq) {
    Account account = new Account();
    Long accounCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());
    BeanUtils.copyProperties(accountReq, account);
    account.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
    account.setStoreCode(accountReq.getDepartmentCode());
    account.setAccountCode(accounCode);
    return account;
  }

  private AccountAmountType getAccountAmountType(Long accountCode, BigDecimal accountBalance,
      String merCode) {
    MerchantAccountType merchantAccountType = merchantAccountTypeService.queryOneByCode(
        merCode,
        MerAccountTypeCode.SURPLUS_QUOTA.code());
    if (null == merchantAccountType) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户无授信额度福利类型", null);
    }
    AccountAmountType accountAmountType = new AccountAmountType();
    accountAmountType.setAccountCode(accountCode);
    accountAmountType.setAccountBalance(accountBalance);
    accountAmountType.setCreateTime(new Date());
    accountAmountType.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
    accountAmountType.setMerAccountTypeCode(MerAccountTypeCode.SURPLUS_QUOTA.code());
    return accountAmountType;
  }

  private void validationAccount(Account account, Boolean isNew) {
    Merchant merchant = merchantService.detailByMerCode(account.getMerCode());
    if (null == merchant) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "商户不存在", null);
    }
    AccountType accountType = accountTypeService
        .queryByTypeCode(account.getMerCode(), account.getAccountTypeCode());
    if (null == accountType) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工类型不存在", null);
    }
    Department department = departmentService.getByDepartmentCode(account.getStoreCode());
    if (null == department) {
      throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工部门不存在", null);
    }
    if (isNew) {
      Account queryAccount = this.findByPhone(account.getPhone(), account.getMerCode());
      if (null != queryAccount) {
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工手机号已经存在", null);
      }
    } else {
      Account syncAccount = accountMapper.selectById(account.getId());
      if (null == syncAccount) {
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "员工账户不存在", null);
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
  public Boolean update(AccountReq accountReq) {
    Account oldAccount = accountMapper.selectById(accountReq.getId());
    String lockKey = ACCOUNT_AMOUNT_TYPE_OPERATE + ":" + oldAccount.getAccountCode();
    RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
    try {
      Account account = assemableAccount4update(accountReq);
      validationAccount(account, false);
      //修改授信额度
      updateSurPlusQuota(oldAccount.getAccountCode(), oldAccount.getMaxQuota(),
          oldAccount.getSurplusQuota(),
          account.getMaxQuota(), account.getUpdateUser(), accountReq.getCredit(),
          account.getMerCode());
      //记录变更时间离线支付用
      accountChangeEvtRecoed(AccountChangeType.ACCOUNT_UPDATE, oldAccount.getAccountCode());

      boolean result = accountDao.updateById(account);
      account = accountDao.getById(account.getId());

      applicationContext.publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE)
          .accountList(Arrays.asList(account)).build());
      return result;
    } finally {
      if (accountLock.isHeldByCurrentThread()) {
        DistributedLockUtil.unlock(accountLock);
      }
    }
  }

  private void accountChangeEvtRecoed(AccountChangeType accountChangeType, Long accountCode) {
    AccountChangeEventRecord accountChangeEventRecord = AccountUtils
        .assemableChangeEvent(accountChangeType, accountCode,
            MerchantUserHolder.getMerchantUser().getUsername());
    accountChangeEventRecordService.save(accountChangeEventRecord);
  }

  private Account assemableAccount4update(AccountReq accountReq) {
    Account account = new Account();
    BeanUtils.copyProperties(accountReq, account);
    account.setStoreCode(accountReq.getDepartmentCode());
    account.setUpdateUser(MerchantUserHolder.getMerchantUser().getUsername());
    return account;
  }

  private void updateSurPlusQuota(Long accountCode, BigDecimal oldMaxQuota, BigDecimal surplusQuota,
      BigDecimal newMaxQuota,
      String updateUser, Boolean credit, String merCode) {
    if (credit) {
      QueryWrapper<AccountAmountType> queryWrapper = new QueryWrapper();
      queryWrapper.eq(AccountAmountType.ACCOUNT_CODE, accountCode);
      queryWrapper
          .eq(AccountAmountType.MER_ACCOUNT_TYPE_CODE, MerAccountTypeCode.SURPLUS_QUOTA.code());

      AccountAmountType accountAmountType = accountAmountTypeMapper.selectOne(queryWrapper);
      if (null == accountAmountType) {
        //授信额度被删除新增 修改account额度
        AccountAmountType addAccountAmountType = getAccountAmountType(accountCode, newMaxQuota,
            merCode);
        accountAmountTypeMapper.insert(addAccountAmountType);
        accountCustomizeMapper
            .updateMaxAndSurplusQuota(accountCode.toString(), newMaxQuota, newMaxQuota, updateUser);
        return;
      }
      if (null != accountAmountType) {
        //判断剩余授信额度  如果是增加,那么额度就是
        if (surplusQuota.compareTo(oldMaxQuota.subtract(newMaxQuota)) < 0) {
          throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "剩余授信额度不足", null);
        }
        //修改了额度
        int incrAccountResult = accountCustomizeMapper
            .increaseAccountSurplusQuota(newMaxQuota.subtract(oldMaxQuota),
                updateUser, accountCode.toString());
        int incrAmountResult = accountAmountTypeMapper.incrBalance(accountCode,
            MerAccountTypeCode.SURPLUS_QUOTA.code(),
            newMaxQuota.subtract(oldMaxQuota),
            updateUser);//先判断有没有记录 如果没有插入 如果有减少
        if (incrAccountResult <= 0 || incrAmountResult <= 0) {
          throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "剩余授信额度不足", null);
        }
      }
    } else {
      //关闭授信额度,如果授信没有使用直接删除
      if (oldMaxQuota.compareTo(surplusQuota) != 0) {
        throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "授信额度已使用 无法关闭", null);
      }
      accountAmountTypeMapper.updateBalance(accountCode,
          MerAccountTypeCode.SURPLUS_QUOTA.code(),
          BigDecimal.ZERO,
          updateUser);
      accountCustomizeMapper
          .updateMaxAndSurplusQuota(accountCode.toString(), BigDecimal.ZERO, BigDecimal.ZERO,
              updateUser);
    }
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
    accountSimpleDTO.setPhone(account.getPhone());
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
    queryWrapper.eq(Account.ACCOUNT_TYPE_CODE, accountTypeCode);
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
    if (cardInfo.getCardStatus().intValue() != CardStatus.WRITTEN.code().intValue()) {
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

  @Override
  public Account findByPhoneAndMerCode(String phone, String merCode) {
    QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
    accountQueryWrapper.eq(Account.PHONE, phone);
    accountQueryWrapper.eq(Account.MER_CODE, merCode);
    return accountDao.getOne(accountQueryWrapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchBindCard(List<CardInfo> cardInfoList, List<Account> accountList) {
    if (CollectionUtils.isEmpty(cardInfoList) ||
        CollectionUtils.isEmpty(accountList)) {
      return;
    }
    cardInfoDao.updateBatchById(cardInfoList);
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

      List<Long> codeList = accountList.stream().map(account -> {
        return account.getAccountCode();
      }).collect(Collectors.toList());
      //批量下发员工账号数据
      applicationContext.publishEvent(AccountEvt
          .builder().typeEnum(ShoppingActionTypeEnum.ACCOUNT_BATCH_ADD).codeList(codeList).build());
    }
  }

  @Override
  public AccountDetailDTO queryDetailPhoneAndMer(String phone) {
    AccountDetailMapperDTO accountDetailMapperDTO = accountCustomizeMapper
        .queryDetailPhoneAndMer(phone,
            MerchantUserHolder.getMerchantUser().getMerchantCode());
    if (accountDetailMapperDTO == null) {
      return null;
    }
    AccountDetailDTO accountDetailDTO = new AccountDetailDTO();
    BeanUtils.copyProperties(accountDetailMapperDTO, accountDetailDTO);
    return accountDetailDTO;
  }
}