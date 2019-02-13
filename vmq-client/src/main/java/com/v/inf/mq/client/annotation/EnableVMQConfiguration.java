package com.v.inf.mq.client.annotation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

/**
 * 注册BeanPost
 * 注册Producer
 */
public class EnableVMQConfiguration implements ImportAware, ImportBeanDefinitionRegistrar {

    private static Logger logger = LoggerFactory.getLogger(EnableVMQConfiguration.class);

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        //do nothing
    }

    @Bean
    public MQConsumerAnnotationBeanPostProcessor mqConsumerAnnotationBeanPostProcessor() {
        MQConsumerAnnotationBeanPostProcessor consumerAnnotationBeanPostProcessor = new MQConsumerAnnotationBeanPostProcessor();
        return consumerAnnotationBeanPostProcessor;
    }

    /**
     * 注册producer
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes enableVMQ = AnnotationAttributes.fromMap(
                importingClassMetadata.getAnnotationAttributes(EnableVMQ.class.getName(), false));
        if (Objects.isNull(enableVMQ))
            return;
        AnnotationAttributes[] producers = enableVMQ.getAnnotationArray("producers");
        if (Objects.nonNull(producers)) {
            for (AnnotationAttributes producer : producers) {
                String dataSource = producer.getString("dataSource");
                String name = producer.getString("name");
                Class<?> type = producer.getClass("type");

                logger.info("register producer, type:{} , dataSource:{}", type, dataSource);
                BeanDefinitionBuilder producerDefinition = BeanDefinitionBuilder.rootBeanDefinition(type);
                producerDefinition.addPropertyReference("dataSource", dataSource);
                registry.registerBeanDefinition(name, producerDefinition.getBeanDefinition());
            }
        }
    }

}
