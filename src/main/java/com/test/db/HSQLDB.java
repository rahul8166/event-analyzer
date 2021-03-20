package com.test.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class HSQLDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(HSQLDB.class);
    private static final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
    private static final String JDBC_URL = "jdbc:hsqldb:file:eventsdb;ifexists=true;shutdown=true;";
    private static final String JDBC_USERNAME = "SA";
    private static final String JDBC_PASSWORD = "";
    private static BasicDataSource bds;

    private static BasicDataSource getDatasource() throws SQLException {
        if(bds != null){
            return bds;
        }
        bds = new BasicDataSource();
        bds.setDriverClassName(JDBC_DRIVER);
        bds.setUrl(JDBC_URL);
        bds.setUsername(JDBC_USERNAME);
        bds.setPassword(JDBC_PASSWORD);
        bds.setInitialSize(5);
        bds.setMaxIdle(10);
        bds.setMinIdle(5);
        LOGGER.info("HSQLDB connection pool created successfully.");
        return bds;
    }

    public static Connection getConnection() throws SQLException {
        BasicDataSource ds = getDatasource();
        Connection connection = ds.getConnection();
        return connection;
    }
}





