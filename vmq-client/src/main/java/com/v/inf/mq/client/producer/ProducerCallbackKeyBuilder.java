package com.v.inf.mq.client.producer;

import com.google.common.base.Preconditions;
import com.v.inf.mq.Message;
import com.v.inf.mq.constants.Constants;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class ProducerCallbackKeyBuilder implements Function<Message, String> {

    @Override
    public String apply(Message message) {
        String messageId = message.getMessageId();
        String result = messageId;
        String dbKey = message.getStringAttr(Constants.DB_KEY);
        // 强校验
        Preconditions.checkArgument(StringUtils.isNotBlank(dbKey));
        if (StringUtils.isNotBlank(dbKey)) {
            result = result.concat("$" + dbKey);

        }
        return result;
    }
}