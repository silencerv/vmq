package com.v.inf.mq.broker.retry;

import java.util.Date;

public interface Retry {

    /**
     * @param firstTime 第一次发送时间
     * @param count     重试次数
     * @return nextRetry 下一次重试时间
     */
    Date nextRetry(Date firstTime, int count);
}
