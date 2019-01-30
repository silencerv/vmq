package com.v.inf.mq.rabbit.broker;

import com.v.inf.mq.Message;
import com.v.inf.mq.MessageBroker;
import com.v.inf.mq.common.sync.Holder;
import com.v.inf.mq.exception.MessageException;
import com.v.inf.mq.rabbit.converter.JSONMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.retry.backoff.NoBackOffPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @anthor v
 * Create on 2019/1/8
 */
public class RabbitBroker implements MessageBroker, InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(RabbitBroker.class);

    private ConnectionFactory rabbitConnectionFactory;

    private RabbitTemplate.ConfirmCallback confirmCallback;

    private RabbitAdmin rabbitAdmin;

    private RabbitTemplate rabbitTemplate;

    private Function<Message, String> callbackKeyFunction;

    private Map<String, Holder<String>> declaredExchange = new ConcurrentHashMap<>();

    public RabbitBroker() {
    }

    public RabbitBroker(ConnectionFactory rabbitConnectionFactory) {
        this.rabbitConnectionFactory = rabbitConnectionFactory;
    }

    public RabbitTemplate.ConfirmCallback getConfirmCallback() {
        return confirmCallback;
    }

    public void setConfirmCallback(RabbitTemplate.ConfirmCallback confirmCallback) {
        this.confirmCallback = confirmCallback;
    }

    public Function<Message, String> getCallbackKeyFunction() {
        return callbackKeyFunction;
    }

    public void setCallbackKeyFunction(Function<Message, String> callbackKeyFunction) {
        this.callbackKeyFunction = callbackKeyFunction;
    }

    /**
     * 内置字段初始化
     */
    @Override
    public void afterPropertiesSet() {
        rabbitAdmin = new RabbitAdmin(rabbitConnectionFactory);
        buildRabbitTemplate(rabbitConnectionFactory);
    }

    @Override
    public void send(Message message) throws MessageException {
        try {
            sendInternal(message);
        } catch (Throwable t) {
            logger.error("send message fail: {}", message.getMessageId(), t);
            throw new MessageException(t);
        }

    }

    private void sendInternal(Message message) {
        String subject = message.getSubject();
        //declare exchange
        declareExchange(subject);
        //send
        CorrelationData correlationData = buildCorrelationData(message);
        rabbitTemplate.setMessageConverter(new JSONMessageConverter());
        rabbitTemplate.convertAndSend(subject, message.getRoutingKey(), message, correlationData);
    }

    /**
     * 构造rabbit template
     *
     * @return
     */
    private RabbitTemplate buildRabbitTemplate(ConnectionFactory cachingConnectionFactory) {
        rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        //retry
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setBackOffPolicy(new NoBackOffPolicy());
        rabbitTemplate.setRetryTemplate(retryTemplate);
        //confirm broker
        rabbitTemplate.setConfirmCallback(confirmCallback);
        return rabbitTemplate;
    }

    /**
     * 声明exchange
     * 避免net io，只声明一次
     *
     * @param subject
     */
    private void declareExchange(String subject) {
        Holder<String> holder = declaredExchange.get(subject);
        if (Objects.isNull(holder)) {
            declaredExchange.putIfAbsent(subject, new Holder<>());
            holder = declaredExchange.get(subject);
        }
        if (Objects.isNull(holder.get())) {
            synchronized (holder) {
                if (Objects.isNull(holder.get())) {
                    Exchange exchange = ExchangeBuilder.directExchange(subject).durable(true).build();
                    rabbitAdmin.declareExchange(exchange);
                    logger.info("declare exchange :{}", subject);
                    holder.set(subject);
                }
            }
        }
    }

    /**
     * 构造confirm broker key
     *
     * @param message
     * @return
     */
    private CorrelationData buildCorrelationData(Message message) {
        String messageId = message.getMessageId();
        if (Objects.nonNull(callbackKeyFunction)) {
            messageId = callbackKeyFunction.apply(message);
        }
        return new CorrelationData(messageId);
    }

}
