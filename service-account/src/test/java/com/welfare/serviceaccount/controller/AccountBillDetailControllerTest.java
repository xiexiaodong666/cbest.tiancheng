package com.welfare.serviceaccount.controller;

import org.junit.Test;

public class AccountBillDetailControllerTest extends BaseControllerTest{

    @Test
    public void billList() {
        get("/accountBillDetail/billList");
    }
}
