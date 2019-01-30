package com.v;


import com.v.inf.mq.task.App;
import com.v.inf.mq.task.executor.BrokerRetryExecutor;
import com.v.inf.mq.task.executor.ProducerRetryExecutor;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Unit test for simple App.
 */
public class TaskTest {

    @Test
    public void TestRetryTask() throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class);
        BrokerRetryExecutor brokerRetryExecutor = applicationContext.getBean(BrokerRetryExecutor.class);
        brokerRetryExecutor.execute();
        System.in.read();
    }

    @Test
    public void TestProducerTask() throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class);
        ProducerRetryExecutor retryExecutor = applicationContext.getBean(ProducerRetryExecutor.class);
        retryExecutor.execute();
        System.in.read();
    }
}
