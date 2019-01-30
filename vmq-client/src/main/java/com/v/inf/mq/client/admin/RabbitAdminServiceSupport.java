package com.v.inf.mq.client.admin;

import com.v.inf.mq.client.listener.ConsumerContext;
import com.v.inf.mq.client.listener.ConsumerListenerContainer;
import com.v.inf.mq.client.listener.VMQListenerAdaptor;
import com.v.inf.mq.rabbit.connection.VMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @anthor v
 * Create on 2019/1/15
 */
public class RabbitAdminServiceSupport implements RabbitAdminService {

    private static Logger logger = LoggerFactory.getLogger(RabbitAdminServiceSupport.class);

    /**
     * 可以通过指定jvm参数来指定配置文件位置
     */
    private static final String MQ_PROPERTIES_NAME = "vmq.properties.path";

    private static final String DEFAULT_MQ_PROPERTIES = "vmq.properties";

    private List<ConsumerListenerContainer> listenerContainers = new ArrayList<>();

    private ConnectionFactory connectionFactory;

    private RabbitAdmin rabbitAdmin;

    public RabbitAdminServiceSupport() {
        Properties properties;
        try {
            logger.info("load vmq properties :{}", getPropertiesFileName());
            properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(getPropertiesFileName()));
        } catch (IOException e) {
            logger.error("load vmq properties fail!", e);
            throw new RuntimeException("load vmq properties fail!");
        }
        String hosts = properties.getProperty("mq.connectionFactory.hosts");
        String username = properties.getProperty("mq.connectionFactory.username");
        String password = properties.getProperty("mq.connectionFactory.password");
        VMQConnectionFactory vmqConnectionFactory = new VMQConnectionFactory(hosts);
        vmqConnectionFactory.setUsername(username);
        vmqConnectionFactory.setPassword(password);
        vmqConnectionFactory.setPublisherConfirms(true);
        connectionFactory = vmqConnectionFactory;
        rabbitAdmin = new RabbitAdmin(connectionFactory);
    }

    private String getPropertiesFileName() {
        return System.getProperty(MQ_PROPERTIES_NAME, DEFAULT_MQ_PROPERTIES);
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    @Override
    public void addListener(VMQListenerAdaptor VMQListenerAdaptor) {
        listenerContainers.add(registerListener(VMQListenerAdaptor));
    }

    private ConsumerListenerContainer registerListener(VMQListenerAdaptor VMQListenerAdaptor) {
        ConsumerListenerContainer listenerContainer = new ConsumerListenerContainer(connectionFactory);
        ConsumerContext consumerContext = VMQListenerAdaptor.getConsumerContext();
        listenerContainer.setSubject(consumerContext.getSubject());
        listenerContainer.setGroup(consumerContext.getGroup());
        listenerContainer.setPrefetchCount(consumerContext.getPrefetchCount());
        listenerContainer.setConcurrency(String.valueOf(consumerContext.getConcurrency()));
        listenerContainer.setMaxConcurrentConsumers(consumerContext.getMaxConcurrentConsumers());
        listenerContainer.setMessageListener(VMQListenerAdaptor);
        listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
        listenerContainer.setDefaultRequeueRejected(consumerContext.isRequeueReject());
        listenerContainer.setAutoStartup(true);
        listenerContainer.afterPropertiesSet();
        //start
        listenerContainer.start();
        return listenerContainer;
    }

}
