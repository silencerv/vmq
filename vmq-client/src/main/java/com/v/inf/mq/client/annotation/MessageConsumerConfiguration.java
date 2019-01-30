package com.v.inf.mq.client.annotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 注册BeanPost
 */
@Configuration
class MessageConsumerConfiguration implements ImportAware {

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        //do nothing
    }

    @Bean
    public MQConsumerAnnotationBeanPostProcessor mqConsumerAnnotationBeanPostProcessor() {
        MQConsumerAnnotationBeanPostProcessor consumerAnnotationBeanPostProcessor = new MQConsumerAnnotationBeanPostProcessor();
        return consumerAnnotationBeanPostProcessor;
    }
}
