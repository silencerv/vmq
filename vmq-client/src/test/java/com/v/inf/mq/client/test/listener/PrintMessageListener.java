package com.v.inf.mq.client.test.listener;

import com.v.inf.mq.Message;
import com.v.inf.mq.MessageListener;
import com.v.inf.mq.common.util.JsonUtils;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class PrintMessageListener implements MessageListener {

    //    @MqConsumer(subjects = "test", group = "lisa")
    @Override
    public void onMessage(Message message) {
//        throw new RuntimeException("1");
        System.out.println(JsonUtils.toJsonString(message));
    }
}
