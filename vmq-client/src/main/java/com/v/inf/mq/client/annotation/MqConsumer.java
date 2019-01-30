package com.v.inf.mq.client.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Inherited
public @interface MqConsumer {

    /**
     * 消息主题
     * 可以为多个，以,分割
     *
     * @return
     */
    String subjects() default "";

    /**
     * group
     *
     * @return
     */
    String group() default "";

    /**
     * 并发相关
     * 详见rabbit amqp
     * 消费者线程队列等待数量
     *
     * @return
     */
    int prefetchCount() default 1;

    /**
     * 并发消费者数量
     *
     * @return
     */
    int concurrency() default 10;

    /**
     * 最大并发消费者数量
     *
     * @return
     */
    int maxConcurrentConsumers() default 10;

    boolean requeueReject() default false;
}
