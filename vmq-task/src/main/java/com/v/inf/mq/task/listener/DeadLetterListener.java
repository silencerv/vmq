package com.v.inf.mq.task.listener;

import com.v.inf.mq.Message;
import com.v.inf.mq.MessageListener;
import com.v.inf.mq.broker.constants.DeadMessage;
import com.v.inf.mq.broker.retry.PowerIncrementRetry;
import com.v.inf.mq.broker.retry.Retry;
import com.v.inf.mq.broker.store.MessageStoreService;
import com.v.inf.mq.client.annotation.MqConsumer;
import com.v.inf.mq.rabbit.constants.DeadLetterExchange;
import com.v.inf.mq.store.message.BrokerMessage;
import com.v.inf.mq.store.message.Messages;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @anthor v
 * Create on 2019/1/17
 */
@Component
public class DeadLetterListener implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(DeadLetterListener.class);

    private Retry retry = new PowerIncrementRetry(3);

    @Autowired
    private MessageStoreService messageStoreService;

    @MqConsumer(subjects = DeadLetterExchange.NAME, group = "broker")
    @Override
    public void onMessage(Message message) {
        logger.info("receive dead letter message: {}", message.getMessageId());
        BrokerMessage brokerMessage = Messages.toBrokerMessage(message);
        brokerMessage.setNextRetry(retry.nextRetry(DateUtils.addMilliseconds(brokerMessage.getCreateTime(), message.getDelayMills()), brokerMessage.getTryCount()));
        brokerMessage.setTryCount(brokerMessage.getTryCount() + 1);
        messageStoreService.save(brokerMessage);
        //一定次数之后 message dead
        if (brokerMessage.getTryCount() >= DeadMessage.MAX_REY_COUNT) {
            // todo do something
        }
    }
}
