package com.welfare.service.remote.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: 短信发送请求
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/22/2021
 */
@Data
public class SendMessageReq implements Serializable {
    private static final String E_WELFARE = "3";

    private String expireTime;
    private String merchantType;
    private String message;
    private String phone;
    private String requestId;

    public static SendMessageReq of(String phone,String message){
        SendMessageReq sendMessageReq = new SendMessageReq();
        sendMessageReq.setMessage(message);
        sendMessageReq.setPhone(phone);
        sendMessageReq.setRequestId(UUID.randomUUID().toString());
        sendMessageReq.setMerchantType(E_WELFARE);
        return sendMessageReq;
    }
}
