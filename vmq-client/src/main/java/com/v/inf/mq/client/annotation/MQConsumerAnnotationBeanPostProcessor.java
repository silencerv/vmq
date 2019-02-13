package com.v.inf.mq.client.annotation;

import com.v.inf.mq.Message;
import com.v.inf.mq.client.admin.RabbitAdminService;
import com.v.inf.mq.client.listener.ConsumerContext;
import com.v.inf.mq.client.listener.VMQListenerAdaptor;
import com.v.inf.mq.rabbit.converter.JSONMessageConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * @anthor v
 * Create on 2019/1/15
 */
public class MQConsumerAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered, BeanFactoryAware {

    private static Logger logger = LoggerFactory.getLogger(MQConsumerAnnotationBeanPostProcessor.class);

    private RabbitAdminService rabbitAdminService;

    private BeanFactory beanFactory;

    private int order;

    public MQConsumerAnnotationBeanPostProcessor() {
        this.rabbitAdminService = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        scanMethods(bean, bean.getClass());
        return bean;
    }

    private void scanMethods(Object bean, Class<?> clazz) {
        if (clazz == null) {
            return;
        }
        for (Method method : clazz.getDeclaredMethods()) {
            try {
                MqConsumer[] wmqConsumers = method.getAnnotationsByType(MqConsumer.class);
                if (wmqConsumers == null || wmqConsumers.length == 0) {
                    continue;
                }

                for (MqConsumer annotation : wmqConsumers) {
                    String subjectsStr = annotation.subjects();
                    String[] subjects = subjectsStr.split(",");
                    if (ArrayUtils.isEmpty(subjects)) {
                        throw new RuntimeException("subjects必须指定. bean: " + bean + ", method: " + method);
                    }
                    for (String subject : subjects) {
                        addConsumer(subject, bean, method, annotation);
                    }
                }
            } catch (Throwable e) {
                throw new RuntimeException("初始化@MQConsumer失败. bean: " + bean + ", method: " + method, e);
            }
        }
    }

    private void addConsumer(String subject, Object bean, Method method, MqConsumer annotation) {
        subject = resolveEmbeddedValue(subject);
        String group = annotation.group();
        if (StringUtils.hasText(group)) {
            group = resolveEmbeddedValue(group);
        }
        logger.info("发现注解消费者:bean: " + bean + ", method: " + method);
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1 || !parameterTypes[0].equals(Message.class)) {
            throw new RuntimeException("@MQConsumer仅能应用于有且仅有一个参数类型为Message的方法！ bean: " + bean + ", method: " + method);
        }

        Class<?> parameterType = parameterTypes[0];
        if (parameterType.equals(Message.class)) {
            ConsumerContext consumerContext = new ConsumerContext();
            consumerContext.setSubject(subject);
            consumerContext.setGroup(group);
            consumerContext.setConcurrency(annotation.concurrency());
            consumerContext.setMaxConcurrentConsumers(annotation.maxConcurrentConsumers());
            consumerContext.setPrefetchCount(annotation.prefetchCount());
            consumerContext.setRequeueReject(annotation.requeueReject());
            VMQListenerAdaptor listenerAdapter = new VMQListenerAdaptor();
            listenerAdapter.setConsumerContext(consumerContext);
            listenerAdapter.setMessageConverter(new JSONMessageConverter());
            listenerAdapter.setDelegate(bean);
            listenerAdapter.setDefaultListenerMethod(method.getName());
            rabbitAdminService.addListener(listenerAdapter);
            return;
        }
    }

    /**
     * 解析通配符字符串
     *
     * @param value
     * @return
     */
    private String resolveEmbeddedValue(String value) {
        if (this.beanFactory != null && this.beanFactory instanceof ConfigurableBeanFactory) {
            return ((ConfigurableBeanFactory) this.beanFactory).resolveEmbeddedValue(value);
        }
        return value;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
