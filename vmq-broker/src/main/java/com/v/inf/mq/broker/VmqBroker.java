package com.v.inf.mq.broker;

import com.v.inf.mq.Message;
import com.v.inf.mq.MessageBroker;
import com.v.inf.mq.broker.exector.MqSendExecutor;
import com.v.inf.mq.broker.rabbit.BrokerConfirmCallback;
import com.v.inf.mq.broker.store.MessageStoreService;
import com.v.inf.mq.client.admin.RabbitAdminService;
import com.v.inf.mq.common.dubbo.extension.ExtensionLoader;
import com.v.inf.mq.constants.Status;
import com.v.inf.mq.exception.MessageException;
import com.v.inf.mq.rabbit.broker.RabbitBroker;
import com.v.inf.mq.store.message.BrokerMessage;
import com.v.inf.mq.store.message.Messages;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutorService;

/**
 * @anthor v
 * Create on 2019/1/9
 */
@Component
public class VmqBroker implements MessageBroker, InitializingBean {

    private Logger logger = LoggerFactory.getLogger(VmqBroker.class);

    private RabbitBroker delegateBroker;

    @Autowired
    private BrokerConfirmCallback brokerConfirmCallback;

    @Autowired
    private MessageStoreService messageStoreService;

    private ExecutorService mqSendExecutor = new MqSendExecutor();

    @Override
    public void afterPropertiesSet() throws Exception {
        RabbitAdminService rabbitAdminService = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
        delegateBroker = new RabbitBroker(rabbitAdminService.getConnectionFactory());
        delegateBroker.setConfirmCallback(brokerConfirmCallback);
        delegateBroker.afterPropertiesSet();
    }

    @Override
    public void send(Message message) throws MessageException {
        BrokerMessage brokerMessage = Messages.toBrokerMessage(message);
        brokerMessage.setTryCount(message.getTimes());
        brokerMessage.setNextRetry(new Date());
        brokerMessage.setStatus(Status.NORMAL);
        int delayMills = message.getDelayMills();
        brokerMessage.setNextRetry(DateUtils.addMilliseconds(brokerMessage.getNextRetry(), delayMills));
        messageStoreService.save(brokerMessage);
        logger.info("save broker message :{}", message.getMessageId());

        if (delayMills == 0) {
            logger.info("send message :{}", message.getMessageId());
            doSend(message);
        } else {
            logger.info("delay message:{} , wait task....", message.getMessageId());
        }
    }

    /**
     * 暴露给task使用
     *
     * @param message
     */
    public void doSend(Message message) {
        mqSendExecutor.submit(() -> {
            delegateBroker.send(message);
        });
    }

}
