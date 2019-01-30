package com.v.inf.mq.rabbit.converter;

import com.v.inf.mq.common.util.JsonUtils;
import com.v.inf.mq.message.BaseMessage;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

/**
 * @anthor v
 * Create on 2019/1/16
 */
public class JSONMessageConverter implements MessageConverter {

    @Override
    public Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
        return new Message(JsonUtils.toJsonString(object).getBytes(), messageProperties);
    }

    @Override
    public Object fromMessage(Message message) throws MessageConversionException {
        return JsonUtils.toBean(message.getBody(), BaseMessage.class);
    }
}
