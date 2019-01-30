package com.v.inf.mq.client.spring.parser;

import com.v.inf.mq.client.annotation.MQConsumerAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * @anthor v
 * Create on 2019/1/15
 */
public class MQAnnotationDrivenParser extends AbstractSingleBeanDefinitionParser {

    public static final String BEAN_NAME = "vmQConsumerAnnotationBeanPostProcessor";

    @Override
    protected Class<?> getBeanClass(Element element) {
        return MQConsumerAnnotationBeanPostProcessor.class;
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) {
        return BEAN_NAME;
    }

    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        if (element.getLocalName().equals("annotation-driven")) {
            String order = element.getAttribute("order");
            if (StringUtils.hasLength(order)) {
                builder.addPropertyValue("order", Integer.parseInt(order));
            }
        }
    }
}
