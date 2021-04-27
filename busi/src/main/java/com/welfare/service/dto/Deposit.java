package com.welfare.service.dto;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.*;
import com.welfare.service.GroupDeposit;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/7/2021
 */
@Data
@ApiModel("充值请求")
public class Deposit {
    @ApiModelProperty("请求id")
    private String requestId;
    @ApiModelProperty("交易号")
    private String transNo;
    @ApiModelProperty("账户")
    private Long accountCode;
    @ApiModelProperty("充值卡号")
    private String cardNo;
    @ApiModelProperty("充值金额")
    private BigDecimal amount;
    @ApiModelProperty("商户编码")
    private String merchantCode;
    @ApiModelProperty(value = "充值目标,对应充到哪个账户下(烤火费、自主等)")
    private String merAccountTypeCode;
    @ApiModelProperty(value = "充值状态",notes = "0:新增, 1:处理中, 2:处理成功 -1:处理失败")
    private Integer depositStatus;
    @ApiModelProperty("渠道,wechat,alipay")
    private String channel;
    @ApiModelProperty("申请编号")
    private String applyCode;
    @ApiModelProperty("支付渠道")
    private String paymentChannel;

    public static Deposit of(AccountDepositApply accountDepositApply,AccountDepositApplyDetail accountDepositApplyDetail){
        Deposit deposit = new Deposit();
        deposit.setAccountCode(accountDepositApplyDetail.getAccountCode());
        deposit.setMerAccountTypeCode(accountDepositApply.getMerAccountTypeCode());
        deposit.setAmount(accountDepositApplyDetail.getRechargeAmount());
        deposit.setMerchantCode(accountDepositApply.getMerCode());
        deposit.setAccountCode(accountDepositApplyDetail.getAccountCode());
        deposit.setTransNo(accountDepositApplyDetail.getTransNo());
        deposit.setChannel(accountDepositApply.getChannel());
        deposit.setApplyCode(accountDepositApply.getApplyCode());
        return deposit;
    }

    public static List<Deposit> of(AccountDepositApply accountDepositApply, List<AccountDepositApplyDetail> accountDepositApplyDetails){
        return accountDepositApplyDetails.stream()
                .map(detail -> Deposit.of(accountDepositApply, detail))
                .collect(Collectors.toList());
    }

    public static Deposit of(AccountBillDetail accountBillDetail, Account account){
        Deposit deposit = new Deposit();
        deposit.setAmount(accountBillDetail.getTransAmount());
        deposit.setCardNo(accountBillDetail.getCardId());
        deposit.setChannel(accountBillDetail.getChannel());
        deposit.setTransNo(accountBillDetail.getTransNo());
        deposit.setAccountCode(accountBillDetail.getAccountCode());
        deposit.setDepositStatus(WelfareConstant.AsyncStatus.SUCCEED.code());
        deposit.setMerchantCode(account.getMerCode());
        deposit.setAccountCode(account.getAccountCode());
        return deposit;
    }

    public AccountAmountType toNewAccountAmountType(){
        AccountAmountType accountAmountType = new AccountAmountType();
        accountAmountType.setAccountBalance(BigDecimal.ZERO);
        accountAmountType.setAccountCode(accountCode);
        accountAmountType.setMerAccountTypeCode(merAccountTypeCode);
        return accountAmountType;
    }

    public static AccountBillDetail assemblyAccountBillDetail(Deposit deposit, AccountAmountType accountAmountType,
                                                        Account account) {
        AccountBillDetail accountBillDetail = new AccountBillDetail();
        Long accountCode = deposit.getAccountCode();
        BigDecimal amount = deposit.getAmount();
        accountBillDetail.setAccountCode(accountCode);
        accountBillDetail.setAccountBalance(account.getAccountBalance());
        accountBillDetail.setChannel(deposit.getChannel());
        accountBillDetail.setTransNo(deposit.getTransNo());
        accountBillDetail.setTransAmount(amount);
        accountBillDetail.setTransTime(Calendar.getInstance().getTime());
        accountBillDetail.setSurplusQuota(account.getSurplusQuota());
        accountBillDetail.setSurplusQuotaOverpay(account.getSurplusQuotaOverpay());
        accountBillDetail.setTransType(WelfareConstant.TransType.DEPOSIT_INCR.code());
        accountBillDetail.setPaymentChannel(deposit.getPaymentChannel());
        return accountBillDetail;
    }

    public static List<Deposit> of(BigDecimal amount, List<Sequence> sequences, List<AccountAmountType> accountAmountTypes) {
        AtomicInteger index = new AtomicInteger();
        List<Deposit> deposits = new ArrayList<>();
        accountAmountTypes.forEach(accountAmountType -> {
            Deposit deposit = new Deposit();
            deposit.setAmount(amount);
            deposit.setTransNo(sequences.get(index.getAndIncrement()).getSequenceNo() + "");
            deposit.setAccountCode(accountAmountType.getAccountCode());
            deposit.setMerAccountTypeCode(accountAmountType.getMerAccountTypeCode());
            deposit.setChannel(WelfareConstant.Channel.PLATFORM.code());
            deposits.add(deposit);
        });
        return deposits;
    }
}
