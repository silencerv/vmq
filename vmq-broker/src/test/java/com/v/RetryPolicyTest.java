package com.v;

import com.v.inf.mq.broker.retry.PowerIncrementRetry;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.Test;

import java.util.Date;

/**
 * @anthor v
 * Create on 2019/1/17
 */
public class RetryPolicyTest {

    @Test
    public void testPowerRetry() {
        PowerIncrementRetry powerIncrementRetry = new PowerIncrementRetry(3);
        Date nextRetry = new Date();
        for (int count = 1; count < 11; count++) {
            nextRetry = powerIncrementRetry.nextRetry(nextRetry, count);
            System.out.println(DateFormatUtils.format(nextRetry, "yyyy-MM-dd hh:mm:ss:SS") + "              " + count);
        }
    }
}
