package com.v.inf.mq.broker.retry;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/**
 * @anthor v
 * Create on 2019/1/17
 */
public class PowerIncrementRetry implements Retry {

    private long intervalSecond;

    public PowerIncrementRetry(long intervalSecond) {
        this.intervalSecond = intervalSecond;
    }


    @Override
    public Date nextRetry(Date firstTime, int count) {
        return DateUtils.addSeconds(firstTime, (int) Math.pow(intervalSecond, count));
    }
}
