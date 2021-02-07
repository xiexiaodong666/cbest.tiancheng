package com.welfare.serviceaccount.controller;

import com.alibaba.fastjson.JSON;
import com.welfare.common.constants.WelfareConstant.Header;
import com.welfare.common.constants.WelfareConstant.HeaderSource;
import com.welfare.common.domain.AccountUserInfo;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseControllerTest extends AbstractBaseControllerTest {

    @Override
    protected Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put(Header.SOURCE.code(), HeaderSource.E_WELFARE_API.code());
        AccountUserInfo accountUserInfo = new AccountUserInfo();
        accountUserInfo.setAccountCode(1000000029L);
        headers.put(Header.ACCOUNT_USER.code(), JSON.toJSONString(accountUserInfo));
        return headers;
    }
}
