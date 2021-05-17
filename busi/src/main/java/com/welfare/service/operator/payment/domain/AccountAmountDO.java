package com.welfare.service.operator.payment.domain;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.enums.PaymentTypeEnum;
import com.welfare.common.util.SpringBeanUtils;
import com.welfare.persist.entity.*;
import com.welfare.service.MerchantCreditService;
import com.welfare.service.dto.payment.*;
import com.welfare.service.operator.merchant.AbstractMerAccountTypeOperator;
import com.welfare.service.operator.merchant.domain.MerchantAccountOperation;
import lombok.Data;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.welfare.common.constants.WelfareConstant.MerAccountTypeCode.*;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/13/2021
 */
@Data
public class AccountAmountDO {
    private AccountAmountType accountAmountType;
    private AccountAmountTypeGroup accountAmountTypeGroup;
    private MerchantAccountType merchantAccountType;
    private Account account;
    private String transNo;

    public static AccountAmountDO of(AccountAmountType accountAmountType,
                                     MerchantAccountType merchantAccountType,
                                     Account account,
                                     AccountAmountTypeGroup accountAmountTypeGroup) {
        AccountAmountDO accountAmountDO = new AccountAmountDO();
        Assert.notNull(accountAmountType, "子账户不能为空");
        Assert.notNull(merchantAccountType, "商户子账户不能为空");
        accountAmountDO.setAccountAmountType(accountAmountType);
        accountAmountDO.setMerchantAccountType(merchantAccountType);
        accountAmountDO.setAccount(account);
        accountAmountDO.setAccountAmountTypeGroup(accountAmountTypeGroup);
        return accountAmountDO;
    }

    public static BigDecimal calculateAccountCredit(List<AccountAmountType> accountTypes) {
        return accountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calculateAccountBalance(List<AccountAmountType> accountTypes) {
        return accountTypes.stream()
                //排除掉授信额度，溢缴款和批发额度，才是余额
                .filter(accountAmountType -> !(
                        SURPLUS_QUOTA.code().equals(accountAmountType.getMerAccountTypeCode())
                                || SURPLUS_QUOTA_OVERPAY.code().equals(accountAmountType.getMerAccountTypeCode())
                                || WHOLESALE_PROCUREMENT.code().equals(accountAmountType.getMerAccountTypeCode())
                ))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal calculateAccountCreditOverpay(List<AccountAmountType> accountAmountTypes) {
        return accountAmountTypes.stream()
                .filter(accountAmountType -> SURPLUS_QUOTA_OVERPAY.code().equals(accountAmountType.getMerAccountTypeCode()))
                .map(AccountAmountType::getAccountBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static AccountBillDetail generateAccountBillDetail(PaymentRequest paymentRequest,
                                                              BigDecimal operatedAmount,
                                                              List<AccountAmountType> accountAmountTypes,
                                                              AccountAmountTypeGroup accountAmountTypeGroup) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        accountBillDetail.setAccountCode(paymentRequest.calculateAccountCode());
        accountBillDetail.setTransType(WelfareConstant.TransType.CONSUME.code());
        accountBillDetail.setTransTime(paymentRequest.getPaymentDate());
        accountBillDetail.setTransNo(paymentRequest.getTransNo());
        accountBillDetail.setOrderNo(paymentRequest.getOrderNo());
        accountBillDetail.setPos(paymentRequest.getMachineNo());
        accountBillDetail.setTransAmount(operatedAmount);
        accountBillDetail.setStoreCode(paymentRequest.getStoreNo());
        accountBillDetail.setCardId(paymentRequest.getCardNo());
        accountBillDetail.setOrderChannel(paymentRequest.getPaymentScene());
        accountBillDetail.setPaymentChannel(paymentRequest.getPaymentChannel());
        accountBillDetail.setAccountAmountTypeGroupId(accountAmountTypeGroup == null ? null : accountAmountTypeGroup.getId());
        if (paymentRequest instanceof CardPaymentRequest) {
            accountBillDetail.setPaymentType(PaymentTypeEnum.CARD.getCode());
            accountBillDetail.setPaymentTypeInfo(((CardPaymentRequest) paymentRequest).getCardInsideInfo());
        } else if (paymentRequest instanceof BarcodePaymentRequest) {
            accountBillDetail.setPaymentType(PaymentTypeEnum.BARCODE.getCode());
            accountBillDetail.setPaymentTypeInfo(((BarcodePaymentRequest) paymentRequest).getBarcode());
        } else if (paymentRequest instanceof OnlinePaymentRequest) {
            accountBillDetail.setPaymentType(PaymentTypeEnum.ONLINE.getCode());
        } else if (paymentRequest instanceof DoorAccessPaymentRequest) {
            accountBillDetail.setPaymentType(PaymentTypeEnum.DOOR_ACCESS.getCode());
        } else if (paymentRequest instanceof WholesalePaymentRequest) {
            accountBillDetail.setPaymentType(PaymentTypeEnum.WHOLESALE.getCode());
        }
        BigDecimal accountBalance = AccountAmountDO.calculateAccountBalance(accountAmountTypes);
        BigDecimal accountSurplusQuota = AccountAmountDO.calculateAccountCredit(accountAmountTypes);
        BigDecimal accountSurplusOverpay = AccountAmountDO.calculateAccountCreditOverpay(accountAmountTypes);
        accountBillDetail.setAccountBalance(accountBalance);
        accountBillDetail.setSurplusQuota(accountSurplusQuota);
        accountBillDetail.setSurplusQuotaOverpay(accountSurplusOverpay);
        return accountBillDetail;
    }

    public static AccountDeductionDetail generateAccountDeductionDetail(PaymentRequest paymentRequest,
                                                                        AccountAmountType accountAmountType,
                                                                        BigDecimal operatedAmount,
                                                                        PaymentOperation paymentOperation,
                                                                        Account account, SupplierStore supplierStore,
                                                                        MerchantCredit merchantCredit,
                                                                        AbstractMerAccountTypeOperator merAccountTypeOperator,
                                                                        AccountAmountTypeGroup accountAmountTypeGroup) {
        AccountDeductionDetail accountDeductionDetail = new AccountDeductionDetail();
        accountDeductionDetail.setAccountCode(paymentRequest.calculateAccountCode());
        accountDeductionDetail.setOrderChannel(paymentRequest.getPaymentScene());
        accountDeductionDetail.setAccountDeductionAmount(operatedAmount);
        accountDeductionDetail.setOrderNo(paymentRequest.getOrderNo());
        accountDeductionDetail.setAccountAmountTypeBalance(Objects.isNull(accountAmountType) ? BigDecimal.ZERO : accountAmountType.getAccountBalance());
        accountDeductionDetail.setMerAccountType(Objects.isNull(accountAmountType) ? null : accountAmountType.getMerAccountTypeCode());
        accountDeductionDetail.setPos(paymentRequest.getMachineNo());
        accountDeductionDetail.setTransNo(paymentRequest.getTransNo());
        accountDeductionDetail.setPayCode(WelfareConstant.PayCode.WELFARE_CARD.code());
        accountDeductionDetail.setTransType(WelfareConstant.TransType.CONSUME.code());
        accountDeductionDetail.setTransAmount(operatedAmount);
        accountDeductionDetail.setReversedAmount(BigDecimal.ZERO);
        accountDeductionDetail.setTransTime(paymentRequest.getPaymentDate());
        accountDeductionDetail.setStoreCode(paymentRequest.getStoreNo());
        accountDeductionDetail.setPaymentChannel(paymentRequest.getPaymentChannel());
        accountDeductionDetail.setAccountAmountTypeGroupId(accountAmountTypeGroup == null ? null : accountAmountTypeGroup.getId());
        if (paymentRequest instanceof CardPaymentRequest) {
            accountDeductionDetail.setCardId(paymentRequest.getCardNo());
        }
        if (accountAmountType == null) {
            //联通沃生活馆，没有福利类型
            accountDeductionDetail.setSelfDeductionAmount(BigDecimal.ZERO);
        } else {
            accountDeductionDetail.setSelfDeductionAmount(SELF.code().equals(accountAmountType.getMerAccountTypeCode()) ? operatedAmount : BigDecimal.ZERO);
        }
        accountDeductionDetail.setAccountDeductionAmount(operatedAmount);
        //扣减商户金额

        Assert.notNull(supplierStore, "根据门店号没有找到门店");
        if (!Objects.equals(supplierStore.getMerCode(), account.getMerCode()) && Objects.nonNull(merAccountTypeOperator)) {
            decreaseMerchant(paymentRequest, operatedAmount, paymentOperation, merchantCredit, merAccountTypeOperator, accountDeductionDetail);
        } else {
            paymentOperation.setMerchantAccountOperations(Collections.emptyList());
            accountDeductionDetail.setMerDeductionAmount(BigDecimal.ZERO);
            accountDeductionDetail.setMerDeductionCreditAmount(BigDecimal.ZERO);
        }


        return accountDeductionDetail;
    }

    private static void decreaseMerchant(PaymentRequest paymentRequest, BigDecimal operatedAmount, PaymentOperation paymentOperation, MerchantCredit merchantCredit, AbstractMerAccountTypeOperator merAccountTypeOperator, AccountDeductionDetail accountDeductionDetail) {
        MerchantCreditService merchantCreditService = SpringBeanUtils.getBean(MerchantCreditService.class);
        List<MerchantAccountOperation> merchantAccountOperations = merchantCreditService.doOperateAccount(
                merchantCredit,
                operatedAmount,
                paymentRequest.getTransNo(),
                merAccountTypeOperator, WelfareConstant.TransType.CONSUME.code());
        paymentOperation.setMerchantAccountOperations(merchantAccountOperations);
        Map<String, MerchantBillDetail> merBillDetailMap = merchantAccountOperations.stream().map(MerchantAccountOperation::getMerchantBillDetail)
                .collect(Collectors.toMap(MerchantBillDetail::getBalanceType, merchantBillDetail -> merchantBillDetail));
        MerchantBillDetail currentBalanceDetail = merBillDetailMap.get(WelfareConstant.MerCreditType.CURRENT_BALANCE.code());
        MerchantBillDetail remainingLimitDetail = merBillDetailMap.get(WelfareConstant.MerCreditType.REMAINING_LIMIT.code());
        accountDeductionDetail.setMerDeductionAmount(currentBalanceDetail == null ? BigDecimal.ZERO : currentBalanceDetail.getTransAmount().abs());
        accountDeductionDetail.setMerDeductionCreditAmount(remainingLimitDetail == null ? BigDecimal.ZERO : remainingLimitDetail.getTransAmount().abs());
    }

    public static void updateAccountAfterOperated(Account account, List<AccountAmountType> accountAmountTypes) {
        BigDecimal accountBalance = AccountAmountDO.calculateAccountBalance(accountAmountTypes);
        BigDecimal accountCreditBalance = AccountAmountDO.calculateAccountCredit(accountAmountTypes);
        BigDecimal accountCreditOverpay = AccountAmountDO.calculateAccountCreditOverpay(accountAmountTypes);
        account.setAccountBalance(accountBalance);
        account.setSurplusQuota(accountCreditBalance);
        account.setSurplusQuotaOverpay(accountCreditOverpay);
    }
}
