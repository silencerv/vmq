package com.v.inf.mq.task;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * @anthor v
 * Create on 2019/1/28
 */
@Component
public class DataSourceConfiguration {

    @Bean("primaryDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource primaryDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @QuartzDataSource
    @ConfigurationProperties(prefix = "quartz.datasource")
    public DataSource secondaryDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    /**
     * 需要扫描的producer 数据库
     * @param dataSource
     * @return
     */
    @Bean("producerDataSources")
    public List<DataSource> producerDataSources(@Qualifier("primaryDataSource") DataSource dataSource) {
        return Lists.newArrayList(dataSource);
    }
}
