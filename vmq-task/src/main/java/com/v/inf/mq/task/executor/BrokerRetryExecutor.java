package com.v.inf.mq.task.executor;

import com.v.inf.mq.broker.VmqBroker;
import com.v.inf.mq.broker.store.MessageStoreService;
import com.v.inf.mq.store.message.BrokerMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/27
 */
@Component
public class BrokerRetryExecutor implements Executor {

    /**
     * 这是一个和吞吐量相关的参数，根据实际情况设定
     */
    private static final int LIMIT = 1;

    private static final Logger logger = LoggerFactory.getLogger(BrokerRetryExecutor.class);

    @Autowired
    private MessageStoreService messageStoreService;

    @Autowired
    private VmqBroker vmqBroker;

    @Override
    public void execute() {
        logger.info("start broker retry job ... ");
        List<BrokerMessage> readyMessage = messageStoreService.queryReady(DateUtils.addMilliseconds(new Date(), -100), LIMIT);
        readyMessage.forEach(brokerMessage -> {
            vmqBroker.doSend(brokerMessage.toMessage());

        });
        logger.info("end broker retry job ... ");
    }
}
