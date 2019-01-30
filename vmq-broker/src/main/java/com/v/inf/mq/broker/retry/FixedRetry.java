package com.v.inf.mq.broker.retry;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @anthor v
 * Create on 2019/1/17
 */
public class FixedRetry implements Retry {

    private int interval;

    public FixedRetry(int intervalSecond) {
        this.interval = intervalSecond;
    }

    @Override
    public Date nextRetry(Date firstTime, int count) {
        return DateUtils.addSeconds(firstTime, count * interval);
    }
}
