package com.v.inf.mq.client.producer;

import com.v.inf.mq.Message;
import com.v.inf.mq.MessageProducer;
import com.v.inf.mq.client.admin.RabbitAdminService;
import com.v.inf.mq.exception.MessageException;
import com.v.inf.mq.rabbit.broker.RabbitBroker;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.Objects;

/**
 * 直接生产消息到broker 不保证事务
 * @anthor v
 * Create on 2019/2/14
 */
public class DirectMessageProducer implements MessageProducer, InitializingBean {

    protected ConnectionFactory connectionFactory;

    protected RabbitBroker rabbitBroker;

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(connectionFactory)) {
            RabbitAdminService rabbitAdminService = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
            setConnectionFactory(rabbitAdminService.getConnectionFactory());
        }
        rabbitBroker = new RabbitBroker(connectionFactory);
        rabbitBroker.afterPropertiesSet();
    }

    @Override
    public void send(Message message) throws MessageException {
        rabbitBroker.send(message);
    }
}
