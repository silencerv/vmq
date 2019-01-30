package com.v.inf.mq.client.service;

import com.v.inf.mq.common.db.DataSourceUtil;
import com.v.inf.mq.common.sync.Holder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @anthor v
 * Create on 2019/1/14
 */
public class StoreServiceManager {

    private static ConcurrentHashMap<String, Holder<DbMessageStoreService>> STORE_SERVICES = new ConcurrentHashMap<>();

    /**
     * 数据源作为key获取
     *
     * @param dataSource
     * @return
     */
    public static DbMessageStoreService getStoreService(DataSource dataSource) {
        String dataSourceKey = DataSourceUtil.getDataSourceKey(dataSource);
        Holder<DbMessageStoreService> serviceHolder = STORE_SERVICES.get(dataSourceKey);
        if (Objects.isNull(serviceHolder)) {
            STORE_SERVICES.putIfAbsent(dataSourceKey, new Holder<>());
            serviceHolder = STORE_SERVICES.get(dataSourceKey);
        }
        if (Objects.isNull(serviceHolder.get())) {
            synchronized (serviceHolder) {
                if (Objects.isNull(serviceHolder.get())) {
                    serviceHolder.set(new DbMessageStoreService(dataSource));
                }
            }
        }
        return serviceHolder.get();
    }

    /**
     * 自适应获取
     *
     * @return
     */
    public static DbMessageStoreService getAdaptiveStoreService() {
        Optional<Object> dsOptional = TransactionSynchronizationManager.getResourceMap().keySet()
                .stream().filter(key -> key instanceof DataSource)
                .findFirst();
        if (dsOptional.isPresent()) {
            DataSource dataSource = (DataSource) dsOptional.get();
            return getStoreService(dataSource);
        }
        throw new RuntimeException("无法获取当前数据源!");
    }

    /**
     * 默认数据源
     *
     * @param defaultDatasource
     * @return
     */
    public static DbMessageStoreService getStoreServiceWithDefault(DataSource defaultDatasource) {
        Optional<Object> dsOptional = TransactionSynchronizationManager.getResourceMap().keySet()
                .stream().filter(key -> key instanceof DataSource)
                .findFirst();
        if (dsOptional.isPresent()) {
            DataSource dataSource = (DataSource) dsOptional.get();
            return getStoreService(dataSource);
        }
        if (Objects.nonNull(defaultDatasource)) {
            return getStoreService(defaultDatasource);
        }
        throw new RuntimeException("无法获取当前数据源!");
    }

    public static DbMessageStoreService getStoreService(String dataSourceKey) {
        return STORE_SERVICES.get(dataSourceKey).get();
    }
}
