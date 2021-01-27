package com.welfare.service.remote.entity;

import lombok.Data;

import java.util.UUID;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/27/2021
 */
@Data
public class NotificationReq {
    private String message;
    private String phone;
    private String requestId;
    private String targetPath;
    private String title;

    public static NotificationReq of(String phone,String msg,String targetPath,String title){
        NotificationReq notificationReq = new NotificationReq();
        notificationReq.setMessage(msg);
        notificationReq.setPhone(phone);
        notificationReq.setRequestId(UUID.randomUUID().toString());
        notificationReq.setTargetPath(targetPath);
        notificationReq.setTitle(title);
        return notificationReq;
    }
}
