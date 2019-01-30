package com.v.inf.mq.client.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * {@link EnableVMQ}
 *
 * @author v
 */
class WMQAnnotationConfigurationSelector implements ImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{MessageConsumerConfiguration.class.getName()};
    }
}