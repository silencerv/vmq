package com.v.inf.mq.broker.store;

import com.v.inf.mq.store.message.BrokerMessage;

import java.util.Date;
import java.util.List;

public interface MessageStoreService {

    /**
     * save
     * if duplicate key ->  ignore
     *
     * @param storeMessage
     */
    void save(BrokerMessage storeMessage);

    void success(long id);

    void success(String messageId);

    /**
     * 查询等待发送的消息
     *
     * @param startTime
     * @param limit
     * @return
     */
    List<BrokerMessage> queryReady(Date startTime, int limit);
}
