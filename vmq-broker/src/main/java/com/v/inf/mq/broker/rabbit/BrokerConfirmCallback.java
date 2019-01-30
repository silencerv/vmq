package com.v.inf.mq.broker.rabbit;

import com.v.inf.mq.broker.store.MessageStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @anthor v
 * Create on 2019/1/9
 */
@Component
public class BrokerConfirmCallback implements RabbitTemplate.ConfirmCallback {

    private Logger logger = LoggerFactory.getLogger(BrokerConfirmCallback.class);

    @Autowired
    private MessageStoreService messageStoreService;

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String messageId = correlationData.getId();
        if (ack) {
            logger.info("receive ack confirm,message id:{}", messageId);
            messageStoreService.success(messageId);
        } else {
            logger.info("receive nack confirm,message id:{}", messageId);
        }
    }
}
