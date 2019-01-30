package com.v.inf.mq.broker.store.impl;

import com.v.inf.mq.broker.store.MessageStoreService;
import com.v.inf.mq.broker.store.mapper.MessageMapper;
import com.v.inf.mq.store.message.BrokerMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/14
 */
@Repository
public class MessageStoreServiceImpl implements MessageStoreService {

    private static Logger logger = LoggerFactory.getLogger(MessageStoreServiceImpl.class);

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void save(BrokerMessage storeMessage) {
        messageMapper.insert(storeMessage);
    }

    @Override
    public void success(long id) {
        logger.info("success message use column id : {}", id);
        messageMapper.deleteById(id);
    }

    @Override
    public void success(String messageId) {
        logger.info("success message use message id : {}", messageId);
        messageMapper.deleteByMessageId(messageId);
    }

    @Override
    public List<BrokerMessage> queryReady(Date startTime, int limit) {
        return messageMapper.queryReady(startTime, limit);
    }
}
