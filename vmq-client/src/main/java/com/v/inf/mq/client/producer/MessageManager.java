package com.v.inf.mq.client.producer;

import org.springframework.core.NamedThreadLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/14
 */
public class MessageManager extends NamedThreadLocal<List<MessageSender>> {

    private static ThreadLocal<List<MessageSender>> MESSAGE_SENDERS = new MessageManager("Message Sender Resources");

    public MessageManager(String name) {
        super(name);
    }

    @Override
    protected List<MessageSender> initialValue() {
        return new ArrayList<>();
    }

    public static void addSender(MessageSender messageSender) {
        MESSAGE_SENDERS.get().add(messageSender);
    }

    public static List<MessageSender> clear() {
        List<MessageSender> messageSenders = Collections.unmodifiableList(MESSAGE_SENDERS.get());
        MESSAGE_SENDERS.remove();
        return messageSenders;
    }
}
