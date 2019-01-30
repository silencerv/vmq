package com.v.inf.mq.broker.exector;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @anthor v
 * Create on 2019/1/9
 */
public class MqSendExecutor extends ThreadPoolExecutor {

    private static final int QUEUE_SIZE = 1000;
    private static final int THREAD_SIZE = 5;
    private static ThreadFactory threadFactory;

    static {
        ThreadFactoryBuilder threadFactoryBuilder = new ThreadFactoryBuilder();
        threadFactoryBuilder.setNameFormat("mq-send-thread-pool-");
        threadFactory = threadFactoryBuilder.build();
    }

    public MqSendExecutor() {
        super(THREAD_SIZE, THREAD_SIZE, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(QUEUE_SIZE), threadFactory);
    }
}
