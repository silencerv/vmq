package com.v;

import com.v.inf.mq.Message;
import com.v.inf.mq.client.producer.SingleDbMessageProducer;
import com.v.inf.mq.message.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
@EnableAutoConfiguration
@ComponentScans({@ComponentScan(basePackages = "com.v.inf.mq.client")})
public class BootTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    SingleDbMessageProducer singleDbMessageProducer;

    @Bean
    public JdbcTemplate buildJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public SingleDbMessageProducer buildDbMessageProducer(DataSource dataSource) {
        SingleDbMessageProducer singleDbMessageProducer = new SingleDbMessageProducer();
        singleDbMessageProducer.setDataSource(dataSource);
        return singleDbMessageProducer;
    }

//    @Bean
//    public MQTransactionManager buildMQTransactionManager(DataSource dataSource) {
//        return new MQTransactionManager(dataSource);
//    }

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = SpringApplication.run(App.class, args);
        BootTest app = applicationContext.getBean(BootTest.class);
        for (int i = 0; i < 100; i++) {
            app.selectStu();
        }
    }

    private void sendMessage() {
        Message message = MessageBuilder.aBasicMessage()
                .withSubject("test")
                .build();
        message.setAttr("content", "hahaha");
        singleDbMessageProducer.send(message);
    }

    @Transactional
    public void selectStu() {
        sendMessage();
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from stu.stu limit 3");
        System.out.println(maps);
    }
}
