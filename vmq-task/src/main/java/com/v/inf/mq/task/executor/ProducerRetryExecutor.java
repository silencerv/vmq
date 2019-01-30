package com.v.inf.mq.task.executor;

import com.v.inf.mq.broker.VmqBroker;
import com.v.inf.mq.client.service.DbMessageStoreService;
import com.v.inf.mq.client.service.StoreServiceManager;
import com.v.inf.mq.store.message.StoreMessage;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/28
 */
@Component
public class ProducerRetryExecutor implements Executor {

    private static final Logger logger = LoggerFactory.getLogger(BrokerRetryExecutor.class);

    /**
     * 这是一个和吞吐量相关的参数，根据实际情况设定
     */
    private static final int LIMIT = 1;

    /**
     * 需要扫描的数据库
     */
    @Resource(name = "producerDataSources")
    private List<DataSource> dataSources;

    @Autowired
    private VmqBroker vmqBroker;

    @Override
    public void execute() {
        logger.info("start producer retry job ... ");
        dataSources.forEach(dataSource -> {
            DbMessageStoreService storeService = StoreServiceManager.getStoreService(dataSource);
            List<StoreMessage> storeMessages = storeService.queryReady(DateUtils.addMilliseconds(new Date(), -100), LIMIT);
            storeMessages.forEach(storeMessage -> {
                vmqBroker.send(storeMessage.toMessage());
                storeService.success(storeMessage.getMessageId());
            });
        });
        logger.info("end producer retry job ... ");
    }
}
