package com.test.utils;

import com.test.LogAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class JDBCUtils {
    private static Logger LOGGER = LoggerFactory.getLogger(JDBCUtils.class);
    public static void logSQLException(String msg, SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                LOGGER.error(msg);
                LOGGER.error("SQLState: {}", ((SQLException) e).getSQLState());
                LOGGER.error("Error Code: {}", ((SQLException) e).getErrorCode());
                LOGGER.error("Message: {}", e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    LOGGER.error("Cause: {}", t);
                    t = t.getCause();
                }
            }
        }
    }
}
