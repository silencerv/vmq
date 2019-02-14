package com.v.inf.mq.client.producer;

import com.v.inf.mq.client.service.DbMessageStoreService;
import com.v.inf.mq.client.service.StoreServiceManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @anthor v
 * Create on 2019/1/9
 */
public class ProducerConfirmCallback implements RabbitTemplate.ConfirmCallback {

    private static Logger logger = LoggerFactory.getLogger(ProducerConfirmCallback.class);

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String[] idAndDbKey = StringUtils.split(correlationData.getId(), "$");
        String messageId = idAndDbKey[0];
        if (ack) {
            logger.debug("receive ack confirm:{}", messageId);
            if (idAndDbKey.length > 1) {
                String dbKey = idAndDbKey[1];
                DbMessageStoreService storeService = StoreServiceManager.getStoreService(dbKey);
                storeService.success(messageId);
                logger.debug("delete message :{} , from :{}!", messageId, dbKey);
            }
        } else {
            logger.warn("receive nack confirm:{}", messageId);
        }
    }
}
