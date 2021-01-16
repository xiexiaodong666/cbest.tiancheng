package com.welfare.serviceaccount.controller;

import com.alibaba.fastjson.JSON;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.common.domain.AccountUserInfo;
import java.util.HashMap;
import java.util.Map;

public class BaseControllerTest extends AbstractBaseControllerTest {

    @Override
    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(WelfareConstant.Header.SOURCE.code(), "api");
        AccountUserInfo accountUserInfo = new AccountUserInfo();
        accountUserInfo.setAccountCode(1000000007L);
        accountUserInfo.setMerCode("A102");
        accountUserInfo.setPhone("13361612716");
        headers.put(WelfareConstant.Header.ACCOUNT_USER.code(), JSON.toJSONString(accountUserInfo));
        return headers;
    }
}
