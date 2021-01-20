package com.welfare.service.config;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.google.common.collect.Maps;
import com.welfare.service.impl.OrderServiceImpl;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.Map;

/**
 * @ProjectName: e-welfare
 * @Package: com.welfare.service.config
 * @ClassName: KafkaConsumerConfig
 * @Author: jian.zhou
 * @Description: kafka
 * @Date: 2021/1/12 15:33
 * @Version: 1.0
 */
//@Configuration
//@EnableKafka
public class KafkaConsumerConfig {

    final static String list ="192.1.30.236:6667,192.1.30.237:6667,192.1.30.238:6667";

    /**
     * Description：获取配置
     * Date：        2017年7月11日
     * @author      shaqf
     */
    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = Maps.newHashMap();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, list);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "15000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "welfare");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        System.out.println("KafkaConsumer consumerConfigs "+ JSON.toJSONString(props));
        return props;
    }
    /** 获取工厂 */
    private ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory(consumerConfigs());
    }
    /** 获取实例 */
    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory1 = new ConcurrentKafkaListenerContainerFactory();
        factory1.setConsumerFactory(consumerFactory());
        factory1.setConcurrency(2);
        factory1.getContainerProperties().setPollTimeout(3000);
        System.out.println("KafkaConsumer kafkaListenerContainerFactory factory"+ JSON.toJSONString(factory1));
        return factory1;
    }

    /**
     * topic的消费者组1监听
     * @return
     */
    @Bean
    public OrderServiceImpl listener() {
        return new OrderServiceImpl();
    }
}
