package com.welfare.service.remote;

import com.welfare.common.annotation.ConditionalOnHavingProperty;
import com.welfare.service.remote.entity.NotificationReq;
import com.welfare.service.remote.entity.NotificationResp;
import com.welfare.service.remote.entity.SendMessageReq;
import com.welfare.service.remote.fallback.NotificationFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/27/2021
 */
@FeignClient(value = "notificationFeign", url = "${notification.url:http://test1-welfare-internal.kube-test.cbestcd.com}", fallbackFactory = NotificationFallback.class)
@ConditionalOnHavingProperty("notification.url")
public interface NotificationFeign {

    /**
     * 调用通知系统通知
     * @param notificationReq
     * @return
     */
    @RequestMapping(value = "/backend-api/rpc-base/inward/tc/push/message", method = RequestMethod.POST, consumes = "application/json")
    NotificationResp doNotify(NotificationReq notificationReq);

    /**
     *  调用通知系统发送手机短信
     * @param sendMessageReq
     * @return
     */
    @RequestMapping(value ="/backend-api/rpc-base/inward/tc/send/phone", method = RequestMethod.POST, consumes = "application/json")
    NotificationResp doSendSms(SendMessageReq sendMessageReq);

}
