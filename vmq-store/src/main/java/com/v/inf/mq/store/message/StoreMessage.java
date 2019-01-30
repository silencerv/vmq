package com.v.inf.mq.store.message;

import com.v.inf.mq.Message;
import com.v.inf.mq.common.util.JsonUtils;
import com.v.inf.mq.message.BaseMessage;

import java.util.Date;

/**
 * @anthor v
 * Create on 2019/1/8
 */
public class StoreMessage {

    private Message source;

    private long id;

    private String messageId;

    private String subject;

    private String content;

    private Date createTime;

    private Date updateTime;

    public Message getSource() {
        return source;
    }

    public void setSource(Message source) {
        this.source = source;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Message toMessage() {
        return JsonUtils.toBean(content, BaseMessage.class);
    }
}
