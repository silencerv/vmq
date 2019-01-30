package com.v.inf.mq.store.message;

import com.v.inf.mq.Message;
import com.v.inf.mq.common.util.JsonUtils;

import java.util.Date;

/**
 * @anthor v
 * Create on 2019/1/9
 */
public class Messages {

    public static StoreMessage toStoreMessage(Message message) {
        StoreMessage storeMessage = new StoreMessage();
        storeMessage.setSource(message);
        storeMessage.setSubject(message.getSubject());
        storeMessage.setMessageId(message.getMessageId());
        storeMessage.setContent(JsonUtils.toJsonString(message));
        storeMessage.setCreateTime(new Date(message.getCreateTime()));
        return storeMessage;
    }

    public static BrokerMessage toBrokerMessage(Message message) {
        BrokerMessage brokerMessage = new BrokerMessage();
        brokerMessage.setSource(message);
        brokerMessage.setSubject(message.getSubject());
        brokerMessage.setContent(JsonUtils.toJsonString(message));
        brokerMessage.setMessageId(message.getMessageId());
        brokerMessage.setCreateTime(new Date(message.getCreateTime()));
        brokerMessage.setTryCount(message.getTimes());
        return brokerMessage;
    }
}
