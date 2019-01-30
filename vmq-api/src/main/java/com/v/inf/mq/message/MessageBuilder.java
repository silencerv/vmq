package com.v.inf.mq.message;

import com.v.inf.mq.Message;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public final class MessageBuilder {
    private String messageId;
    private String subject;
    private String routingKey;
    private Map<String, Object> attrs = new HashMap<String, Object>();
    private long createTime = System.currentTimeMillis();
    private int delayMills;
    private int times;

    private MessageBuilder() {
    }

    public static MessageBuilder aBasicMessage() {
        return new MessageBuilder();
    }

    public MessageBuilder withMessageId(String messageId) {
        this.messageId = messageId;
        return this;
    }

    public MessageBuilder withSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MessageBuilder withRoutingKey(String routingKey) {
        this.routingKey = routingKey;
        return this;
    }

    public MessageBuilder withAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
        return this;
    }

    public MessageBuilder withDelayMills(int delayMills) {
        this.delayMills = delayMills;
        return this;
    }

    public MessageBuilder withTimes(int times) {
        this.times = times;
        return this;
    }

    public MessageBuilder withCreateTime(long createTime) {
        this.createTime = createTime;
        return this;
    }

    public Message build() {
        if (StringUtils.isBlank(messageId)) {
            messageId = UUID.randomUUID().toString().replaceAll("-", "");
        }
        BaseMessage baseMessage = new BaseMessage();
        baseMessage.setMessageId(messageId);
        baseMessage.setSubject(subject);
        baseMessage.setRoutingKey(routingKey);
        baseMessage.setAttrs(attrs);
        baseMessage.setCreateTime(createTime);
        baseMessage.setDelayMills(delayMills);
        baseMessage.setTimes(times);
        return baseMessage;
    }
}
