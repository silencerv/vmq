package com.v;

import com.v.inf.mq.Message;
import com.v.inf.mq.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class WaitMessageListener implements MessageListener {
    private static Logger logger = LoggerFactory.getLogger(WaitMessageListener.class);

    @Override
    public void onMessage(Message message) {
        try {
            logger.info("!!!!!!!!!!!");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
