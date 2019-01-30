package com.v.inf.mq.client.producer;

import com.v.inf.mq.client.service.DbMessageStoreService;
import com.v.inf.mq.client.service.StoreServiceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client端调用send 方法时，会将消息在同一事务中入库
 * 支持常见的单DB场景
 *
 * @implNote dataSource 必须是业务数据源！！
 * @anthor v
 * Create on 2019/1/9
 */
public class SingleDbMessageProducer extends AbstractDbMessageProducer {

    private static Logger logger = LoggerFactory.getLogger(SingleDbMessageProducer.class);

    @Override
    protected DbMessageStoreService getStoreService() {
        return StoreServiceManager.getStoreService(dataSource);
    }
}
