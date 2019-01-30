package com.v.inf.mq.client.service;

/**
 * @anthor v
 * Create on 2019/1/10
 */
public class SqlStatement {

    public static final String INSERT = "insert into mq.message_producer(message_id,subject,content,create_time) values (?, ?, ?, now())";
    public static final String DELETE = "delete from mq.message_producer where message_id = ?";
    public static final String QUERY_READY = "select * from mq.message_producer where create_time < ? limit ?";
//    public static final String QUERY_READY = "select * from mq.message_producer limit 1";
}
