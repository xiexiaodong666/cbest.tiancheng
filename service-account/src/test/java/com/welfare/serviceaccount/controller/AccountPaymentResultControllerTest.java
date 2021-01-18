package com.welfare.serviceaccount.controller;

import org.junit.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class AccountPaymentResultControllerTest extends BaseControllerTest {

    @Test
    public void barcodePaymentNotify() {
        try {
            postWithJson("/accountPaymentResult/barcodePaymentNotify", "{\n"
                + "        \"trade_no\":\"202001021Z000002\",\n"
                + "        \"gateway_trade_no\":\"202010210729378103575060480\",\n"
                + "        \"barcode\":\"6961142989927448610\",\n"
                + "        \"account_code\":\"1000000007\",\n"
                + "        \"account_name\":\"假行僧\",\n"
                + "        \"account_balance\":\"1\",\n"
                + "        \"account_creedit\":\"1\",\n"
                + "        \"total_amount\":\"1\",\n"
                + "        \"receipt_amount\":\"1\",\n"
                + "        \"actual_amount\":\"1\",\n"
                + "        \"total_discount_amount\":\"0\",\n"
                + "        \"channel_discount_amount\":\"0\",\n"
                + "        \"merchant_discount_amount\":\"0\",\n"
                + "        \"pay_detail\":\"9999-甜橙生活-1\"\n"
                + "    }");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void barcodePaymentResult() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("barcode", "6961142989927448610");
        get("/accountPaymentResult/barcodePaymentResult", params);
    }
}
