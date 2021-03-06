package com.v.inf.mq.task;

import com.v.inf.mq.broker.rabbit.BrokerConfirmCallback;
import com.v.inf.mq.client.admin.RabbitAdminService;
import com.v.inf.mq.client.annotation.EnableVMQ;
import com.v.inf.mq.client.annotation.VMQProducer;
import com.v.inf.mq.rabbit.broker.RabbitBroker;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.springframework.context.annotation.Bean;

/**
 * @anthor v
 * Create on 2019/1/28
 */
@EnableVMQ(producers = @VMQProducer)
public class MQConfiguration {

    @Bean
    public RabbitBroker buildRabbitBroker(BrokerConfirmCallback brokerConfirmCallback) {
        RabbitAdminService defaultExtension = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
        RabbitBroker rabbitBroker = new RabbitBroker(defaultExtension.getConnectionFactory());
        rabbitBroker.setConfirmCallback(brokerConfirmCallback);
        return rabbitBroker;
    }
}
