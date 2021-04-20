package com.welfare.service.payment;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.exception.BizException;
import com.welfare.common.exception.ExceptionCode;
import com.welfare.persist.entity.*;
import com.welfare.service.async.AsyncService;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.operator.merchant.CurrentBalanceOperator;
import com.welfare.service.operator.payment.domain.AccountAmountDO;
import com.welfare.service.operator.payment.domain.PaymentOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/26/2021
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class WelfarePaymentOperator implements IPaymentOperator {
    private final CurrentBalanceOperator currentBalanceOperator;
    private final AsyncService asyncService;

    @Override
    public List<PaymentOperation> pay(PaymentRequest paymentRequest, Account account, List<AccountAmountDO> accountAmountDOList, SupplierStore supplierStore, MerchantCredit merchantCredit) {
        BigDecimal amount = paymentRequest.getAmount();
        Assert.notEmpty(accountAmountDOList, "用户没有可用的福利类型");
        BigDecimal allTypeBalance = accountAmountDOList.stream()
                .map(accountAmountDO -> {
                    if (Objects.nonNull(accountAmountDO.getAccountAmountTypeGroup())) {
                        return accountAmountDO.getAccountAmountTypeGroup().getBalance();
                    } else {
                        return accountAmountDO.getAccountAmountType().getAccountBalance();
                    }
                }).reduce(BigDecimal.ZERO, BigDecimal::add);
        //判断子账户之和
        boolean accountTypesEnough = allTypeBalance.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
        if (!accountTypesEnough) {
            onInsufficientBalance(paymentRequest, account);
        }
        accountAmountDOList.sort(Comparator.comparing(x -> x.getMerchantAccountType().getDeductionOrder()));
        List<PaymentOperation> paymentOperations = new ArrayList<>(4);
        List<AccountAmountType> accountAmountTypes = accountAmountDOList.stream().map(AccountAmountDO::getAccountAmountType)
                .collect(Collectors.toList());
        for (AccountAmountDO accountAmountDO : accountAmountDOList) {
            if (BigDecimal.ZERO.compareTo(accountAmountDO.getAccountAmountType().getAccountBalance()) == 0) {
                //当前的accountType没钱，则继续下一个账户
                continue;
            }
            PaymentOperation currentOperation = decrease(accountAmountDO, amount, paymentRequest, accountAmountTypes, supplierStore, merchantCredit);
            amount = amount.subtract(currentOperation.getOperateAmount());
            paymentOperations.add(currentOperation);
            if (currentOperation.isEnough()) {
                break;
            }
        }
        return paymentOperations;
    }


    private PaymentOperation decrease(AccountAmountDO accountAmountDO,
                                      BigDecimal toOperateAmount,
                                      PaymentRequest paymentRequest,
                                      List<AccountAmountType> accountAmountTypes, SupplierStore supplierStore, MerchantCredit merchantCredit) {
        AccountAmountType accountAmountType = accountAmountDO.getAccountAmountType();
        AccountAmountTypeGroup accountAmountTypeGroup = accountAmountDO.getAccountAmountTypeGroup();
        MerchantAccountType merchantAccountType = accountAmountDO.getMerchantAccountType();
        //如果有分组，则需要操作分组里的钱
        BigDecimal accountBalance = accountAmountTypeGroup == null?accountAmountType.getAccountBalance():accountAmountTypeGroup.getBalance();
        BigDecimal subtract = accountBalance.subtract(toOperateAmount);

        BigDecimal operatedAmount;
        /**
         * 扣减个人账户
         */
        boolean isCurrentEnough = subtract.compareTo(BigDecimal.ZERO) >= 0;
        if (isCurrentEnough) {
            operatedAmount = toOperateAmount;
            //分组情况
            if(accountAmountTypeGroup !=null){
                accountAmountTypeGroup.setBalance(subtract);
            }else{
                accountAmountType.setAccountBalance(subtract);
            }
        } else {
            operatedAmount = accountBalance;
            //分组情况
            if (accountAmountTypeGroup != null) {
                accountAmountTypeGroup.setBalance(BigDecimal.ZERO);
            } else {
                accountAmountType.setAccountBalance(BigDecimal.ZERO);
            }
        }
        PaymentOperation paymentOperation = new PaymentOperation();
        paymentOperation.setOperateAmount(operatedAmount);
        paymentOperation.setAccountAmountType(accountAmountType);
        paymentOperation.setAccountAmountTypeGroup(accountAmountTypeGroup);
        paymentOperation.setMerchantAccountType(merchantAccountType);
        paymentOperation.setTransNo(paymentRequest.getTransNo());
        AccountBillDetail accountBillDetail = AccountAmountDO.generateAccountBillDetail(
                paymentRequest,
                operatedAmount,
                accountAmountTypes,
                accountAmountTypeGroup
        );
        paymentOperation.setAccountBillDetail(accountBillDetail);
        paymentOperation.setEnough(isCurrentEnough);
        /**
         * 扣减商户账户
         */
        AccountDeductionDetail accountDeductionDetail = AccountAmountDO.generateAccountDeductionDetail(
                paymentRequest,
                accountAmountType,
                operatedAmount,
                paymentOperation,
                accountAmountDO.getAccount(),
                supplierStore,
                merchantCredit,
                currentBalanceOperator,
                accountAmountTypeGroup
        );
        paymentOperation.setAccountDeductionDetail(accountDeductionDetail);
        return paymentOperation;

    }

    private void onInsufficientBalance(PaymentRequest paymentRequest, Account account) {
        //离线模式，且账户不为锁定状态才需要发送短信(上游系统会尝试多次扣款，这个判断是避免重复给用户发送短信)
        if (paymentRequest.getOffline() && !WelfareConstant.AccountOfflineFlag.DISABLE.code().equals(account.getOfflineLock())) {
            asyncService.onInsufficientBalanceOffline(account, paymentRequest);
        }
        throw new BizException(ExceptionCode.INSUFFICIENT_BALANCE);
    }


}
