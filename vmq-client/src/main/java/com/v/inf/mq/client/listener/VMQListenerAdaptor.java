package com.v.inf.mq.client.listener;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @anthor v
 * Create on 2019/1/15
 */
public class VMQListenerAdaptor extends MessageListenerAdapter {

    public VMQListenerAdaptor() {
    }

    public VMQListenerAdaptor(Object delegate) {
        super(delegate);
    }

    public VMQListenerAdaptor(Object delegate, MessageConverter messageConverter) {
        super(delegate, messageConverter);
    }

    public VMQListenerAdaptor(Object delegate, String defaultListenerMethod) {
        super(delegate, defaultListenerMethod);
    }

    private ConsumerContext consumerContext;

    public ConsumerContext getConsumerContext() {
        return consumerContext;
    }

    public void setConsumerContext(ConsumerContext consumerContext) {
        this.consumerContext = consumerContext;
    }

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        //TODO do something
        super.onMessage(message, channel);
    }
}
