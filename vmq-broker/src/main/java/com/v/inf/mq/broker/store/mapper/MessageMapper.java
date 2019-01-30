package com.v.inf.mq.broker.store.mapper;

import com.v.inf.mq.store.message.BrokerMessage;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface MessageMapper {

    int insert(BrokerMessage message);

    int insertSelective(BrokerMessage message);

    int deleteById(long id);

    int deleteByMessageId(String messageId);

    List<BrokerMessage> queryReady(@Param("startTime") Date startTime, @Param("limit") int limit);
}