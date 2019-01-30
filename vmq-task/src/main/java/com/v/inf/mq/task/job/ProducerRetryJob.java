package com.v.inf.mq.task.job;

import com.v.inf.mq.task.executor.ProducerRetryExecutor;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @anthor v
 * Create on 2019/1/28
 */
@Component
public class ProducerRetryJob extends QuartzJobBean {

    @Autowired
    private ProducerRetryExecutor producerRetryExecutor;

    @Bean
    public JobDetail producerRetryJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(this.getClass());
        jobDetailFactoryBean.setName("producer_retry_job");
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.afterPropertiesSet();
        return jobDetailFactoryBean.getObject();
    }

    @Bean
    public CronTriggerFactoryBean producerRetryTrigger(@Qualifier("producerRetryJobDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(jobDetail);
        triggerFactoryBean.setStartDelay(1000);
        triggerFactoryBean.setName("producer_retry_trigger");
        triggerFactoryBean.setCronExpression("*/1 * * * * ?");
        return triggerFactoryBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        producerRetryExecutor.execute();
    }
}
