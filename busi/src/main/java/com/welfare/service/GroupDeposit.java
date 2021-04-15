package com.welfare.service;

import com.welfare.persist.entity.AccountAmountType;
import com.welfare.persist.entity.Sequence;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: duanhy
 * @Version: 0.0.1
 * @Date: 2021/4/15 10:17 上午
 */
@Data
public class GroupDeposit {

    private Long groupId;

    private List<DepositItem> deposits;

    @Data
    public static class DepositItem {
        @ApiModelProperty("请求id")
        private String requestId;
        @ApiModelProperty("交易号")
        private String transNo;
        @ApiModelProperty("账户")
        private Long accountCode;
        @ApiModelProperty("充值金额")
        private BigDecimal amount;
        @ApiModelProperty(value = "充值目标,对应充到哪个账户下(烤火费、自主等)")
        private String merAccountTypeCode;
    }

    public static List<GroupDeposit> of(BigDecimal amount, List<Sequence> sequences, Map<Long, List<AccountAmountType>> accountAmountTypeMap) {
        AtomicInteger index = new AtomicInteger();
        List<GroupDeposit> groupDeposits = new ArrayList<>();
        accountAmountTypeMap.forEach((groupId, accountAmountTypes) -> {
            GroupDeposit groupDeposit = new GroupDeposit();
            groupDeposit.setGroupId(groupId);
            List<DepositItem> deposits = new ArrayList<>();
            groupDeposit.setDeposits(deposits);
            groupDeposits.add(groupDeposit);
            accountAmountTypes.forEach(accountAmountType -> {
                DepositItem depositItem = new DepositItem();
                depositItem.setAccountCode(accountAmountType.getAccountCode());
                depositItem.setAmount(amount);
                depositItem.setMerAccountTypeCode(accountAmountType.getMerAccountTypeCode());
                depositItem.setTransNo(sequences.get(index.getAndIncrement()) + "");
                deposits.add(depositItem);
            });
        });
        return null;
    }
}
