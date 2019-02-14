package com.v.inf.mq.task.job;

import com.v.inf.mq.task.executor.BrokerRetryExecutor;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
public class RetryJob extends QuartzJobBean {

    @Autowired
    private BrokerRetryExecutor brokerRetryExecutor;

    @Bean
    public JobDetail brokerRetryJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(this.getClass());
        jobDetailFactoryBean.setName("broker_retry_job");
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.afterPropertiesSet();
        return jobDetailFactoryBean.getObject();
    }

    @Bean
    public CronTriggerFactoryBean brokerRetryTrigger(@Qualifier("brokerRetryJobDetail") JobDetail jobDetail,
                                                     @Value("${vmq.task.corn.broker.retry}") String brokerRetryCorn) {
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(jobDetail);
        triggerFactoryBean.setStartDelay(1000);
        triggerFactoryBean.setName("broker_retry_trigger");
        triggerFactoryBean.setCronExpression(brokerRetryCorn);
        return triggerFactoryBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        brokerRetryExecutor.execute();
    }
}
