package com.v.inf.mq.client.spring.parser;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class MqNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("consumer", new MQConsumerParse());
        registerBeanDefinitionParser("annotation-driven", new MQAnnotationDrivenParser());
    }
}
