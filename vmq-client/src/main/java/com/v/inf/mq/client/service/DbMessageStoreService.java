package com.v.inf.mq.client.service;

import com.sun.istack.internal.Nullable;
import com.v.inf.mq.store.message.ProducerMessage;
import com.v.inf.mq.store.message.StoreMessage;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.Date;
import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/9
 */
public class DbMessageStoreService {

    /**
     * 默认数据源
     * 若当前无事务，则会使用默认数据源
     */
    @Nullable
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private RowMapper<StoreMessage> rowMapper = new BeanPropertyRowMapper<>(StoreMessage.class);
    ;

    public DbMessageStoreService(DataSource dataSource) {
        this.dataSource = dataSource;
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public ProducerMessage insert(StoreMessage storeMessage) {
        jdbcTemplate.update(SqlStatement.INSERT, storeMessage.getMessageId(), storeMessage.getSubject(),
                storeMessage.getContent());

        ProducerMessage producerMessage = new ProducerMessage();
        producerMessage.setStoreMessage(storeMessage);
        producerMessage.setOperateDataSource(dataSource);
        return producerMessage;
    }

    public void success(String messageId) {
        jdbcTemplate.update(SqlStatement.DELETE, messageId);
    }

    public List<StoreMessage> queryReady(Date startTime, int limit) {
        return jdbcTemplate.query(SqlStatement.QUERY_READY, new Object[]{startTime, limit}, rowMapper);
    }
}
