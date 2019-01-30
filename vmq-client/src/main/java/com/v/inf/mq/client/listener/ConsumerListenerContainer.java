package com.v.inf.mq.client.listener;

import com.v.inf.mq.rabbit.constants.DeadLetterExchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class ConsumerListenerContainer extends SimpleMessageListenerContainer {

    public ConsumerListenerContainer() {
    }

    public ConsumerListenerContainer(ConnectionFactory connectionFactory) {
        super(connectionFactory);
        rabbitAdmin = new RabbitAdmin(connectionFactory);
    }

    private String subject;

    private String group;

    private RabbitAdmin rabbitAdmin;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setRabbitAdmin(RabbitAdmin rabbitAdmin) {
        this.rabbitAdmin = rabbitAdmin;
    }

    @Override
    protected void doStart() throws Exception {
        //声明exchange and queue
        declareAndBind();
        setQueueNames(getQueueName());
        super.doStart();
    }

    private void declareAndBind() {
        Exchange exchange = ExchangeBuilder.directExchange(subject).durable(true).build();
        rabbitAdmin.declareExchange(exchange);
        String queueName = getQueueName();
        Queue queue = QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", DeadLetterExchange.NAME)
                .withArgument("x-dead-letter-routing-key", StringUtils.EMPTY)
                .build();
        rabbitAdmin.declareQueue(queue);
        Binding queueBinding = BindingBuilder.bind(queue).to(exchange).with(Strings.EMPTY).noargs();
        rabbitAdmin.declareBinding(queueBinding);
    }

    //获取queue name
    private String getQueueName() {
        return subject + "$" + group;
    }
}
