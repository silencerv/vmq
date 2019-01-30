package com.v.inf.mq.client.listener;

/**
 * @anthor v
 * Create on 2019/1/15
 */
public class ConsumerContext {

    private String subject;

    private String group;

    private int prefetchCount;

    private int concurrency;

    private int maxConcurrentConsumers;

    private boolean requeueReject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public int getPrefetchCount() {
        return prefetchCount;
    }

    public void setPrefetchCount(int prefetchCount) {
        this.prefetchCount = prefetchCount;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }

    public int getMaxConcurrentConsumers() {
        return maxConcurrentConsumers;
    }

    public void setMaxConcurrentConsumers(int maxConcurrentConsumers) {
        this.maxConcurrentConsumers = maxConcurrentConsumers;
    }

    public boolean isRequeueReject() {
        return requeueReject;
    }

    public void setRequeueReject(boolean requeueReject) {
        this.requeueReject = requeueReject;
    }
}
