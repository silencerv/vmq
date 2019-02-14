package com.v.inf.mq.task;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


/**
 * @anthor v
 * Create on 2019/1/14
 */
@SpringBootApplication
@PropertySource({"classpath:application-corn.properties"})
@ComponentScan("com.v.inf.mq")
@MapperScan(basePackages = "com.v.inf.mq.broker.store.mapper")
public class App implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Joining thread, you can press Ctrl+C to shutdown application");
        Thread.currentThread().join();
    }
}
