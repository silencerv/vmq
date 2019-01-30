package com.v.inf.mq.store.message;

import javax.sql.DataSource;

/**
 * @anthor v
 * Create on 2019/1/14
 */
public class ProducerMessage {

    private DataSource operateDataSource;

    private StoreMessage storeMessage;

    public DataSource getOperateDataSource() {
        return operateDataSource;
    }

    public void setOperateDataSource(DataSource operateDataSource) {
        this.operateDataSource = operateDataSource;
    }

    public StoreMessage getStoreMessage() {
        return storeMessage;
    }

    public void setStoreMessage(StoreMessage storeMessage) {
        this.storeMessage = storeMessage;
    }
}
