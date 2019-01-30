package com.v.inf.mq.client.tx;

import com.v.inf.mq.client.executor.MqSenderExecutor;
import com.v.inf.mq.client.producer.MessageManager;
import com.v.inf.mq.client.producer.MessageSender;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import javax.sql.DataSource;
import java.util.List;

/**
 * 事务结束后立即发送消息
 * 如果没有配置此类，task补偿也会发送消息
 *
 * @anthor v
 * Create on 2019/1/9
 */
public class MQTransactionManager extends DataSourceTransactionManager {

    public MQTransactionManager(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        super.doCommit(status);
        List<MessageSender> messages = MessageManager.clear();
        //send message
        messages.forEach(MqSenderExecutor::asyncSend);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        super.doRollback(status);
        MessageManager.clear();
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        MessageManager.clear();
    }
}
