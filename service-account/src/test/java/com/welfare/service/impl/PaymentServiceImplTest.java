package com.welfare.service.impl;

import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.entity.*;
import com.welfare.service.dto.payment.OnlinePaymentRequest;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import com.welfare.serviceaccount.BaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 2/1/2021
 */
public class PaymentServiceImplTest extends BaseTest {
    PaymentRequest paymentRequest;
    Account account;
    List<AccountAmountDO> accountAmountDOs;
    SupplierStore supplierStore;
    MerchantCredit merchantCredit;
    @Before
    public void setUp() {
        paymentRequest = new OnlinePaymentRequest();
        paymentRequest.setAccountCode(123L);
        paymentRequest.setTransNo("testTransNo");
        paymentRequest.setStoreNo("testStoreCode01");
        paymentRequest.setAmount(BigDecimal.valueOf(10));
        account = new Account();
        account.setAccountCode(123L);
        account.setAccountBalance(BigDecimal.valueOf(5));
        account.setSurplusQuota(BigDecimal.valueOf(10));
        account.setAccountTypeCode("testType");
        account.setMerCode("merCode001");
        accountAmountDOs = new ArrayList<>();
        AccountAmountType accountAmountType = new AccountAmountType();
        accountAmountType.setAccountBalance(BigDecimal.valueOf(3));
        accountAmountType.setAccountCode(account.getAccountCode());
        accountAmountType.setMerAccountTypeCode("first");
        MerchantAccountType merchantAccountType = new MerchantAccountType();
        merchantAccountType.setMerAccountTypeCode("first");
        merchantAccountType.setDeductionOrder(1);
        AccountAmountType accountAmountType1 = new AccountAmountType();
        accountAmountType1.setAccountBalance(BigDecimal.valueOf(3));
        accountAmountType1.setAccountCode(account.getAccountCode());
        accountAmountType1.setMerAccountTypeCode("second");
        MerchantAccountType merchantAccountType1 = new MerchantAccountType();
        merchantAccountType1.setMerAccountTypeCode("second");
        merchantAccountType1.setDeductionOrder(2);
        AccountAmountType accountAmountType2 = new AccountAmountType();
        accountAmountType2.setAccountBalance(BigDecimal.valueOf(6));
        accountAmountType2.setAccountCode(account.getAccountCode());
        accountAmountType2.setMerAccountTypeCode("third");
        MerchantAccountType merchantAccountType2 = new MerchantAccountType();
        merchantAccountType2.setMerAccountTypeCode("third");
        merchantAccountType2.setDeductionOrder(3);
        accountAmountDOs.add(AccountAmountDO.of(accountAmountType,merchantAccountType,account));
        accountAmountDOs.add(AccountAmountDO.of(accountAmountType1,merchantAccountType1,account));
        accountAmountDOs.add(AccountAmountDO.of(accountAmountType2,merchantAccountType2,account));
        supplierStore = new SupplierStore();
        supplierStore.setStoreCode("testStoreCode01");
        supplierStore.setMerCode("merCode002");//与请求不一样
        merchantCredit = new MerchantCredit();
        merchantCredit.setCurrentBalance(BigDecimal.valueOf(4));
        merchantCredit.setRemainingLimit(BigDecimal.valueOf(7));
        merchantCredit.setMerCode("merCode001");
    }

    public void tearDown() {
    }

    @Test
    public void testDecreaseAccount() {
        PaymentServiceImpl paymentService = SpringBeanUtils.getBean(PaymentServiceImpl.class);
        List<PaymentOperation> operations =  paymentService.decreaseAccount(paymentRequest,account,accountAmountDOs,supplierStore,merchantCredit);
        List<List<MerchantAccountOperation>> merchantOperations = operations.stream()
                .map(PaymentOperation::getMerchantAccountOperations).collect(Collectors.toList());
        List<AccountAmountType> accountAmountTypes = operations.stream()
                .map(PaymentOperation::getAccountAmountType).collect(Collectors.toList());
        List<AccountBillDetail> accountBillDetails = operations.stream()
                .map(PaymentOperation::getAccountBillDetail)
                .collect(Collectors.toList());
        List<AccountDeductionDetail> accountDeductionDetails = operations.stream()
                .map(PaymentOperation::getAccountDeductionDetail)
                .collect(Collectors.toList());
        BigDecimal totalBalance = accountAmountTypes.stream().map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTransAmountBillDetail = accountBillDetails.stream()
                .map(AccountBillDetail::getTransAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalTransAmountDeductionDetail = accountDeductionDetails.stream()
                .map(AccountDeductionDetail::getTransAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal merchantTransAmount = merchantOperations.stream().flatMap(Collection::stream)
                .map(operation -> operation.getMerchantBillDetail().getTransAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Assert.isTrue(merchantCredit.getCurrentBalance().compareTo(BigDecimal.ZERO)==0,"扣减出错");
        Assert.isTrue(merchantCredit.getRemainingLimit().compareTo(BigDecimal.ONE)==0,"扣减出错");
        Assert.isTrue(totalTransAmountDeductionDetail.compareTo(totalTransAmountBillDetail) == totalTransAmountBillDetail.compareTo(paymentRequest.getAmount()),"扣减出错");
        Assert.isTrue(totalBalance.compareTo(BigDecimal.valueOf(2))==0,"扣减出错");
        Assert.isTrue(merchantTransAmount.abs().compareTo(paymentRequest.getAmount())==0,"扣减出错");
    }
}
