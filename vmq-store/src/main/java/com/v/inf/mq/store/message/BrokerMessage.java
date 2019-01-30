package com.v.inf.mq.store.message;

import java.util.Date;

/**
 * @anthor v
 * Create on 2019/1/8
 */
public class BrokerMessage extends StoreMessage {

    private int tryCount;

    private int status;

    private Date nextRetry;

    public int getTryCount() {
        return tryCount;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getNextRetry() {
        return nextRetry;
    }

    public void setNextRetry(Date nextRetry) {
        this.nextRetry = nextRetry;
    }
}
