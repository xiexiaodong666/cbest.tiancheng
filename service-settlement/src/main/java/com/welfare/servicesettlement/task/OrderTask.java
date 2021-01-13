package com.welfare.servicesettlement.task;

import com.welfare.service.OrderService;
import com.xxl.job.core.handler.annotation.JobHandler;
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

@Component("orderTask")
@JobHandler(value = "orderTask")
public class OrderTask {

    @Autowired
    private OrderService orderService;

    @Scheduled(cron = "0 * * * * ? ")
    public void syncOrderData(){
        //查询系统中所有配置门店

        //处理kafka数据
        orderService.syncOrderData();

    }
}
