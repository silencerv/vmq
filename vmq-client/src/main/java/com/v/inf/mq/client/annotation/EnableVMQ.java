package com.v.inf.mq.client.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 {@link MqConsumer} 生效的注解
 * 支持Spring Boot
 *
 * @author v
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(WMQAnnotationConfigurationSelector.class)
public @interface EnableVMQ {


}
