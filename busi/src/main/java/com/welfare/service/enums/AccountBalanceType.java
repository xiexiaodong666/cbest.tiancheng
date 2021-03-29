package com.welfare.service.enums;

import com.welfare.persist.entity.Account;
import com.welfare.service.remote.entity.response.WoLifeGetUserMoneyResponse;

import java.util.function.Function;

public interface AccountBalanceType<T> {

    Function<T, ?> getFieldFunc();

    String getName();

    enum Welfare implements AccountBalanceType<Account> {
        WELFARE_ACCOUNT_BALANCE(Account::getAccountBalance, "账户余额"),
        WELFARE_MAX_QUOTA(Account::getMaxQuota, "最大授权额度"),
        WELFARE_SURPLUS_QUOTA(Account::getSurplusQuota, "剩余授权额度"),
        WELFARE_SURPLUS_QUOTA_OVERPAY(Account::getSurplusQuotaOverpay, "个人授信额度溢缴款");
        private Function<Account, ?> fieldFunc;
        private String name;

        Welfare(Function<Account, ?> fieldFunc, String name) {
            this.fieldFunc = fieldFunc;
            this.name = name;
        }

        public Function<Account, ?> getFieldFunc() {
            return fieldFunc;
        }

        public String getName() {
            return name;
        }
    }

    enum WoLife implements AccountBalanceType<WoLifeGetUserMoneyResponse> {
        WO_LIFE_AVAILABLE_INTEGRAL(WoLifeGetUserMoneyResponse::getAvailableIntegral, "可用积分"),
        WO_LIFE_AVAILABLE_BALANCE(WoLifeGetUserMoneyResponse::getAvailableBalance, "可用余额"),
        WO_LIFE_AVAILABLE_WELFARE_INTEGRAL(WoLifeGetUserMoneyResponse::getAvailableWelfareIntegral,
            "可用积点");
        private Function<WoLifeGetUserMoneyResponse, ?> fieldFunc;
        private String name;

        WoLife(Function<WoLifeGetUserMoneyResponse, ?> fieldFunc, String name) {
            this.fieldFunc = fieldFunc;
            this.name = name;
        }

        public Function<WoLifeGetUserMoneyResponse, ?> getFieldFunc() {
            return fieldFunc;
        }

        public String getName() {
            return name;
        }
    }

}
