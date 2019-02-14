package com.v.inf.mq.client.producer;

import com.google.common.base.Preconditions;
import com.v.inf.mq.Message;
import com.v.inf.mq.MessageProducer;
import com.v.inf.mq.client.admin.RabbitAdminService;
import com.v.inf.mq.client.service.DbMessageStoreService;
import com.v.inf.mq.common.db.DataSourceUtil;
import com.v.inf.mq.constants.Constants;
import com.v.inf.mq.exception.MessageException;
import com.v.inf.mq.rabbit.broker.RabbitBroker;
import com.v.inf.mq.store.message.Messages;
import com.v.inf.mq.store.message.ProducerMessage;
import com.v.inf.mq.store.message.StoreMessage;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.Objects;

/**
 * @anthor v
 * Create on 2019/1/28
 */
public abstract class AbstractDbMessageProducer implements MessageProducer, InitializingBean {

    private static Logger LOGGER = LoggerFactory.getLogger(AdaptiveDbMessageProducer.class);

    protected DataSource dataSource;

    protected ConnectionFactory connectionFactory;

    protected RabbitBroker rabbitBroker;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public ConnectionFactory getConnectionFactory() {
        return connectionFactory;
    }

    public void setConnectionFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (Objects.isNull(connectionFactory)) {
            RabbitAdminService rabbitAdminService = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
            setConnectionFactory(rabbitAdminService.getConnectionFactory());
        }
        rabbitBroker = new RabbitBroker(connectionFactory);
        rabbitBroker.setCallbackKeyFunction(new ProducerCallbackKeyBuilder());
        rabbitBroker.setConfirmCallback(new ProducerConfirmCallback());
        rabbitBroker.afterPropertiesSet();

    }

    @Override
    public void send(Message message) throws MessageException {
        checkMessage(message);
        boolean txSynchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
        StoreMessage storeMessage = Messages.toStoreMessage(message);
        //如果存在活动事务
        if (txSynchronizationActive) {
            DbMessageStoreService storeService = getStoreService();
            ProducerMessage producerMessage = storeService.insert(storeMessage);
            String dataSourceKey = DataSourceUtil.getDataSourceKey(producerMessage.getOperateDataSource());
            message.setAttr(Constants.DB_KEY, dataSourceKey);
            MessageSender messageSender = new MessageSender(rabbitBroker, producerMessage);
            MessageManager.addSender(messageSender);
            LOGGER.debug("save message to db , id :{}, dbKey: {}", message.getMessageId(), dataSourceKey);
        } else {
            rabbitBroker.send(message);
            LOGGER.debug("send not transaction message :{}", message.getMessageId());
        }

    }

    /**
     * 子类去实现
     * 本质上是如何绑定数据源
     *
     * @return
     */
    protected abstract DbMessageStoreService getStoreService();

    protected void checkMessage(Message message) {
        Preconditions.checkNotNull(message, "message can not be null!");
        Preconditions.checkArgument(StringUtils.isNotBlank(message.getMessageId()), "message id can not be empty!");
        Preconditions.checkArgument(StringUtils.isNotBlank(message.getSubject()), "subject can not be empty!");
    }


}
