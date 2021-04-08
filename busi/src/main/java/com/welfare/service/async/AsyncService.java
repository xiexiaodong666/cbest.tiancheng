package com.welfare.service.async;

import com.alibaba.fastjson.JSON;
import com.welfare.common.constants.WelfareConstant;
import com.welfare.persist.dao.AccountDao;
import com.welfare.persist.entity.Account;
import com.welfare.service.dto.payment.PaymentRequest;
import com.welfare.service.remote.NotificationFeign;
import com.welfare.service.remote.entity.NotificationReq;
import com.welfare.service.remote.entity.NotificationResp;
import com.welfare.service.remote.entity.SendMessageReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

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
     * 不使用事务，不管结果如何，该异步方法的更新操作均会提交
     */
    @Async("e-welfare-taskExecutor")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void onInsufficientBalanceOffline(Account account, PaymentRequest paymentRequest){
        //离线模式需要锁定其离线交易
        account.setOfflineLock(WelfareConstant.AccountOfflineFlag.DISABLE.code());
        accountDao.updateById(account);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(paymentRequest.getPaymentDate());
        String msg = String.format("甜橙生活:%s,您因余额不足导致消费订单被挂起无法扣款，请尽快充值个人余额，以免影响正常交易。",dateStr);
        SendMessageReq sendMessageReq = SendMessageReq.of(account.getPhone(),msg);
        try{
            NotificationResp notificationResp = notificationFeign.doSendSms(sendMessageReq);
            if(!SUCCEED.equals(notificationResp.getCode())){
                log.error("调用通知系统出错,msg:{},failureData{}",notificationResp.getMsg(),JSON.toJSONString(notificationResp.getFailureData()));
            } else {
                log.info("调用通知系统完成,msg:{},",notificationResp.getMsg());
            }
        }catch (Exception e){
            log.error("调用通知系统出现系统级异常,msg:{}",e.getMessage(),e);
        }

    }
}
