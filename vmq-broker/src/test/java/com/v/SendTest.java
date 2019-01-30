package com.v;

import com.v.inf.mq.Message;
import com.v.inf.mq.broker.App;
import com.v.inf.mq.broker.VmqBroker;
import com.v.inf.mq.message.MessageBuilder;
import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Unit test for simple App.
 */
public class SendTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void TestSend() throws Exception {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(App.class);
        VmqBroker vmqBroker = applicationContext.getBean(VmqBroker.class);
        for (int i = 0; i < 10; i++) {
            sendMessage(vmqBroker);
        }
        System.in.read();
    }

    private void sendMessage(VmqBroker vmqBroker) {
        Message message = MessageBuilder.aBasicMessage()
                .withSubject("test")
                .build();
        message.setAttr("content", "hahahaha");
        vmqBroker.send(message);
    }
}
