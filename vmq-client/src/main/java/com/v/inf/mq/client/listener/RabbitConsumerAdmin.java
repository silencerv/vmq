package com.v.inf.mq.client.listener;

import com.v.inf.mq.client.admin.RabbitAdminService;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class RabbitConsumerAdmin implements InitializingBean {

    private List<VMQListenerAdaptor> listenerAdaptors = new ArrayList<>();

    public List<VMQListenerAdaptor> getListenerAdaptors() {
        return listenerAdaptors;
    }

    public void setListenerAdaptors(List<VMQListenerAdaptor> listenerAdaptors) {
        this.listenerAdaptors = listenerAdaptors;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //init listener
        initListener();
    }

    private void initListener() {
        RabbitAdminService rabbitAdminService = ExtensionLoader.getExtensionLoader(RabbitAdminService.class).getDefaultExtension();
        listenerAdaptors.forEach(rabbitAdminService::addListener);
    }


}
