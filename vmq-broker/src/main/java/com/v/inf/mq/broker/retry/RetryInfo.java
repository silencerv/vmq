package com.v.inf.mq.broker.retry;

/**
 * @anthor v
 * Create on 2019/1/17
 */
public class RetryInfo {

    /**
     * 本次发送时间
     */
    private long firstTime;

    /**
     * 第几次发送
     */
    private int thisCount;

    public RetryInfo(long firstTime, int thisCount) {
        this.firstTime = firstTime;
        this.thisCount = thisCount;
    }

    public long getFirstTime() {
        return firstTime;
    }

    public void setFirstTime(long firstTime) {
        this.firstTime = firstTime;
    }

    public int getThisCount() {
        return thisCount;
    }

    public void setThisCount(int thisCount) {
        this.thisCount = thisCount;
    }
}
