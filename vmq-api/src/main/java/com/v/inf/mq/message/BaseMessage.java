package com.v.inf.mq.message;

import com.google.common.base.Preconditions;
import com.v.inf.mq.Message;
import com.v.inf.mq.constants.Constants;

import java.util.*;

/**
 * @anthor v
 * Create on 2019/1/11
 */
public class BaseMessage implements Message {

    /**
     * 比如 trace id
     */
    protected static Set<String> protectedKeyNames;

    static {
        protectedKeyNames = new HashSet<>();
        protectedKeyNames.add(Constants.DB_KEY);
    }

    private String messageId;

    private String subject;

    private String routingKey = "";

    private Map<String, Object> attrs = new HashMap<String, Object>();

    private long createTime;

    private int delayMills;

    private int times;

    @Override
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    @Override
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    @Override
    public Map<String, Object> getAttrs() {
        return Collections.unmodifiableMap(attrs);
    }

    public void setAttrs(Map<String, Object> attrs) {
        Preconditions.checkNotNull(attrs);
        this.attrs.entrySet().stream()
                .filter(entry -> protectedKeyNames.contains(entry.getKey()))
                .forEach((entry) -> attrs.put(entry.getKey(), entry.getValue()));
        this.attrs = attrs;
    }

    private void setObjectAttr(String name, Object value) {
        if (protectedKeyNames.contains(name))
            throw new IllegalArgumentException("property name [" + name + "] is protected !");
        attrs.put(name, value);
    }

    @Override
    public void setAttr(String name, boolean value) {
        setObjectAttr(name, value);
    }

    @Override
    public void setAttr(String name, int value) {
        setObjectAttr(name, value);
    }

    @Override
    public void setAttr(String name, long value) {
        setObjectAttr(name, value);
    }

    @Override
    public void setAttr(String name, float value) {
        setObjectAttr(name, value);
    }


    @Override
    public void setAttr(String name, double value) {
        setObjectAttr(name, value);
    }

    @Override
    public void setAttr(String name, Date value) {
        if (value == null) return;
        setObjectAttr(name, value.getTime());
    }

    @Override
    public void setAttr(String name, String value) {
        if (value == null) return;
        setObjectAttr(name, value);
    }

    @Override
    public String getStringAttr(String name) {
        return String.valueOf(attrs.get(name));
    }

    @Override
    public boolean getBooleanAttr(String name) {
        Object v = attrs.get(name);
        if (Objects.isNull(v))
            return false;
        return Boolean.valueOf(v.toString());
    }

    @Override
    public Date getDateAttr(String name) {
        Object o = attrs.get(name);
        if (Objects.isNull(o))
            return null;
        Long v = Long.valueOf(o.toString());
        return new Date(v);
    }

    @Override
    public int getIntAttr(String name) {
        Object o = attrs.get(name);
        if (Objects.isNull(o))
            return 0;
        return Integer.valueOf(o.toString());
    }

    @Override
    public long getLongAttr(String name) {
        Object o = attrs.get(name);
        if (Objects.isNull(o))
            return 0;
        return Long.valueOf(o.toString());
    }

    @Override
    public float getFloatAttr(String name) {
        Object o = attrs.get(name);
        if (Objects.isNull(o))
            return 0;
        return Float.valueOf(o.toString());
    }

    @Override
    public double getDoubleAttr(String name) {
        Object o = attrs.get(name);
        if (Objects.isNull(o))
            return 0;
        return Double.valueOf(o.toString());
    }

    @Override
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public int getDelayMills() {
        return delayMills;
    }

    @Override
    public void setDelayMills(int delayMills) {
        this.delayMills = delayMills;
    }

    @Override
    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
