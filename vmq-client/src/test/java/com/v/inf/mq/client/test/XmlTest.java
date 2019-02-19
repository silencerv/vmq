package com.v.inf.mq.client.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class XmlTest {
    /**
     * Rigorous Test :-)
     */
    @Test
    public void TestConsumer() throws IOException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(new String[]{"spring/mq-consumer.xml"}, false);
        ((ClassPathXmlApplicationContext) applicationContext).refresh();
        System.in.read();
    }
}
