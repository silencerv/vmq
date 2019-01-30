package com.v.inf.mq.client.producer;

import com.v.inf.mq.client.service.DbMessageStoreService;
import com.v.inf.mq.client.service.StoreServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client端调用send 方法时，会将消息在同一事务中入库
 *
 * @anthor v
 * Create on 2019/1/9
 * @Note 请谨慎使用这个类，它只在特定条件下才能正常工作！
 */
public class AdaptiveDbMessageProducer extends AbstractDbMessageProducer {

    private static Logger logger = LoggerFactory.getLogger(AdaptiveDbMessageProducer.class);

    @Override
    protected DbMessageStoreService getStoreService() {
        return StoreServiceManager.getStoreServiceWithDefault(dataSource);
    }

}
