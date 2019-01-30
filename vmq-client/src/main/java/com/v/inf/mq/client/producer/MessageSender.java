package com.v.inf.mq.client.producer;

import com.v.inf.mq.MessageBroker;
import com.v.inf.mq.store.message.ProducerMessage;

/**
 * @anthor v
 * Create on 2019/1/14
 */
public class MessageSender {

    private MessageBroker messageBroker;

    private ProducerMessage producerMessage;

    public MessageSender(MessageBroker messageBroker, ProducerMessage producerMessage) {
        this.messageBroker = messageBroker;
        this.producerMessage = producerMessage;
    }

    public MessageBroker getMessageBroker() {
        return messageBroker;
    }

    public void setMessageBroker(MessageBroker messageBroker) {
        this.messageBroker = messageBroker;
    }

    public ProducerMessage getProducerMessage() {
        return producerMessage;
    }

    public void setProducerMessage(ProducerMessage producerMessage) {
        this.producerMessage = producerMessage;
    }

    public void send() {
        messageBroker.send(producerMessage.getStoreMessage().getSource());
    }
}
