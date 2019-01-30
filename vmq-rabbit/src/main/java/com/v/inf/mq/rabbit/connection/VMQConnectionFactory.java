package com.v.inf.mq.rabbit.connection;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

import java.net.URI;

/**
 * @anthor v
 * Create on 2019/1/13
 */
public class VMQConnectionFactory extends CachingConnectionFactory {

    public VMQConnectionFactory() {
    }

    public VMQConnectionFactory(String hostname) {
        super(hostname);
    }

    public VMQConnectionFactory(int port) {
        super(port);
    }

    public VMQConnectionFactory(String hostNameArg, int port) {
        super(hostNameArg, port);
    }

    public VMQConnectionFactory(URI uri) {
        super(uri);
    }

    public VMQConnectionFactory(ConnectionFactory rabbitConnectionFactory) {
        super(rabbitConnectionFactory);
    }
}
