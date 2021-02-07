package com.welfare.service.impl;
import com.welfare.common.enums.FileUniversalStorageEnum;
import com.welfare.service.dto.UploadImgErrorMsgDTO;
import java.util.Date;


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
import com.welfare.common.constants.WelfareConstant.TransType;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BusiException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.AccountUtil;
import com.welfare.common.util.DistributedLockUtil;
import com.welfare.common.util.MerchantUserHolder;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.common.util.UserInfoHolder;
import com.welfare.persist.dao.*;
import com.welfare.persist.dto.*;
import com.welfare.persist.entity.*;
import com.welfare.persist.mapper.AccountAmountTypeMapper;
import com.welfare.persist.mapper.AccountBillDetailMapper;
import com.welfare.persist.mapper.AccountChangeEventRecordCustomizeMapper;
import com.welfare.persist.mapper.AccountCustomizeMapper;
import com.welfare.persist.mapper.AccountDeductionDetailMapper;
import com.welfare.persist.mapper.AccountMapper;
import com.welfare.service.*;
import com.welfare.service.converter.AccountConverter;
import com.welfare.service.dto.*;
import com.welfare.service.listener.AccountBatchBindCardListener;
import com.welfare.service.listener.AccountUploadListener;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;

import java.math.BigDecimal;
import java.util.*;
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
import org.springframework.util.Assert;
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
    private final FileUniversalStorageDao fileUniversalStorageDao;

  private final AccountMapper accountMapper;
    private final AccountCustomizeMapper accountCustomizeMapper;
    private final AccountConverter accountConverter;
    @Autowired
    private AccountTypeService accountTypeService;
    private final CardInfoDao cardInfoDao;
    private final CardApplyDao cardApplyDao;
    @Autowired(required = false)
    private ShoppingFeignClient shoppingFeignClient;
    private final ObjectMapper mapper;
    @Autowired
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
    private final AccountBillDetailMapper accountBillDetailMapper;
    private final AccountDeductionDetailMapper accountDeductionDetailMapper;
    private final AccountDeductionDetailDao accountDeductionDetailDao;
    private final AccountBillDetailDao accountBillDetailDao;
    private final OrderTransRelationDao orderTransRelationDao;
    private final AccountConsumeSceneDao accountConsumeSceneDao;
    private final AccountConsumeSceneStoreRelationDao accountConsumeSceneStoreRelationDao;
    private final SupplierStoreDao supplierStoreDao;

    @Override
    public Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page, AccountPageReq accountPageReq) {
        IPage<AccountPageDTO> iPage = accountCustomizeMapper
                .queryPageDTO(page, accountPageReq.getMerCode(), accountPageReq.getAccountName(),
                        accountPageReq.getDepartmentPathList(), accountPageReq.getAccountStatus(),
                        accountPageReq.getAccountTypeCode(), accountPageReq.getBinding(),
                        accountPageReq.getCardId(),
                        accountPageReq.getPhone());
        return accountConverter.toPage(iPage);
    }

    @Override
    public List<AccountIncrementDTO> queryIncrementDTO(AccountIncrementReq accountIncrementReq) {
        return accountCustomizeMapper
                .queryIncrementDTO(accountIncrementReq.getStoreCode(), accountIncrementReq.getSize(),
                        accountIncrementReq.getChangeEventId(), ConsumeTypeEnum.SHOP_SHOPPING.getCode());
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
                        accountPageReq.getCardId(),
                        accountPageReq.getPhone());
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
    @Transactional(rollbackFor = Exception.class)
    public void batchSyncData(Integer staffStatus) {
        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<Account>();
        accountQueryWrapper.eq(Account.STAFF_STATUS, staffStatus);
        List<Account> accountList = accountDao.list(accountQueryWrapper);

        List<AccountChangeEventRecord> recordList = AccountUtils
                .getEventList(accountList, AccountChangeType.ACCOUNT_NEW);
        accountChangeEventRecordService.batchSave(recordList, AccountChangeType.ACCOUNT_NEW);

        for (Account account : accountList) {
            account.setStaffStatus("0");
        }
        accountDao.updateBatchById(accountList);

        List<List<Account>> averageAccountList = AccountUtil.averageAssign(accountList);
        for (List<Account> syncList : averageAccountList) {
            List<Long> codeList = syncList.stream().map(account -> {
                return account.getAccountCode();
            }).collect(Collectors.toList());
            //批量下发员工账号数据
            applicationContext.publishEvent(AccountEvt
                    .builder().typeEnum(ShoppingActionTypeEnum.ACCOUNT_BATCH_ADD).codeList(codeList)
                    .build());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AccountBatchImgDTO uploadBatchImg(AccountBatchImgReq accountBatchImgReq) {
      AccountBatchImgDTO accountBatchImgDTO = new AccountBatchImgDTO();

      List<String> successList = new ArrayList<>();
      List<String> failList;
      Map<String, FileUniversalStorage> map = new HashMap<>();

      String merCode = accountBatchImgReq.getMerCode();
      List<AccountBatchImgInfoReq> batchImgInfoListReq = accountBatchImgReq.getAccountBatchImgInfoReqList();

      batchImgInfoListReq = batchImgInfoListReq.stream().collect(
          Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getPhone()))), ArrayList::new));

      List<String> phones = batchImgInfoListReq.stream().map(b->b.getPhone()).collect(Collectors.toList());

      QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
      accountQueryWrapper.in(Account.PHONE, phones);
      accountQueryWrapper.eq(Account.MER_CODE, merCode);
      List<Account> accountList = accountDao.list(accountQueryWrapper);

      List<FileUniversalStorage> fileUniversalStorageList = new ArrayList<>();

      for(Account account : accountList) {
        AccountBatchImgInfoReq accountOptional = batchImgInfoListReq.stream().filter(i->i.getPhone().equals(account.getPhone())).findFirst().get();
        successList.add(accountOptional.getPhone());
        FileUniversalStorage fileUniversalStorage;
        if(account.getFileUniversalStorageId() != null && account.getFileUniversalStorageId() != 0) {
          fileUniversalStorage = fileUniversalStorageDao.getById(account.getFileUniversalStorageId());
          fileUniversalStorage.setUrl(accountOptional.getUrl());
        } else {
          fileUniversalStorage = new FileUniversalStorage();
          fileUniversalStorage.setType(FileUniversalStorageEnum.ACCOUNT_IMG.getCode());
          fileUniversalStorage.setUrl(accountOptional.getUrl());
          fileUniversalStorage.setDeleted(false);
        }
        map.put(account.getPhone(), fileUniversalStorage);
        fileUniversalStorageList.add(fileUniversalStorage);
      }

      phones.removeAll(successList);
      failList = phones;

      fileUniversalStorageDao.saveOrUpdateBatch(fileUniversalStorageList);
      for (int i =0; i< accountList.size(); i++) {
        accountList.get(i).setFileUniversalStorageId(map.get(accountList.get(i).getPhone()).getId());
      }
      accountDao.updateBatchById(accountList);

      List<UploadImgErrorMsgDTO> uploadImgErrorMsgDTOList = new ArrayList<>();
      accountBatchImgDTO.setSuccessList(successList);

      if(CollectionUtils.isNotEmpty(failList)) {
        for (String phone:
        failList) {
          UploadImgErrorMsgDTO uploadImgErrorMsgDTO = new UploadImgErrorMsgDTO();
          uploadImgErrorMsgDTO.setError("该商户下没有此用户");
          uploadImgErrorMsgDTO.setPhone(phone);

          uploadImgErrorMsgDTOList.add(uploadImgErrorMsgDTO);
        }
        accountBatchImgDTO.setFailList(uploadImgErrorMsgDTOList);
      }

      return accountBatchImgDTO;
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

        FileUniversalStorage fileUniversalStorage = new FileUniversalStorage();
        fileUniversalStorage.setType(FileUniversalStorageEnum.ACCOUNT_IMG.getCode());
        fileUniversalStorage.setUrl(accountReq.getImgUrl());
        fileUniversalStorage.setDeleted(false);
        fileUniversalStorageDao.save(fileUniversalStorage);
        account.setFileUniversalStorageId(fileUniversalStorage.getId());


        // 添加
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
            Account queryAccount = this.findByPhone(account.getPhone(), account.getMerCode());
            if (queryAccount != null && queryAccount.getId().longValue() != account.getId().longValue()) {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "已经有其他员工绑定了该手机", null);
            }
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

          // 上传照片
          if(oldAccount.getFileUniversalStorageId() != null) {
            FileUniversalStorage fileUniversalStorage = fileUniversalStorageDao.getById(oldAccount.getFileUniversalStorageId());
            fileUniversalStorage.setUrl(accountReq.getImgUrl());
            fileUniversalStorageDao.updateById(fileUniversalStorage);
          } else {
            FileUniversalStorage fileUniversalStorage = new FileUniversalStorage();
            fileUniversalStorage.setType(FileUniversalStorageEnum.ACCOUNT_IMG.getCode());
            fileUniversalStorage.setUrl(accountReq.getImgUrl());
            fileUniversalStorage.setDeleted(false);
            fileUniversalStorageDao.save(fileUniversalStorage);
            account.setFileUniversalStorageId(fileUniversalStorage.getId());
          }

            validationAccount(account, false);

            //记录变更时间离线支付用
            accountChangeEvtRecoed(AccountChangeType.ACCOUNT_UPDATE, oldAccount.getAccountCode());

            boolean result = accountDao.updateById(account);
            //修改授信额度
            updateSurPlusQuota(oldAccount.getAccountCode(), oldAccount.getMaxQuota(),
                    oldAccount.getSurplusQuota(),
                    account.getMaxQuota(), account.getUpdateUser(), accountReq.getCredit(),
                    account.getMerCode());
            account = accountDao.getById(account.getId());

            applicationContext
                    .publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.UPDATE)
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
                                    BigDecimal newMaxQuota, String updateUser, Boolean credit, String merCode) {

        if (credit) {
            AccountAmountTypeService accountAmountTypeService = SpringBeanUtils
                    .getBean(AccountAmountTypeService.class);
            AccountAmountType accountAmountType = accountAmountTypeService
                    .queryOne(accountCode, MerAccountTypeCode.SURPLUS_QUOTA.code());
            if (null == accountAmountType) {
                //账户新增授信额度 修改account额度
                AccountAmountType addAccountAmountType = getAccountAmountType(accountCode, newMaxQuota,
                        merCode);
                accountAmountTypeMapper.insert(addAccountAmountType);
                accountCustomizeMapper
                        .updateMaxAndSurplusQuota(accountCode.toString(), newMaxQuota, newMaxQuota, updateUser);
            } else {
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
            //关闭授信额度,账户以及账户金额类型表变0
            if (oldMaxQuota.compareTo(surplusQuota) != 0) {
                throw new BusiException(ExceptionCode.ILLEGALITY_ARGURMENTS, "授信额度已使用 无法关闭", null);
            }
            accountAmountTypeMapper.updateBalance(accountCode, MerAccountTypeCode.SURPLUS_QUOTA.code(),
                    BigDecimal.ZERO, updateUser);
            accountCustomizeMapper.updateMaxAndSurplusQuota(accountCode.toString(), BigDecimal.ZERO,
                    BigDecimal.ZERO, updateUser);
        }
        //保存流水
        saveDetail(accountCode, oldMaxQuota, newMaxQuota);
    }

    private void saveDetail(Long accountCode, BigDecimal oldMaxQuota, BigDecimal newMaxQuota) {
        TransType transType = null;
        BigDecimal transAmout = null;
        if (oldMaxQuota.compareTo(newMaxQuota) < 0) {
            //变大
            transType = TransType.RESET_INCR;
            transAmout = newMaxQuota.subtract(oldMaxQuota);
        } else if (oldMaxQuota.compareTo(newMaxQuota) > 0) {
            //变小
            transType = TransType.RESET_DECR;
            transAmout = oldMaxQuota.subtract(newMaxQuota);
        } else {
            //不变
            return;
        }
        Account account = this.getByAccountCode(accountCode);
        String transNo = String.valueOf(
                sequenceService.nextNo(WelfareConstant.SequenceType.RESET_ACCOUNT_SURPLUS_QUOTA.code()));
        ;
        AccountBillDetail accountBillDetail = getAccountBillDetail(accountCode,
                transType, transAmout, account, transNo);
        accountBillDetailMapper.insert(accountBillDetail);
        AccountDeductionDetail accountDeductionDetail = getAccountDeductionDetail(
                accountCode, transType, transAmout, account, transNo);
        accountDeductionDetailMapper.insert(accountDeductionDetail);
    }

    private AccountDeductionDetail getAccountDeductionDetail(Long accountCode, TransType transType,
                                                             BigDecimal transAmout, Account account, String transNo) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(accountCode);
        accountDeductionDetail.setTransNo(transNo);
        accountDeductionDetail.setTransType(transType.code());
        accountDeductionDetail.setTransAmount(transAmout);
        accountDeductionDetail.setTransTime(new Date());
        accountDeductionDetail.setMerAccountType(MerAccountTypeCode.SURPLUS_QUOTA.code());
        accountDeductionDetail.setAccountDeductionAmount(transAmout);
        accountDeductionDetail.setAccountAmountTypeBalance(account.getSurplusQuota());
        accountDeductionDetail.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
        accountDeductionDetail.setCreateTime(new Date());
        return accountDeductionDetail;
    }

    private AccountBillDetail getAccountBillDetail(Long accountCode, TransType transType,
                                                   BigDecimal transAmout, Account account, String transNo) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(accountCode);
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setTransNo(transNo);
        accountBillDetail.setTransType(transType.code());
        accountBillDetail.setTransAmount(transAmout);
        accountBillDetail.setTransTime(new Date());
        accountBillDetail.setSurplusQuota(account.getSurplusQuota());
        accountBillDetail.setCreateUser(MerchantUserHolder.getMerchantUser().getUsername());
        accountBillDetail.setCreateTime(new Date());
        return accountBillDetail;
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
        String storeCode = account.getStoreCode();
        List<Department> parentDepartmentList = getParentDepartmentList(storeCode);
        accountSimpleDTO.setMerName(
                parentDepartmentList.size() > 1 ? parentDepartmentList.get(1).getDepartmentName()
                        : parentDepartmentList.get(0).getDepartmentName());
        accountSimpleDTO.setPhone(account.getPhone());
        accountSimpleDTO.setAccountName(account.getAccountName());
        accountSimpleDTO.setAccountBalance(account.getAccountBalance());
        accountSimpleDTO.setSurplusQuota(account.getSurplusQuota());
        return accountSimpleDTO;
    }

    public List<Department> getParentDepartmentList(String code) {
        Department department = departmentService.getByDepartmentCode(code);
        String departmentParent = department.getDepartmentParent();
        String merCode = department.getMerCode();
        List<Department> codeList = new ArrayList<>();
        if (merCode.equals(departmentParent)) {
            codeList.add(department);
            return codeList;
        }
        List<Department> parentCodeList = getParentDepartmentList(departmentParent);
        parentCodeList.add(department);
        return parentCodeList;
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
                    .builder().typeEnum(ShoppingActionTypeEnum.ACCOUNT_BATCH_ADD).codeList(codeList)
                    .build());
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreSurplusQuotaByMerCode(String merCode, BigDecimal merUpdateCreditAmount,
                                             String settlementTransNo) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(Account.MER_CODE, merCode);
        queryWrapper.eq(Account.CREDIT, Boolean.TRUE);
        List<Account> accounts = accountDao.list(queryWrapper);
        String updateUser = UserInfoHolder.getUserInfo().getUserId();
        if (CollectionUtils.isNotEmpty(accounts)) {
            accounts.forEach(account -> {
                restoreSurplusQuotaByAccountCode(account.getAccountCode(), updateUser, settlementTransNo);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void restoreSurplusQuotaByAccountCode(Long accountCode, String updateUser,
                                                 String settlementTransNo) {
        String lockKey = ACCOUNT_AMOUNT_TYPE_OPERATE + ":" + accountCode;
        RLock accountLock = DistributedLockUtil.lockFairly(lockKey);
        try {
            QueryWrapper<Account> queryCredit = new QueryWrapper<>();
            queryCredit.eq(Account.ACCOUNT_CODE, accountCode);
            queryCredit.eq(Account.CREDIT, Boolean.TRUE);
            Account account = accountDao.getOne(queryCredit);
            if (Objects.nonNull(account)) {
                BigDecimal updateQuota = account.getMaxQuota().subtract(account.getSurplusQuota());
                int isSuccess = accountCustomizeMapper
                        .restoreAccountSurplusQuota(account.getAccountCode(), updateUser);
                accountAmountTypeMapper.updateBalance(
                        account.getAccountCode(),
                        MerAccountTypeCode.SURPLUS_QUOTA.code(),
                        account.getMaxQuota(),
                        updateUser
                );
                String transNo = sequenceService.nextNo(WelfareConstant.SequenceType.DEPOSIT.code()) + "";
                AccountBillDetail accountBillDetail = assemblyAccountBillDetail(account, updateQuota,
                        transNo);
                accountBillDetailDao.save(accountBillDetail);
                AccountDeductionDetail accountDeductionDetail = assemblyAccountDeductionDetail(account,
                        updateQuota, transNo);
                accountDeductionDetailDao.save(accountDeductionDetail);
                OrderTransRelation transRelation = assemblyOrderTransRelation(updateQuota, transNo,
                        settlementTransNo);
                orderTransRelationDao.save(transRelation);
            }
        } finally {
            DistributedLockUtil.unlock(accountLock);
        }
    }

    @Override
    public AccountConsumeSceneDO queryAccountConsumeSceneDO(String storeCode, WelfareConstant.ConsumeQueryType queryType, String queryInfo) {
        Long accountCode = null;
        AccountConsumeSceneDO accountConsumeSceneDO = new AccountConsumeSceneDO();
        accountConsumeSceneDO.setQueryInfo(queryInfo);
        accountConsumeSceneDO.setQueryInfoType(queryType.code());
        switch (queryType) {
            case CARD:
                CardInfoDao cardInfoDao = SpringBeanUtils.getBean(CardInfoDao.class);
                CardInfo cardInfo = cardInfoDao.getOneByMagneticStripe(queryInfo);
                Assert.notNull(cardInfo, "根据磁条号未找到卡片:" + queryInfo);
                Assert.isTrue(WelfareConstant.CardEnable.ENABLE.code().equals(cardInfo.getEnabled()), "卡片已禁用");
                accountCode = cardInfo.getAccountCode();
                break;
            case BARCODE:
                BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
                accountCode = barcodeService.parseAccountFromBarcode(queryInfo, Calendar.getInstance().getTime(), true);
                break;
            default:
                break;
        }
        Assert.notNull(accountCode, "根据条件没有解析出账号");
        Account account = this.getByAccountCode(accountCode);
        accountConsumeSceneDO.setAccount(account);
        SupplierStore supplierStore = supplierStoreDao.getOneByCode(storeCode);
        List<AccountConsumeScene> accountConsumeScenes
                = accountConsumeSceneDao.getAccountTypeAndMerCode(account.getAccountTypeCode(), supplierStore.getMerCode());
        if(CollectionUtils.isEmpty(accountConsumeScenes)){
            log.warn("没有找到消费场景");
            accountConsumeSceneDO.setAccountConsumeTypes(Collections.emptyList());
            return accountConsumeSceneDO;
        }
        List<Long> sceneIds = accountConsumeScenes.stream()
                .map(AccountConsumeScene::getId).collect(Collectors.toList());
        List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelations
                = accountConsumeSceneStoreRelationDao.queryBySceneIdsAndStoreNo(sceneIds, storeCode);
        if(CollectionUtils.isEmpty(accountConsumeSceneStoreRelations)){
            log.warn("没有找到消费场景和门店关联");
            accountConsumeSceneDO.setAccountConsumeTypes(Collections.emptyList());
            return accountConsumeSceneDO;
        }
        List<String> consumeTypes = accountConsumeSceneStoreRelations.stream()
                .map(AccountConsumeSceneStoreRelation::getSceneConsumType)
                .collect(Collectors.toList());
        accountConsumeSceneDO.setAccountConsumeTypes(consumeTypes);
        return accountConsumeSceneDO;
    }

    @Override
    public AccountDO queryByQueryInfo(String queryInfo, String queryType) {
        Long accountCode = null;
        if (WelfareConstant.ConsumeQueryType.CARD.code().equals(queryType)) {
            CardInfoDao cardInfoDao = SpringBeanUtils.getBean(CardInfoDao.class);
            CardInfo cardInfo = cardInfoDao.getOneByMagneticStripe(queryInfo);
            Assert.notNull(cardInfo, "根据磁条号未找到卡片:" + queryInfo);
            Assert.isTrue(WelfareConstant.CardEnable.ENABLE.code().equals(cardInfo.getEnabled()), "卡片已禁用");
            accountCode = cardInfo.getAccountCode();
        }else if(WelfareConstant.ConsumeQueryType.BARCODE.code().equals(queryType)){
            BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
            accountCode = barcodeService.parseAccountFromBarcode(queryInfo, Calendar.getInstance().getTime(), true);
        }
        Assert.notNull(accountCode, "根据条件没有解析出账号");
        return AccountDO.of(this.getByAccountCode(accountCode));
    }

    private OrderTransRelation assemblyOrderTransRelation(BigDecimal updateQuota,
                                                          String transNo, String settlementTransNo) {
        OrderTransRelation transRelation = new OrderTransRelation();
        transRelation.setTransNo(transNo);
        transRelation.setOrderId(settlementTransNo);
        if (updateQuota.compareTo(BigDecimal.ZERO) < 0) {
            transRelation.setType(TransType.RESET_DECR.code());
        } else {
            transRelation.setType(TransType.RESET_INCR.code());
        }
        return transRelation;
    }

    private AccountBillDetail assemblyAccountBillDetail(Account account, BigDecimal updateQuota,
                                                        String transNo) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(account.getAccountCode());
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setChannel(WelfareConstant.Channel.PLATFORM.code());
        accountBillDetail.setTransNo(transNo);
        accountBillDetail.setTransAmount(updateQuota);
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        accountBillDetail.setSurplusQuota(account.getMaxQuota());
        if (updateQuota.compareTo(BigDecimal.ZERO) < 0) {
            accountBillDetail.setTransType(TransType.RESET_DECR.code());
        } else {
            accountBillDetail.setTransType(TransType.RESET_INCR.code());
        }
        return accountBillDetail;
    }

    private AccountDeductionDetail assemblyAccountDeductionDetail(Account account,
                                                                  BigDecimal updateQuota, String transNo) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(account.getAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(updateQuota.abs());
        accountDeductionDetail.setAccountAmountTypeBalance(account.getMaxQuota());
        accountDeductionDetail.setMerAccountType(MerAccountTypeCode.SURPLUS_QUOTA.code());
        accountDeductionDetail.setTransNo(transNo);
        if (updateQuota.compareTo(BigDecimal.ZERO) < 0) {
            accountDeductionDetail.setTransType(TransType.RESET_DECR.code());
        } else {
            accountDeductionDetail.setTransType(TransType.RESET_INCR.code());
        }
        accountDeductionDetail.setTransAmount(updateQuota.abs());
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(Calendar.getInstance().getTime());
        accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
        accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setChanel(WelfareConstant.Channel.PLATFORM.code());
        accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
        return accountDeductionDetail;
    }
}