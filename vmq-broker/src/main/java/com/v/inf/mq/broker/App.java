package com.v.inf.mq.broker;

import com.v.inf.mq.broker.rabbit.BrokerConfirmCallback;
import com.v.inf.mq.client.admin.RabbitAdminService;
import com.v.inf.mq.client.annotation.EnableVMQ;
import com.v.inf.mq.common.dubbo.extension.ExtensionLoader;
import com.v.inf.mq.rabbit.broker.RabbitBroker;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @anthor v
 * Create on 2019/1/14
 */
@SpringBootApplication
@EnableVMQ
@ComponentScan("com.v.inf.mq")
@MapperScan(basePackages = "com.v.inf.mq.broker.store.mapper")
public class App implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    @Bean
    public RabbitBroker buildRabbitBroker(BrokerConfirmCallback brokerConfirmCallback) {
        RabbitAdminService defaultExtension = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
        RabbitBroker rabbitBroker = new RabbitBroker(defaultExtension.getConnectionFactory());
        rabbitBroker.setConfirmCallback(brokerConfirmCallback);
        return rabbitBroker;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Joining thread, you can press Ctrl+C to shutdown application");
//        Thread.currentThread().join();
    }
}
