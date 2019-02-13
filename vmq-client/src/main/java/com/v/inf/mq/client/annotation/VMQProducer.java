package com.v.inf.mq.client.annotation;

import com.v.inf.mq.client.producer.SingleDbMessageProducer;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @anthor v
 * Create on 2019/2/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VMQProducer {

    String name() default "producer";

    /**
     * spring 中数据源bean的name
     * 查找不到将会报错
     * @return
     */
    String dataSource() default "dataSource";

    /**
     * 预留配置
     * @return
     */
    Class type() default SingleDbMessageProducer.class;
}
