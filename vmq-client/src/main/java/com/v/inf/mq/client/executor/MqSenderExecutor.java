package com.v.inf.mq.client.executor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.v.inf.mq.client.producer.MessageSender;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @anthor v
 * Create on 2019/1/17
 */
public class MqSenderExecutor {

    private static final int QUEUE_SIZE = 1000;
    private static final int THREAD_SIZE = 5;
    private static final String THREAD_NAME_PREFIX = "mq-client-send-pool-";

    private static ExecutorService executorService = new ThreadPoolExecutor(THREAD_SIZE, THREAD_SIZE, 0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(QUEUE_SIZE), new ThreadFactoryBuilder().setNameFormat(THREAD_NAME_PREFIX).build());

    public static void asyncSend(MessageSender messageSender) {
        executorService.submit(() -> messageSender.send());
    }
}