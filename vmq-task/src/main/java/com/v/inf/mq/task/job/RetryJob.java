package com.v.inf.mq.task.job;

import com.v.inf.mq.task.executor.RetryExecutor;
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
public class RetryJob extends QuartzJobBean {

    @Autowired
    private RetryExecutor retryExecutor;

    @Bean(name = "retryJobDetail")
    public JobDetailFactoryBean jobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(this.getClass());
        jobDetailFactoryBean.setName("retry_job");
        jobDetailFactoryBean.setDurability(true);
        return jobDetailFactoryBean;
    }

    @Bean
    public CronTriggerFactoryBean trigger(@Qualifier("retryJobDetail") JobDetail jobDetail) {
        CronTriggerFactoryBean triggerFactoryBean = new CronTriggerFactoryBean();
        triggerFactoryBean.setJobDetail(jobDetail);
        triggerFactoryBean.setStartDelay(1000);
        triggerFactoryBean.setName("retryTrigger");
        triggerFactoryBean.setCronExpression("*/5 * * * * ?");
        return triggerFactoryBean;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        retryExecutor.execute();
    }
}
