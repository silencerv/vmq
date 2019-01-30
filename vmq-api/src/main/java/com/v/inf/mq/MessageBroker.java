package com.v.inf.mq;

import com.v.inf.mq.exception.MessageException;

/**
 * @anthor v
 * Create on 2019/1/7
 */
public interface MessageBroker {

    /**
     * 发送消息，投递到消费者
     *
     * @param message
     * @throws MessageException
     */
    void send(Message message) throws MessageException;
}
