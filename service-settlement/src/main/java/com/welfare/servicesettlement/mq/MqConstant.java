package com.welfare.servicesettlement.mq;

/**
 * Description:
 *
 * @author Yuxiang Li
 * @email yuxiang.li@sjgo365.com
 * @date 3/29/2021
 */
public class MqConstant {
    public static class Topic{
        public static final String ONLINE_ORDER = "test-topic-for-e-welfare";
    }
    public static class ConsumerGroup{
        public static final String ONLINE_ORDER_CONSUMER_GROUP = "e-welfare-settlement";
    }
}
