package com.v.inf.mq.client.admin;

import com.v.inf.mq.client.listener.VMQListenerAdaptor;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.SPI;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 * @anthor v
 * Create on 2019/1/15
 */
@SPI(Constants.DEFAULT_KEY)
public interface RabbitAdminService {

    ConnectionFactory getConnectionFactory();

    void addListener(VMQListenerAdaptor listenerAdaptor);
}
