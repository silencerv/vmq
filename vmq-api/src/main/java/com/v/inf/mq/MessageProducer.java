package com.v.inf.mq;

import com.v.inf.mq.exception.MessageException;

/**
 * @anthor v
 * Create on 2019/1/7
 */
public interface MessageProducer {

    /**
     * 发送消息
     * 注意：在使用事务性消息时该方法仅将消息入库，当事务成功提交时才发送消息。
     *
     * @param message
     * @throws MessageException todo
     */
    void send(Message message) throws MessageException;
}
