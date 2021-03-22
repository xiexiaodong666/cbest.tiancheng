package com.welfare.service.async;

import com.alibaba.fastjson.JSON;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.remote.NotificationFeign;
import com.welfare.service.remote.entity.NotificationReq;
import com.welfare.service.remote.entity.NotificationResp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 1/27/2021
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncService {

    public static final String SUCCEED = "0000";
    @Autowired(required = false)
    private NotificationFeign notificationFeign;
    @Value("${notification.targetPath:https://test-welfare-app.sjgo365.com/tc/redirect?target=center}")
    private String targetPath;
    private final AccountDao accountDao;

    @Async("e-welfare-taskExecutor")
    public void paymentNotify(String phone, BigDecimal amount){
        String msg = "交易提醒:您在甜橙生活有一笔" + amount.toString() + "元的消费已成功";
        NotificationReq notificationReq = NotificationReq.of(phone, msg, targetPath, "消费账单通知");
        NotificationResp notificationResp = notificationFeign.doNotify(notificationReq);
        if(!SUCCEED.equals(notificationResp.getCode())){
            log.error("调用通知系统出错,msg:{},failureData:{}",notificationResp.getMsg(),JSON.toJSONString(notificationResp.getFailureData()));
        }
    }

    /**
     * 离线模式余额不足的时候触发
     * 1. 锁定用户
     * 2. 发短信通知用户
     */
    @Async("e-welfare-taskExecutor")
    public void onInsufficientBalanceOffline(Account account, PaymentRequest paymentRequest){
        //离线模式需要锁定其离线交易
        account.setOfflineLock(WelfareConstant.AccountOfflineFlag.DISABLE.code());
        accountDao.updateById(account);
        //todo 发送锁定短信
    }
}
