package com.welfare.serviceaccount.controller;

import org.junit.Test;

public class AccountControllerTest extends BaseControllerTest {

    @Test
    public void info() {
        get("/account/info");
    }
}
