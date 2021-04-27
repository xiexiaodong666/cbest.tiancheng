package com.welfare.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.welfare.common.constants.AccountBindStatus;
import com.welfare.common.constants.AccountChangeType;
import com.welfare.common.constants.AccountStatus;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.constants.WelfareConstant.CardStatus;
import com.welfare.common.constants.WelfareConstant.MerAccountTypeCode;
import com.welfare.common.constants.WelfareConstant.TransType;
import com.welfare.common.domain.MerchantUserInfo;
import com.welfare.common.enums.ConsumeTypeEnum;
import com.welfare.common.enums.FileUniversalStorageEnum;
import com.welfare.common.enums.ShoppingActionTypeEnum;
import com.welfare.common.exception.BizAssert;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.common.util.*;
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
import com.welfare.service.converter.DepartmentAndAccountTreeConverter;
import com.welfare.service.dto.*;
import com.welfare.service.dto.accountapply.AccountDepositApprovalRequest;
import com.welfare.service.dto.accountapply.DepositApplyRequest;
import com.welfare.service.enums.AccountBalanceType;
import com.welfare.service.enums.AccountBalanceType.Welfare;
import com.welfare.service.enums.AccountBalanceType.WoLife;
import com.welfare.service.enums.ApprovalStatus;
import com.welfare.service.enums.ApprovalType;
import com.welfare.service.listener.AccountBatchBindCardListener;
import com.welfare.service.listener.AccountUploadListener;
import com.welfare.service.remote.ShoppingFeignClient;
import com.welfare.service.remote.entity.*;
import com.welfare.service.remote.entity.response.WoLifeBasicResponse;
import com.welfare.service.remote.entity.response.WoLifeGetUserMoneyResponse;
import com.welfare.service.remote.service.CbestPayService;
import com.welfare.service.remote.service.WoLifeFeignService;
import com.welfare.service.sync.event.AccountEvt;
import com.welfare.service.utils.AccountUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.welfare.service.utils.TreeUtil;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import static com.welfare.common.constants.RedisKeyConstant.ACCOUNT_AMOUNT_TYPE_OPERATE;

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
    private MerchantService merchantService;
    @Autowired
    private DepartmentService departmentService;
    private final SequenceService sequenceService;
    private final CardInfoService cardInfoService;
    @Autowired
    private AccountChangeEventRecordService accountChangeEventRecordService;
    private final AccountChangeEventRecordCustomizeMapper accountChangeEventRecordCustomizeMapper;
    private final ApplicationContext applicationContext;
    @Autowired
    private MerchantAccountTypeService merchantAccountTypeService;
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
    @Autowired
    private DepartmentAndAccountTreeConverter andAccountTreeConverter;
    private final AccountAmountTypeDao accountAmountTypeDao;
    private final AccountChangeEventRecordDao accountChangeEventRecordDao;

    private final SubAccountDao subAccountDao;
    @Autowired
    private PaymentChannelService paymentChannelService;
    private final PaymentChannelDao paymentChannelDao;
    private final MerchantCreditService merchantCreditService;
    private final static Map<String, WelfareConstant.PaymentChannel> PAYMENT_CHANNEL_MAP = Stream
        .of(WelfareConstant.PaymentChannel.values()).collect(Collectors
            .toMap(WelfareConstant.PaymentChannel::code,
                e -> e));

    private final WoLifeFeignService woLifeFeignService;
    private final DepartmentDao departmentDao;
    private final MerchantDao merchantDao;
    private final AccountTypeDao accountTypeDao;
    @Autowired
    private AccountAmountTypeService accountAmountTypeService;

    private final CbestPayService cbestPayService;
    private final MerchantExtendDao merchantExtendDao;
    @Autowired
    private AccountDepositApplyService depositApplyService;

    @Override
    public Page<AccountDTO> getPageDTO(Page<AccountPageDTO> page, AccountPageReq accountPageReq) {
        IPage<AccountPageDTO> iPage = accountCustomizeMapper
                .queryPageDTO(page, accountPageReq.getMerCode(), accountPageReq.getAccountName(),
                        accountPageReq.getDepartmentPathList(), accountPageReq.getAccountStatus(),
                        accountPageReq.getAccountTypeCodes(), accountPageReq.getBinding(),
                        accountPageReq.getCardId(),
                        accountPageReq.getPhone(),
                        accountPageReq.getAccountBalanceMin(),
                        accountPageReq.getAccountBalanceMax(),
                        accountPageReq.getSurplusQuotaMin(),
                        accountPageReq.getSurplusQuotaMax());

        return accountConverter.toPage(iPage);
    }

    @Override
    public AccountPageExtDTO getPageExtDTO(AccountPageReq accountPageReq) {
        AccountPageExtDTO accountPageExtDTO = accountCustomizeMapper
            .queryPageExtDTO(accountPageReq.getMerCode(), accountPageReq.getAccountName(),
                             accountPageReq.getDepartmentPathList(), accountPageReq.getAccountStatus(),
                             accountPageReq.getAccountTypeCodes(), accountPageReq.getBinding(),
                             accountPageReq.getCardId(),
                             accountPageReq.getPhone(),
                             accountPageReq.getAccountBalanceMin(),
                             accountPageReq.getAccountBalanceMax(),
                             accountPageReq.getSurplusQuotaMin(),
                             accountPageReq.getSurplusQuotaMax());
        return accountPageExtDTO;
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
                accountPageReq.getAccountTypeCodes(), accountPageReq.getBinding(),
                accountPageReq.getCardId(),
                accountPageReq.getPhone(),
                          accountPageReq.getAccountBalanceMin(),
                          accountPageReq.getAccountBalanceMax(),
                          accountPageReq.getSurplusQuotaMin(),
                          accountPageReq.getSurplusQuotaMax());
        return accountConverter.toAccountDTOList(list);
    }

    @Override
    public String uploadAccount(MultipartFile multipartFile) throws IOException {
        try {
            AccountUploadListener listener = new AccountUploadListener(accountTypeService, this,
                merchantService, departmentService, sequenceService, accountTypeDao, departmentDao, accountDao);
            EasyExcel.read(multipartFile.getInputStream(), AccountUploadDTO.class, listener).sheet()
                .doRead();
            String result = listener.getUploadInfo().toString();
            listener.getUploadInfo().delete(0, listener.getUploadInfo().length());
            return result;
        } catch (Exception e) {
            log.info("批量新增员工解析 Excel exception:{}", e.getMessage());
            throw e;
        }
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
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "员工账户不存在", null);
        }
        boolean result = accountDao.removeById(id);
        subAccountDao.deleteAccountCode(syncAccount.getAccountCode());
        accountChangeEvtRecoed(AccountChangeType.ACCOUNT_DELETE, syncAccount.getAccountCode());
        syncAccount.setDeleted(System.currentTimeMillis());
        applicationContext.publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.DELETE)
            .accountList(Arrays.asList(syncAccount)).build());
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean active(Long id, Integer accountStatus) {
        Account syncAccount = accountMapper.selectById(id);
        if (null == syncAccount) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "员工账户不存在", null);
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
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "该员工账号不存在", null);
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
        List<AccountBatchImgInfoReq> batchImgInfoListReq = accountBatchImgReq
            .getAccountBatchImgInfoReqList();

        batchImgInfoListReq = batchImgInfoListReq.stream().collect(
            Collectors.collectingAndThen(Collectors
                    .toCollection(() -> new TreeSet<>(Comparator.comparing(o -> o.getPhone()))),
                ArrayList::new));

        List<String> phones = batchImgInfoListReq.stream().map(b -> b.getPhone())
            .collect(Collectors.toList());

        QueryWrapper<Account> accountQueryWrapper = new QueryWrapper<>();
        accountQueryWrapper.in(Account.PHONE, phones);
        accountQueryWrapper.eq(Account.MER_CODE, merCode);
        List<Account> accountList = accountDao.list(accountQueryWrapper);

        List<FileUniversalStorage> fileUniversalStorageList = new ArrayList<>();

        for (Account account : accountList) {
            AccountBatchImgInfoReq accountOptional = batchImgInfoListReq.stream()
                .filter(i -> i.getPhone().equals(account.getPhone())).findFirst().get();
            successList.add(accountOptional.getPhone());
            FileUniversalStorage fileUniversalStorage;
            if (account.getFileUniversalStorageId() != null
                && account.getFileUniversalStorageId() != 0) {
                fileUniversalStorage = fileUniversalStorageDao
                    .getById(account.getFileUniversalStorageId());
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
        for (int i = 0; i < accountList.size(); i++) {
            accountList.get(i)
                .setFileUniversalStorageId(map.get(accountList.get(i).getPhone()).getId());
        }
        accountDao.updateBatchById(accountList);

        List<UploadImgErrorMsgDTO> uploadImgErrorMsgDTOList = new ArrayList<>();
        accountBatchImgDTO.setSuccessList(successList);

        if (CollectionUtils.isNotEmpty(failList)) {
            for (String phone :
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
        if (accountReq.getCredit() != null && accountReq.getCredit()) {
            //授信额度
            AccountAmountType accountAmountType = getAccountAmountType(account.getAccountCode(),
                account.getMaxQuota(), account.getMerCode(),
                MerAccountTypeCode.SURPLUS_QUOTA.code());
            accountAmountTypeMapper.insert(accountAmountType);
            //授信额度溢缴款,额度没有上限
            AccountAmountType accountAmountType2 = getAccountAmountType(account.getAccountCode(),
                BigDecimal.ZERO, account.getMerCode(),
                MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY.code());
            accountAmountTypeMapper.insert(accountAmountType2);
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
        Merchant merchant = merchantService.getMerchantByMerCode(accountReq.getMerCode());
        BizAssert.notNull(merchant, ExceptionCode.ILLEGALITY_ARGUMENTS, "商户不存在");
        MerchantExtend merchantExtend = merchantExtendDao.getByMerCode(accountReq.getMerCode());
        if(Objects.nonNull(merchantExtend) && !StringUtils.isEmpty(merchantExtend.getIndustryTag())
                && merchantExtend.getIndustryTag().contains(WelfareConstant.IndustryTag.COMMUNITY_HOSPITAL.code())) {
            //批发福利账户
            AccountAmountType accountAmountType = getAccountAmountType(account.getAccountCode(),
                    BigDecimal.ZERO, account.getMerCode(),
                    MerAccountTypeCode.WHOLESALE.code());
            accountAmountTypeMapper.insert(accountAmountType);
            SubAccount subAccount = new SubAccount();
            subAccount.setAccountCode(account.getAccountCode());
            subAccount.setSubAccountType(WelfareConstant.PaymentChannel.WELFARE.code());
            subAccountDao.save(subAccount);
        } else {
            PaymentChannelReq req = new PaymentChannelReq();
            req.setFiltered(false);
            req.setMerCode(accountReq.getMerCode());
            List<PaymentChannelDTO> paymentChannels = paymentChannelService.list(req);
            subAccountDao.saveBatch(assemableSubAccount(paymentChannels, account));
        }
        applicationContext.publishEvent(AccountEvt.builder().typeEnum(ShoppingActionTypeEnum.ADD)
            .accountList(Arrays.asList(account)).build());
        return result;
    }

    private List<SubAccount> assemableSubAccount(List<PaymentChannelDTO> paymentChannels, Account account) {
        List<SubAccount> subAccounts = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(paymentChannels)) {
            paymentChannels.forEach(paymentChannel -> {
                SubAccount subAccount = new SubAccount();
                subAccount.setSubAccountType(paymentChannel.getPaymentChannelCode());
                subAccount.setAccountCode(account.getAccountCode());
                subAccounts.add(subAccount);
            });
        }
        return subAccounts;
    }

    private Account assemableAccount(AccountReq accountReq) {
        Account account = new Account();
        Long accounCode = sequenceService.nextNo(WelfareConstant.SequenceType.ACCOUNT_CODE.code());
        BeanUtils.copyProperties(accountReq, account);
        account.setCreateUser(UserInfoHolder.getUserInfo().getUserName());
        account.setDepartment(accountReq.getDepartmentCode());
        account.setAccountCode(accounCode);
        return account;
    }

    private AccountAmountType getAccountAmountType(Long accountCode, BigDecimal accountBalance,
        String merCode, String merAccountTypeCode) {
        MerchantAccountType merchantAccountType = merchantAccountTypeService.queryOneByCode(
            merCode,
            merAccountTypeCode);
        if (null == merchantAccountType) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "商户无福利类型", null);
        }
        AccountAmountType accountAmountType = new AccountAmountType();
        accountAmountType.setAccountCode(accountCode);
        accountAmountType.setAccountBalance(accountBalance);
        accountAmountType.setCreateTime(new Date());
        accountAmountType.setCreateUser(UserInfoHolder.getUserInfo().getUserName());
        accountAmountType.setMerAccountTypeCode(merAccountTypeCode);
        return accountAmountType;
    }

    private void validationAccount(Account account, Boolean isNew) {
        Merchant merchant = merchantService.detailByMerCode(account.getMerCode());
        if (null == merchant) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "商户不存在", null);
        }
        AccountType accountType = accountTypeService
            .queryByTypeCode(account.getMerCode(), account.getAccountTypeCode());
        if (null == accountType) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "员工类型不存在", null);
        }
        Department department = departmentService.getByDepartmentCode(account.getDepartment());
        if (null == department) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "员工部门不存在", null);
        }
        if (isNew) {
            Account queryAccount = this.findByPhone(account.getPhone(), account.getMerCode());
            if (null != queryAccount) {
                throw new BizException(ExceptionCode.ACCOUNT_ALREADY_EXIST, "员工手机号已经存在", null);
            }
        } else {
            Account queryAccount = this.findByPhone(account.getPhone(), account.getMerCode());
            if (queryAccount != null && queryAccount.getId().longValue() != account.getId()
                .longValue()) {
                throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "已经有其他员工绑定了该手机", null);
            }
            Account syncAccount = accountMapper.selectById(account.getId());
            if (null == syncAccount) {
                throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "员工账户不存在", null);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSave(List<Account> accountList) {
        PaymentChannelReq req = new PaymentChannelReq();
        req.setMerCode(MerchantUserHolder.getMerchantUser().getMerchantCode());
        req.setFiltered(false);
        List<PaymentChannelDTO> paymentChannelDTOS = paymentChannelService.list(req);
        return accountDao.saveBatch(accountList)
                && subAccountDao.saveBatch(AccountUtils.assemableSubAccount(accountList, paymentChannelDTOS));
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
            if (oldAccount.getFileUniversalStorageId() != null) {
                FileUniversalStorage fileUniversalStorage = fileUniversalStorageDao
                    .getById(oldAccount.getFileUniversalStorageId());
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
        account.setDepartment(accountReq.getDepartmentCode());
        account.setUpdateUser(MerchantUserHolder.getMerchantUser().getUsername());
        return account;
    }

    private void updateSurPlusQuota(Long accountCode, BigDecimal oldMaxQuota,
        BigDecimal surplusQuota,
        BigDecimal newMaxQuota, String updateUser, Boolean credit, String merCode) {

        if (credit) {
            AccountAmountTypeService accountAmountTypeService = SpringBeanUtils
                .getBean(AccountAmountTypeService.class);
            AccountAmountType accountAmountType = accountAmountTypeService
                .queryOne(accountCode, MerAccountTypeCode.SURPLUS_QUOTA.code());
            if (null == accountAmountType) {
                //账户新增授信额度 修改account额度
                AccountAmountType addAccountAmountType = getAccountAmountType(accountCode,
                    newMaxQuota,
                    merCode, MerAccountTypeCode.SURPLUS_QUOTA.code());
                accountAmountTypeMapper.insert(addAccountAmountType);
                accountCustomizeMapper
                    .updateMaxAndSurplusQuota(accountCode.toString(), newMaxQuota, newMaxQuota,
                        updateUser);
            } else {
                //判断剩余授信额度  如果是增加,那么额度就是
                if (surplusQuota.compareTo(oldMaxQuota.subtract(newMaxQuota)) < 0) {
                    throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "剩余授信额度不足", null);
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
                    throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "剩余授信额度不足", null);
                }
            }
        } else {
            //关闭授信额度,账户以及账户金额类型表变0
            if (oldMaxQuota.compareTo(surplusQuota) != 0) {
                throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "授信额度已使用 无法关闭", null);
            }
            accountAmountTypeMapper
                .updateBalance(accountCode, MerAccountTypeCode.SURPLUS_QUOTA.code(),
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
            sequenceService
                .nextNo(WelfareConstant.SequenceType.RESET_ACCOUNT_SURPLUS_QUOTA.code()));
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
        accountBillDetail.setSurplusQuotaOverpay(account.getSurplusQuotaOverpay());
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
        String storeCode = account.getDepartment();
        List<Department> parentDepartmentList = getParentDepartmentList(storeCode);
        accountSimpleDTO.setMerName(
            parentDepartmentList.size() > 1 ? parentDepartmentList.get(1).getDepartmentName()
                : parentDepartmentList.get(0).getDepartmentName());
        accountSimpleDTO.setPhone(account.getPhone());
        accountSimpleDTO.setAccountName(account.getAccountName());
        accountSimpleDTO.setAccountBalance(account.getAccountBalance());
        accountSimpleDTO.setMaxQuota(account.getMaxQuota());
        BigDecimal surplusQuota = account.getSurplusQuota();
        BigDecimal surplusQuotaOverpay = account.getSurplusQuotaOverpay();
        accountSimpleDTO.setSurplusQuota(surplusQuota != null && surplusQuotaOverpay != null
            ? surplusQuota.add(surplusQuotaOverpay) : surplusQuota);
        accountSimpleDTO.setSurplusQuotaOverpay(surplusQuotaOverpay);
        accountSimpleDTO.setCredit(account.getCredit());
        return accountSimpleDTO;
    }

    private <T> AccountBalanceDTO getAccountBalanceValue(AccountBalanceType<T> accountBalanceType,
        T t) {
        AccountBalanceDTO accountBalanceDTO = new AccountBalanceDTO();
        accountBalanceDTO.setCode(accountBalanceType.toString());
        accountBalanceDTO.setName(accountBalanceType.getName());
        if (t != null) {
            Object fieldValue = accountBalanceType.getFieldFunc().apply(t);
            accountBalanceDTO.setValue(fieldValue);
        }
        return accountBalanceDTO;
    }

    @Override
    public AccountOverviewDTO queryAccountOverview(Long accountCode, String paymentChannel) {
        Account account = getByAccountCode(accountCode);
        WelfareConstant.PaymentChannel paymentChannelEnum =
            StrUtil.isEmpty(paymentChannel) ? WelfareConstant.PaymentChannel.WELFARE
                : PAYMENT_CHANNEL_MAP.get(paymentChannel);
        List<AccountBalanceDTO> balanceList = new ArrayList<>();
        String queryErrorMsg = null;
        switch (paymentChannelEnum) {
            case WELFARE:
                balanceList = Lists.newArrayList(Welfare.values())
                    .stream()
                    .map(welfare -> getAccountBalanceValue(welfare, account))
                    .collect(Collectors.toList());
                break;
            case WO_LIFE:
                String phone = account.getPhone();
                WoLifeBasicResponse<WoLifeGetUserMoneyResponse> woLifeBasicResponse = woLifeFeignService
                    .getUserMoney(phone);
                if (!woLifeBasicResponse.isSuccess()) {
                    log.error("账户余额查询返回失败, phone: {}, response: {}", phone,
                        JSON.toJSONString(woLifeBasicResponse));
                    queryErrorMsg = StrUtil.format("查询失败，{}", woLifeBasicResponse.getResponseMessage());
                }
                WoLifeGetUserMoneyResponse woLifeGetUserMoneyResponse = woLifeBasicResponse
                    .getResponse();
                balanceList = Lists.newArrayList(WoLife.values())
                    .stream()
                    .map(woLife -> getAccountBalanceValue(woLife, woLifeGetUserMoneyResponse))
                    .collect(Collectors.toList());
                break;
        }

        List<SubAccount> subAccountList = subAccountDao.getBaseMapper()
            .selectList(Wrappers.<SubAccount>lambdaQuery()
                .eq(SubAccount::getAccountCode, accountCode)
                .eq(SubAccount::getDeleted, 0));
        List<AccountPaymentChannelDTO> paymentChannelList = subAccountList.stream()
            .map(subAccount -> {
                AccountPaymentChannelDTO accountPaymentChannelDTO = new AccountPaymentChannelDTO();
                String subAccountType = subAccount.getSubAccountType();
                accountPaymentChannelDTO.setPaymentChannel(subAccountType);
                WelfareConstant.PaymentChannel channel = PAYMENT_CHANNEL_MAP
                    .get(subAccountType);
                accountPaymentChannelDTO.setPaymentChannelDesc(channel.desc());
                return accountPaymentChannelDTO;
            }).collect(Collectors.toList());

        AccountOverviewDTO accountOverviewDTO = new AccountOverviewDTO();
        String storeCode = account.getDepartment();
        List<Department> parentDepartmentList = getParentDepartmentList(storeCode);
        accountOverviewDTO.setMerName(
            parentDepartmentList.size() > 1 ? parentDepartmentList.get(1).getDepartmentName()
                : parentDepartmentList.get(0).getDepartmentName());
        accountOverviewDTO.setPhone(account.getPhone());
        accountOverviewDTO.setAccountName(account.getAccountName());
        accountOverviewDTO.setCredit(account.getCredit());
        accountOverviewDTO.setPaymentChannel(paymentChannelEnum.code());
        accountOverviewDTO.setPaymentChannelDesc(paymentChannelEnum.desc());
        accountOverviewDTO.setBalanceList(balanceList);
        accountOverviewDTO.setPaymentChannelList(paymentChannelList);
        accountOverviewDTO.setQueryErrorMsg(queryErrorMsg);
        return accountOverviewDTO;
    }

    @Override
    public List<AccountPaymentChannelDTO> queryPaymentChannelList(Long accountCode) {
        Account account = getByAccountCode(accountCode);
        List<PaymentChannel> channelList = paymentChannelDao.getBaseMapper().selectList(
            Wrappers.<PaymentChannel>lambdaQuery()
                .eq(PaymentChannel::getMerchantCode, account.getMerCode())
                .eq(PaymentChannel::getDeleted, 0)
                .groupBy(PaymentChannel::getCode)
                .orderByAsc(PaymentChannel::getShowOrder));
        if(CollectionUtil.isEmpty(channelList)) {
            channelList = paymentChannelDao.listByDefaultGroupByCode();
        }

        List<String> paymentChannelCodeList = channelList.stream().map(PaymentChannel::getCode)
            .collect(Collectors.toList());

        List<SubAccount> subAccountList = subAccountDao.getBaseMapper().selectList(
            Wrappers.<SubAccount>lambdaQuery().eq(SubAccount::getAccountCode, accountCode)
                .in(SubAccount::getSubAccountType, paymentChannelCodeList));
        final Map<String, String> subAccountMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(subAccountList)) {
            for (SubAccount subAccount : subAccountList) {
                subAccountMap.put(subAccount.getSubAccountType(), subAccount.getPasswordFreeSignature());
            }
        }

        List<AccountPaymentChannelDTO> paymentChannelList = channelList.stream()
            .map(paymentChannel -> {
                String code = paymentChannel.getCode();
                AccountPaymentChannelDTO accountPaymentChannelDTO = new AccountPaymentChannelDTO();
                accountPaymentChannelDTO.setPaymentChannel(paymentChannel.getCode());
                accountPaymentChannelDTO.setPaymentChannelDesc(paymentChannel.getName());
                String passwordFreeSignature = subAccountMap.get(code);
                accountPaymentChannelDTO.setPasswordFree(passwordFreeSignature != null);
                return accountPaymentChannelDTO;
            }).collect(Collectors.toList());
        return paymentChannelList;
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
    public List<Account> queryByAccountTypeCode(List<String> accountTypeCode) {
        List<Account> list = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(accountTypeCode)) {
            QueryWrapper<Account> queryWrapper = new QueryWrapper<Account>();
            queryWrapper.in(Account.ACCOUNT_TYPE_CODE, accountTypeCode);
            list = accountDao.list(queryWrapper);
        }
        return list;
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
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "员工账户不存在", null);
        }
        CardInfo cardInfo = cardInfoService.getByCardNo(cardId);
        if (null == cardInfo) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "该卡号不存在", null);
        }
        if (cardInfoService.cardIsBind(cardId)) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "该卡号已经绑定其他账号", null);
        }
        if (cardInfo.getCardStatus().intValue() != CardStatus.WRITTEN.code().intValue()) {
            throw new BizException(ExceptionCode.ILLEGALITY_ARGUMENTS, "请确认该卡片状态", null);
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
    public AccountConsumeSceneDO queryAccountConsumeSceneDO(String storeCode,
        WelfareConstant.ConsumeQueryType queryType, String queryInfo) {
        Long accountCode = null;
        AccountConsumeSceneDO accountConsumeSceneDO = new AccountConsumeSceneDO();
        accountConsumeSceneDO.setQueryInfo(queryInfo);
        accountConsumeSceneDO.setQueryInfoType(queryType.code());
        switch (queryType) {
            case CARD:
                CardInfoDao cardInfoDao = SpringBeanUtils.getBean(CardInfoDao.class);
                CardInfo cardInfo = cardInfoDao.getOneByMagneticStripe(queryInfo);
                Assert.notNull(cardInfo, "根据磁条号未找到卡片:" + queryInfo);
                Assert
                    .isTrue(WelfareConstant.CardEnable.ENABLE.code().equals(cardInfo.getEnabled()),
                        "卡片已禁用");
                accountCode = cardInfo.getAccountCode();
                break;
            case BARCODE:
                BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
                accountCode = barcodeService
                    .parseAccountFromBarcode(queryInfo, Calendar.getInstance().getTime(), true, false);
                break;
            default:
                break;
        }
        Assert.notNull(accountCode, "根据条件没有解析出账号");
        Account account = this.getByAccountCode(accountCode);
        accountConsumeSceneDO.setAccount(account);
        SupplierStore supplierStore = supplierStoreDao.getOneByCode(storeCode);
        List<AccountConsumeScene> accountConsumeScenes
            = accountConsumeSceneDao
            .getAccountTypeAndMerCode(account.getAccountTypeCode(), supplierStore.getMerCode());
        if (CollectionUtils.isEmpty(accountConsumeScenes)) {
            log.warn("没有找到消费场景");
            accountConsumeSceneDO.setAccountConsumeTypes(Collections.emptyList());
            return accountConsumeSceneDO;
        }
        List<Long> sceneIds = accountConsumeScenes.stream()
            .map(AccountConsumeScene::getId).collect(Collectors.toList());
        List<AccountConsumeSceneStoreRelation> accountConsumeSceneStoreRelations
            = accountConsumeSceneStoreRelationDao.queryBySceneIdsAndStoreNo(sceneIds, storeCode);
        if (CollectionUtils.isEmpty(accountConsumeSceneStoreRelations)) {
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
    public AccountDO queryByQueryInfo(String queryInfo, String queryType,Date transDate) {
        Long accountCode = null;
        if (WelfareConstant.ConsumeQueryType.CARD.code().equals(queryType)) {
            CardInfoDao cardInfoDao = SpringBeanUtils.getBean(CardInfoDao.class);
            CardInfo cardInfo = cardInfoDao.getOneByMagneticStripe(queryInfo);
            Assert.notNull(cardInfo, "根据磁条号未找到卡片:" + queryInfo);
            Assert.isTrue(WelfareConstant.CardEnable.ENABLE.code().equals(cardInfo.getEnabled()),
                "卡片已禁用");
            accountCode = cardInfo.getAccountCode();
        } else if (WelfareConstant.ConsumeQueryType.BARCODE.code().equals(queryType)) {
            BarcodeService barcodeService = SpringBeanUtils.getBean(BarcodeService.class);
            accountCode = barcodeService
                .parseAccountFromBarcode(queryInfo, Calendar.getInstance().getTime(), true, false);
        }
        Assert.notNull(accountCode, "根据条件没有解析出账号");
        Account account = this.getByAccountCode(accountCode);
        Assert.notNull(account,"找不到账号:"+accountCode);
        Department department = departmentDao.queryByCode(account.getDepartment());
        Merchant merchant = merchantDao.queryByCode(account.getMerCode());
        return AccountDO.of(account, department, merchant);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchRestoreCreditLimit(AccountRestoreCreditLimitReq creditLimitReq) {
        if (creditLimitReq != null && CollectionUtils
            .isNotEmpty(creditLimitReq.getCreditLimitDtos())) {
            List<AccountRestoreCreditLimitReq.RestoreCreditLimitDTO> restoreCreditLimitDtos = creditLimitReq
                .getCreditLimitDtos();
            List<Long> accountCodes = restoreCreditLimitDtos.stream()
                .map(AccountRestoreCreditLimitReq.RestoreCreditLimitDTO::getAccountCode)
                .collect(Collectors.toList());
            ;
            List<RLock> locks = new ArrayList<>();
            RLock multiLock;
            accountCodes.forEach(accountCode -> {
                RLock lock = redissonClient.getFairLock(ACCOUNT_AMOUNT_TYPE_OPERATE + accountCode);
                locks.add(lock);
            });
            multiLock = redissonClient.getMultiLock(locks.toArray(new RLock[]{}));
            multiLock.lock(-1, TimeUnit.SECONDS);
            try {
                Map<Long, Account> accountsMap = accountDao.mapByAccountCodes(accountCodes);
                Map<Long, AccountAmountType> surplusQuotaMap = accountAmountTypeDao
                    .mapByAccountCodes(accountCodes, MerAccountTypeCode.SURPLUS_QUOTA.code());
                Map<Long, AccountAmountType> surplusQuotaOverpayMap = accountAmountTypeDao
                    .mapByAccountCodes(accountCodes,
                        MerAccountTypeCode.SURPLUS_QUOTA_OVERPAY.code());
                Map<Long, AccountRestoreCreditLimitReq.RestoreCreditLimitDTO> restoreCreditLimitMap = restoreCreditLimitDtos
                    .stream()
                    .collect(Collectors
                        .toMap(AccountRestoreCreditLimitReq.RestoreCreditLimitDTO::getAccountCode,
                            a -> a, (k1, k2) -> k1));
                List<Account> updatedAccounts = new ArrayList<>();
                List<AccountAmountType> updatedAccountTypes = new ArrayList<>();
                List<AccountBillDetail> accountBillDetails = new ArrayList<>();
                List<AccountDeductionDetail> accountDeductionDetails = new ArrayList<>();
                List<OrderTransRelation> orderTransRelations = new ArrayList<>();
                List<AccountChangeEventRecord> records = new ArrayList<>();

                restoreCreditLimitDtos.forEach(restoreLimit -> {
                    Long accountCode = restoreLimit.getAccountCode();
                    BigDecimal restoreAmount = restoreLimit.getRestoreAmount();
                    Account account = accountsMap.get(accountCode);
                    if (account == null) {
                        throw new BizException(String.format("员工不存在[%s]", accountCode));
                    }
                    AccountAmountType surplusQuota = surplusQuotaMap.get(accountCode);
                    if (Objects.isNull(surplusQuota)) {
                        throw new BizException(String.format("员工授信额度账户不存在[%s]", accountCode));
                    }
                    // 溢缴款账户
                    AccountAmountType surplusQuotaOverpay = surplusQuotaOverpayMap.get(accountCode);
                    if (Objects.isNull(surplusQuotaOverpay)) {
                        throw new BizException(String.format("员工溢缴款账户不存在[%s]", accountCode));
                    }
                    if (surplusQuota.getAccountBalance().compareTo(account.getMaxQuota()) > 0
                        || account.getSurplusQuota().compareTo(account.getMaxQuota()) > 0) {
                        throw new BizException(
                            String.format("数据异常，剩余授信额度超过最大额度[%s]", accountCode));
                    }
                    BigDecimal overAmount = surplusQuota.getAccountBalance().add(restoreAmount)
                        .subtract(account.getMaxQuota());
                    if (overAmount.compareTo(BigDecimal.ZERO) < 0) {
                        // 恢复授信额度
                        surplusQuota
                            .setAccountBalance(surplusQuota.getAccountBalance().add(restoreAmount));
                        account.setSurplusQuota(account.getSurplusQuota().add(restoreAmount));
                        updatedAccounts.add(account);
                        updatedAccountTypes.add(surplusQuota);
                        // 保存变更流水
                        AccountBillDetail billDetail = assemblyAccountBillDetail(account,
                            restoreAmount, "", TransType.RESET_INCR,
                            surplusQuotaOverpay.getAccountBalance());
                        AccountDeductionDetail deductionDetail = assemblyAccountDeductionDetail(
                            account,
                            restoreAmount,
                            "",
                            TransType.RESET_INCR,
                            surplusQuota);
                        accountBillDetails.add(billDetail);
                        accountDeductionDetails.add(deductionDetail);
                    } else {
                        if (surplusQuota.getAccountBalance().compareTo(account.getMaxQuota()) < 0) {
                            // 变更金额
                            BigDecimal updateAmount = account.getMaxQuota()
                                .subtract(surplusQuota.getAccountBalance());
                            // 恢复授信额度
                            surplusQuota.setAccountBalance(account.getMaxQuota());
                            account.setSurplusQuota(account.getMaxQuota());
                            updatedAccounts.add(account);
                            updatedAccountTypes.add(surplusQuota);
                            AccountBillDetail billDetail = assemblyAccountBillDetail(account,
                                updateAmount, "", TransType.RESET_INCR,
                                surplusQuotaOverpay.getAccountBalance());
                            AccountDeductionDetail deductionDetail = assemblyAccountDeductionDetail(
                                account, updateAmount, "", TransType.RESET_INCR, surplusQuota);
                            accountBillDetails.add(billDetail);
                            accountDeductionDetails.add(deductionDetail);
                        }
                        if (overAmount.compareTo(BigDecimal.ZERO) > 0) {
                            surplusQuotaOverpay.setAccountBalance(
                                surplusQuotaOverpay.getAccountBalance().add(overAmount.abs()));
                            updatedAccountTypes.add(surplusQuotaOverpay);
                            account.setSurplusQuotaOverpay(
                                account.getSurplusQuotaOverpay().add(overAmount.abs()));
                            updatedAccounts.add(account);
                            // 保存变更流水
                            AccountBillDetail billDetail = assemblyAccountBillDetail(account,
                                overAmount.abs(), "", TransType.RESET_INCR,
                                surplusQuotaOverpay.getAccountBalance());
                            AccountDeductionDetail deductionDetail = assemblyAccountDeductionDetail(
                                account, overAmount.abs(), "", TransType.RESET_INCR,
                                surplusQuotaOverpay);
                            accountBillDetails.add(billDetail);
                            accountDeductionDetails.add(deductionDetail);
                        }
                    }
                    AccountChangeEventRecord accountChangeEventRecord = new AccountChangeEventRecord();
                    accountChangeEventRecord.setAccountCode(account.getAccountCode());
                    accountChangeEventRecord
                        .setChangeType(AccountChangeType.ACCOUNT_SETTLE_RESTORE.getChangeType());
                    accountChangeEventRecord
                        .setChangeValue(AccountChangeType.ACCOUNT_SETTLE_RESTORE.getChangeValue());
                    records.add(accountChangeEventRecord);
                });
                BatchSequence batchSequence = sequenceService
                    .batchGenerate(WelfareConstant.SequenceType.DEPOSIT.code(),
                        accountBillDetails.size() + accountDeductionDetails.size());
                int sequenceIndex = 0;
                for (AccountBillDetail billDetail : accountBillDetails) {
                    String transNo =
                        batchSequence.getSequences().get(sequenceIndex++).getSequenceNo() + "";
                    String settleNo = restoreCreditLimitMap.get(billDetail.getAccountCode())
                        .getSettleNo();
                    billDetail.setTransNo(transNo);
                    OrderTransRelation orderTransRelation = assemblyOrderTransRelation(
                        billDetail.getTransAmount(), transNo, settleNo, TransType.RESET_INCR);
                    orderTransRelations.add(orderTransRelation);
                }
                for (AccountDeductionDetail deductionDetail : accountDeductionDetails) {
                    String transNo =
                        batchSequence.getSequences().get(sequenceIndex++).getSequenceNo() + "";
                    String settleNo = restoreCreditLimitMap.get(deductionDetail.getAccountCode())
                        .getSettleNo();
                    deductionDetail.setTransNo(transNo);
                    OrderTransRelation orderTransRelation = assemblyOrderTransRelation(
                        deductionDetail.getTransAmount(), transNo, settleNo, TransType.RESET_INCR);
                    orderTransRelations.add(orderTransRelation);
                }
                accountDeductionDetailDao
                    .saveBatch(accountDeductionDetails, accountDeductionDetails.size());
                accountBillDetailDao.saveBatch(accountBillDetails, accountBillDetails.size());
                orderTransRelationDao.saveBatch(orderTransRelations, orderTransRelations.size());
                accountChangeEventRecordDao.saveBatch(records, records.size());
                Map<Long, AccountChangeEventRecord> recordMap = records.stream().collect(Collectors
                    .toMap(AccountChangeEventRecord::getAccountCode, a -> a, (k1, k2) -> k1));
                updatedAccounts.forEach(account -> {
                    account.setChangeEventId(recordMap.get(account.getAccountCode()).getId());
                });
                boolean flag1 = true;
                boolean flag2 = true;
                if (CollectionUtils.isNotEmpty(updatedAccounts)) {
                    flag1 = accountDao.updateBatchById(updatedAccounts);
                }
                if (CollectionUtils.isNotEmpty(updatedAccountTypes)) {
                    flag2 = accountAmountTypeDao.updateBatchById(updatedAccountTypes);
                }
                if (!flag1 || !flag2) {
                    throw new BizException("操作繁忙，请稍后再试！");
                }
            } finally {
                DistributedLockUtil.unlock(multiLock);
            }
        }
    }

    private OrderTransRelation assemblyOrderTransRelation(BigDecimal updateQuota,
        String transNo, String settlementTransNo,
        WelfareConstant.TransType transType) {
        OrderTransRelation transRelation = new OrderTransRelation();
        transRelation.setTransNo(transNo);
        transRelation.setOrderId(settlementTransNo);
        transRelation.setType(transType.code());
        return transRelation;
    }

    private AccountBillDetail assemblyAccountBillDetail(Account account, BigDecimal updateQuota,
        String transNo, WelfareConstant.TransType transType,
        BigDecimal surplusQuotaOverpay) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(account.getAccountCode());
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setChannel(WelfareConstant.Channel.PLATFORM.code());
        accountBillDetail.setTransNo(transNo);
        accountBillDetail.setTransAmount(updateQuota);
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        accountBillDetail.setSurplusQuota(account.getSurplusQuota());
        accountBillDetail.setSurplusQuotaOverpay(surplusQuotaOverpay);
        accountBillDetail.setTransType(transType.code());
        return accountBillDetail;
    }

    private AccountDeductionDetail assemblyAccountDeductionDetail(Account account,
        BigDecimal updateQuota, String transNo,
        TransType transType,
        AccountAmountType accountAmountType) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(account.getAccountCode());
        accountDeductionDetail.setAccountDeductionAmount(updateQuota.abs());
        accountDeductionDetail.setAccountAmountTypeBalance(accountAmountType.getAccountBalance());
        accountDeductionDetail.setMerAccountType(accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setTransNo(transNo);
        accountDeductionDetail.setTransType(transType.code());
        accountDeductionDetail.setTransAmount(updateQuota.abs());
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(Calendar.getInstance().getTime());
        accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
        accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
        accountDeductionDetail.setChanel(WelfareConstant.Channel.PLATFORM.code());
        accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
        return accountDeductionDetail;
    }


    @Override
    public List<DepartmentAndAccountTreeResp> groupByDepartment(String merCode) {
        List<DepartmentAndAccountTreeResp> treeDTOList = andAccountTreeConverter
            .toE(accountCustomizeMapper.getAllAccountCodeAndDepatmentPath(merCode));
        treeDTOList.forEach(item -> {
            item.setCode(item.getDepartmentCode());
            item.setParentCode(item.getDepartmentParent());
        });
        TreeUtil treeUtil = new TreeUtil(treeDTOList, "0");
        treeDTOList = treeUtil.getTree();
        treeDTOList = CollectionUtils.isNotEmpty(treeDTOList) ? treeDTOList.get(0).getChildren()
            : new ArrayList<>();
        if (CollectionUtils.isNotEmpty(treeDTOList)) {
            Map<String, DepartmentAndAccountTreeResp> treeMap = treeDTOList.stream().collect(
                Collectors.toMap(DepartmentAndAccountTreeResp::getDepartmentCode, a -> a,
                    (k1, k2) -> k1));
            recursiveCalculateAccountNum(treeDTOList, treeMap);
        }
        return treeDTOList;
    }

    private void recursiveCalculateAccountNum(List<DepartmentAndAccountTreeResp> trees,
        Map<String, DepartmentAndAccountTreeResp> treeMap) {
        if (CollectionUtils.isNotEmpty(trees)) {
            trees.forEach(departmentTree -> {
                // 查询人员数量
                recursiveCalculateAccountNum(departmentTree.getChildren(), treeMap);
                // 找到父级，把本级人数加到父级上
                if (treeMap.containsKey(departmentTree.getDepartmentParent())) {
                    DepartmentAndAccountTreeResp p = treeMap
                        .get(departmentTree.getDepartmentParent());
                    p.setAccountTotal(p.getAccountTotal() + departmentTree.getAccountTotal());
                }
            });
        }
    }

    @Override
    public AccountPasswordFreePageSignDTO passwordFreePageSign(Long accountCode, String paymentChannel) {
        WelfareConstant.PaymentChannel paymentChannelEnum = PAYMENT_CHANNEL_MAP.get(paymentChannel);
        String signPage;
        switch (paymentChannelEnum) {
            case ALIPAY:
                Account account = getByAccountCode(accountCode);
                AlipayUserAgreementPageSignReq req = new AlipayUserAgreementPageSignReq();
                req.setExternalLogonId(String.valueOf(accountCode));
                AlipayUserAgreementPageSignResp alipayUserAgreementPageSignResp = cbestPayService
                    .alipayUserAgreementPageSign(req);
                signPage = alipayUserAgreementPageSignResp.getSignPage();
                break;
            default:
                throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "暂不支付此支付渠道免密签约", null);
        }

        AccountPasswordFreePageSignDTO accountPasswordFreePageSignDTO = new AccountPasswordFreePageSignDTO();
        accountPasswordFreePageSignDTO.setSignPage(signPage);
        return accountPasswordFreePageSignDTO;
    }

    @Override
    public AccountPasswordFreeSignDTO passwordFreeSign(Long accountCode, String paymentChannel, String redirectUrl) {
        WelfareConstant.PaymentChannel paymentChannelEnum = PAYMENT_CHANNEL_MAP.get(paymentChannel);
        AccountPasswordFreeSignDTO accountPasswordFreeSignDTO = new AccountPasswordFreeSignDTO();
        switch (paymentChannelEnum) {
            case ALIPAY:
                Account account = getByAccountCode(accountCode);
                AlipayUserAgreementSignReq req = new AlipayUserAgreementSignReq();
                req.setExternalLogonId(String.valueOf(accountCode));
                if (!StringUtils.isEmpty(redirectUrl)) {
                    req.setMerchantProcessUrl(redirectUrl);
                }
                AlipayUserAgreementSignResp alipayUserAgreementSignResp = cbestPayService
                    .alipayUserAgreementSign(req);
                String signParams = alipayUserAgreementSignResp.getSignParams();
                String signUrl = alipayUserAgreementSignResp.getSignUrl();
                accountPasswordFreeSignDTO.setSignUrl(signUrl);
                accountPasswordFreeSignDTO.setSignParams(signParams);
                break;
            default:
                throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "暂不支付此支付渠道免密签约", null);
        }
        return accountPasswordFreeSignDTO;
    }

    @Override
    public AccountPasswordFreeSignDTO passwordFreeUnsign(Long accountCode, String paymentChannel) {
        WelfareConstant.PaymentChannel paymentChannelEnum = PAYMENT_CHANNEL_MAP.get(paymentChannel);
        AccountPasswordFreeSignDTO accountPasswordFreeSignDTO = new AccountPasswordFreeSignDTO();
        switch (paymentChannelEnum) {
            case ALIPAY:
                Account account = getByAccountCode(accountCode);
                AlipayUserAgreementUnsignReq req = new AlipayUserAgreementUnsignReq();
                SubAccount subAccount = subAccountDao.getBaseMapper().selectOne(
                    Wrappers.<SubAccount>lambdaQuery().eq(SubAccount::getAccountCode, accountCode)
                        .eq(SubAccount::getSubAccountType,
                            WelfareConstant.PaymentChannel.ALIPAY.code()));
                req.setAgreementNo(subAccount.getPasswordFreeSignature());
                AlipayUserAgreementUnsignResp alipayUserAgreementUnsignResp = cbestPayService
                    .alipayUserAgreementUnsign(req);
                String signParams = alipayUserAgreementUnsignResp.getSignParams();
                String signUrl = alipayUserAgreementUnsignResp.getSignUrl();
                accountPasswordFreeSignDTO.setSignUrl(signUrl);
                accountPasswordFreeSignDTO.setSignParams(signParams);
                break;
            default:
                throw new BizException(ExceptionCode.UNKNOWN_EXCEPTION, "暂不支付此支付渠道免密解约", null);
        }
        return accountPasswordFreeSignDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public EmployerReqDTO saveAndDeposit(AccountSaveAndDepositReq req) {
        Merchant merchant = merchantService.getMerchantByMerCode(req.getMerCode());
        BizAssert.notNull(merchant, ExceptionCode.ILLEGALITY_ARGUMENTS, "商户不存在");
        AccountType accountType = accountTypeService.queryByTypeCode(merchant.getMerCode(),req.getAccountTypeCode());
        BizAssert.notNull(accountType, ExceptionCode.ILLEGALITY_ARGUMENTS, "员工类型不存在");
        Department department = departmentService.getByDepartmentCode(req.getDepartmentCode());
        BizAssert.notNull(department,ExceptionCode.ILLEGALITY_ARGUMENTS, "商户部门不存在");
        AccountReq accountReq = new AccountReq();
        accountReq.setMerCode(merchant.getMerCode());
        accountReq.setPhone(req.getPhone());
        accountReq.setAccountStatus(AccountStatus.getByCode(req.getAccountStatus()).getCode());
        accountReq.setAccountTypeCode(accountType.getTypeCode());
        accountReq.setCredit(req.getCredit());
        accountReq.setDepartmentCode(department.getDepartmentCode());
        accountReq.setRemark(req.getRemark());
        accountReq.setAccountName("默认名称");
        //新增员工
        try {
            save(accountReq);
        } catch (Exception exception) {
            if (exception instanceof DuplicateKeyException ||
                    ( exception instanceof BizException && ((BizException) exception).getCodeEnum() == ExceptionCode.ACCOUNT_ALREADY_EXIST )) {
                log.warn("员工已存在 req:{}", JSON.toJSONString(req), exception);
                Account account = accountDao.getByMerCodeAndPhone(req.getMerCode(), req.getPhone());
                BizAssert.notNull(accountType, ExceptionCode.ILLEGALITY_ARGUMENTS, "新增员工失败");
                return EmployerReqDTO.of(ShoppingActionTypeEnum.ADD, account, accountType, department);
            } else {
                throw exception;
            }
        }

        // 充值
        MerchantAccountType merchantAccountType = merchantAccountTypeService.queryOneByCode(merchant.getMerCode(), req.getMerAccountTypeCode());
        BizAssert.notNull(merchantAccountType,ExceptionCode.ILLEGALITY_ARGUMENTS, "福利类型不存在");

        // 申请
        DepositApplyRequest request = new DepositApplyRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setApplyRemark("注册并充值");
        request.setMerAccountTypeCode(merchantAccountType.getMerAccountTypeCode());
        request.setMerAccountTypeName(merchantAccountType.getMerAccountTypeName());
        request.setApprovalType(ApprovalType.SINGLE.getCode());
        AccountDepositRequest depositRequest = new AccountDepositRequest();
        depositRequest.setPhone(req.getPhone());
        depositRequest.setRechargeAmount(req.getAmount());
        request.setInfo(depositRequest);
        MerchantUserInfo merchantUserInfo = new MerchantUserInfo();
        merchantUserInfo.setMerchantCode(req.getMerCode());
        merchantUserInfo.setUserCode("platform");
        merchantUserInfo.setUsername("platform");
        Long applyId = depositApplyService.saveOne(request, merchantUserInfo);
        // 审核
        AccountDepositApprovalRequest approvalRequest = new AccountDepositApprovalRequest();
        approvalRequest.setId(String.valueOf(applyId));
        approvalRequest.setApprovalUser("platform");
        approvalRequest.setApprovalStatus(ApprovalStatus.AUDIT_SUCCESS.getCode());
        depositApplyService.approval(approvalRequest);

        Account account = accountDao.getByMerCodeAndPhone(req.getMerCode(), req.getPhone());
        String i = sequenceService.nextNo(WelfareConstant.SequenceType.CONSTRUCTION_BANK_AUTO_INR.code()) + "";
        account.setAccountName("建行客户" + i);
        accountDao.updateById(account);
        BizAssert.notNull(accountType, ExceptionCode.ILLEGALITY_ARGUMENTS, "新增员工失败");
        return EmployerReqDTO.of(ShoppingActionTypeEnum.ADD, account, accountType, department);
    }
}