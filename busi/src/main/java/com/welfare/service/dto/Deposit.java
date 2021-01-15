package com.welfare.service.dto;

import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.entity.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
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
}
