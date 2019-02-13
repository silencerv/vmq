package com.v.inf.mq.task;

import com.v.inf.mq.broker.rabbit.BrokerConfirmCallback;
import com.v.inf.mq.client.admin.RabbitAdminService;
import com.v.inf.mq.common.dubbo.extension.ExtensionLoader;
import com.v.inf.mq.rabbit.broker.RabbitBroker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @anthor v
 * Create on 2019/1/28
 */
@Configuration
public class MQConfiguration {

    @Bean
    public RabbitBroker buildRabbitBroker(BrokerConfirmCallback brokerConfirmCallback) {
        RabbitAdminService defaultExtension = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
        RabbitBroker rabbitBroker = new RabbitBroker(defaultExtension.getConnectionFactory());
        rabbitBroker.setConfirmCallback(brokerConfirmCallback);
        return rabbitBroker;
    }
}
