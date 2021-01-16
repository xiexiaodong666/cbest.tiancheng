package com.welfare.servicesettlement.task;

import com.welfare.service.OrderService;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.servicesettlement.task
 * @ClassName: OrderTask
 * @Author: jian.zhou
 * @Description: 订单定时任务
 * @Date: 2021/1/12 14:32
 * @Version: 1.0
 */
@Slf4j
@Component("orderTask")
@JobHandler(value = "orderTask")
public class OrderTask {

    @Autowired
    private OrderService orderService;

//    @Scheduled(cron = "0 0/1 * * * ? ")
    public void syncOrderData(){
        //查询系统中所有配置门店
        log.info("定时拉取kafka订单数据");
        //处理kafka数据
        orderService.syncOrderData();

    }
}
