package com.v.inf.mq.common.db;

import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.net.URI;
import java.sql.Connection;

/**
 * @anthor v
 * Create on 2019/1/14
 */
public class DataSourceUtil {

    public static String getDataSourceKey(DataSource dataSource) {
        Connection conn = null;
        try {
            conn = DataSourceUtils.getConnection(dataSource);
            String jdbcUrl = conn.getMetaData().getURL();
            URI uri = new URI(jdbcUrl.replaceAll("^\\s*jdbc:", ""));
            return String.format("%s:%s", uri.getHost(), uri.getPort());
        } catch (Exception e) {
            throw new RuntimeException("can't get db info key ", e);
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }
}
