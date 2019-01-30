package com.v.inf.mq.client.spring.parser;

import com.v.inf.mq.client.listener.ConsumerContext;
import com.v.inf.mq.client.listener.RabbitConsumerAdmin;
import com.v.inf.mq.client.listener.VMQListenerAdaptor;
import org.springframework.amqp.rabbit.config.NamespaceUtils;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;

import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class MQConsumerParse extends AbstractBeanDefinitionParser {

    private static final String BEAN_NAME = "rabbitConsumerAdmin";

    private static final String NODE_LISTENER = "listener";

    private static final String ATTR_LISTENER_SUBJECT = "subject";

    private static final String ATTR_LISTENER_GROUP = "group";

    private static final String ATTR_LISTENER_REF = "ref";

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
        return BEAN_NAME;
    }

    @Override
    protected AbstractBeanDefinition parseInternal(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(RabbitConsumerAdmin.class);
        //parse listener
        List<Element> listeners = DomUtils.getChildElementsByTagName(element, NODE_LISTENER);
        parseListeners(listeners, builder, parserContext);
        return builder.getBeanDefinition();
    }

    /**
     * 解析listener标签
     *
     * @param listeners
     * @param consumerAdminDefinition
     */
    private void parseListeners(List<Element> listeners, BeanDefinitionBuilder consumerAdminDefinition, ParserContext parserContext) {
        ManagedList<BeanDefinition> managedList = new ManagedList<BeanDefinition>(listeners.size());
        for (Element listenerElement : listeners) {
            managedList.add(parseListener(listenerElement, parserContext));
        }
        consumerAdminDefinition.addPropertyValue("listenerAdaptors", managedList);
    }

    /**
     * 解析listener
     *
     * @param element
     * @return
     */
    private BeanDefinition parseListener(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder consumerContextDefinition = BeanDefinitionBuilder.rootBeanDefinition(ConsumerContext.class);
        NamespaceUtils.setValueIfAttributeDefined(consumerContextDefinition, element, ATTR_LISTENER_SUBJECT, ATTR_LISTENER_SUBJECT);
        NamespaceUtils.setValueIfAttributeDefined(consumerContextDefinition, element, ATTR_LISTENER_GROUP, ATTR_LISTENER_GROUP);
        NamespaceUtils.setValueIfAttributeDefined(consumerContextDefinition, element, "concurrency", "concurrency");
        NamespaceUtils.setValueIfAttributeDefined(consumerContextDefinition, element, "maxConcurrency", "maxConcurrentConsumers");
        NamespaceUtils.setValueIfAttributeDefined(consumerContextDefinition, element, "prefetchCount", "prefetchCount");
        NamespaceUtils.setValueIfAttributeDefined(consumerContextDefinition, element, "requeueRejected", "requeueReject");

        BeanDefinitionBuilder listenerDefinition = BeanDefinitionBuilder.rootBeanDefinition(VMQListenerAdaptor.class);
        listenerDefinition.addPropertyValue("consumerContext", consumerContextDefinition.getBeanDefinition());
        //add listener
        String ref = element.getAttribute(ATTR_LISTENER_REF);
        if (!StringUtils.hasText(ref)) {
            parserContext.getReaderContext().error("Listener 'ref' attribute contains empty value.", element);
        } else {
            listenerDefinition.addPropertyReference("delegate", ref);
        }
        NamespaceUtils.setValueIfAttributeDefined(consumerContextDefinition, element, "methodName", "defaultListenerMethod");
        return listenerDefinition.getBeanDefinition();
    }

}
