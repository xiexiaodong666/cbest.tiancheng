package com.welfare.common.constants;

public interface AccountBalanceType {

    enum Welfare implements AccountBalanceType {
        WELFARE_ACCOUNT_BALANCE("accountBalance", "账户余额"),
        WELFARE_MAX_QUOTA("maxQuota", "最大授权额度"),
        WELFARE_SURPLUS_QUOTA("surplusQuota", "剩余授权额度"),
        WELFARE_SURPLUS_QUOTA_OVERPAY("surplusQuotaOverpay", "个人授信额度溢缴款");
        private String key;
        private String name;

        Welfare(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }
    }

    enum WoLife implements AccountBalanceType {
        WO_LIFE_AVAILABLE_INTEGRAL("availableIntegral", "可用积分"),
        WO_LIFE_AVAILABLE_BALANCE("availableBalance", "可用余额"),
        WO_LIFE_AVAILABLE_WELFARE_INTEGRAL("availableWelfareIntegral", "可用积点");
        private String key;
        private String name;

        WoLife(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }
    }

    String getKey();

    String getName();
}
