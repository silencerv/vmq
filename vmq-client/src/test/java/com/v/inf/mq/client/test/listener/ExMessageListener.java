package com.v.inf.mq.client.test.listener;

import com.v.inf.mq.Message;
import com.v.inf.mq.MessageListener;
import com.v.inf.mq.client.annotation.MqConsumer;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class ExMessageListener implements MessageListener {

    @MqConsumer(subjects = "test", group = "lisa")
    @Override
    public void onMessage(Message message) {
        System.out.println("这里抛异常啦！！！！！！！！！！！！！");
        throw new RuntimeException("1");
//        System.out.println(ReflectionToStringBuilder.toString(message));
    }
}
