package com.v.inf.mq;

import java.util.Date;
import java.util.Map;

public interface Message {

    /**
     * 获取message id，一般是自动生成的，com.v.inf.mq.store.message id需要保证全局唯一
     *
     * @return com.v.inf.mq.store.message id
     */
    String getMessageId();

    /**
     * 消息主题
     *
     * @return subject
     */
    String getSubject();

    /**
     * @return
     */
    String getRoutingKey();

    /**
     * 消息属性
     * 返回不可变集合
     *
     * @return
     */
    Map<String, Object> getAttrs();

    void setAttr(String name, boolean value);

    void setAttr(String name, int value);

    void setAttr(String name, long value);

    void setAttr(String name, float value);

    void setAttr(String name, double value);

    void setAttr(String name, Date date);

    void setAttr(String name, String value);

    String getStringAttr(String name);

    boolean getBooleanAttr(String name);

    Date getDateAttr(String name);

    int getIntAttr(String name);

    long getLongAttr(String name);

    float getFloatAttr(String name);

    double getDoubleAttr(String name);

    /**
     * 创建时间，消息产生的时间，不是消息发送的时间
     *
     * @return
     */
    long getCreateTime();

    /**
     * 延迟消息
     * 单位:ms
     *
     * @return
     */
    int getDelayMills();

    /**
     * 设置延迟时间，默认为0
     * 单位:ms
     *
     * @param delayMills
     */
    void setDelayMills(int delayMills);

    /**
     * 第几次发送
     *
     * @return
     */
    int getTimes();
}
