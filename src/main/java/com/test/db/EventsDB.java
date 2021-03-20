package com.test.db;

import com.test.model.EventEntity;
import com.test.utils.JDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class EventsDB {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventsDB.class);

    private static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE IF NOT EXISTS events (" +
                    "  event_id VARCHAR(50) PRIMARY KEY," +
                    "  host VARCHAR(20)," +
                    "  type VARCHAR(20)," +
                    "  duration INTEGER NOT NULL," +
                    "  alert BOOLEAN NOT NULL" +
                    "  );";

    private static final String INSERT_EVENTS = "INSERT INTO events" +
            "  (event_id, host, type, duration, alert) VALUES " +
            " (?, ?, ?, ?, ?);";

    public static void initDB() throws SQLException {
        try (Connection connection = HSQLDB.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_EVENTS);
        } catch (SQLException e) {
            JDBCUtils.logSQLException("Failed to create table raw_log_events.", e);
            throw e;
        }
    }

    public static boolean insertBatch(List<EventEntity> entityList) throws SQLException {
        boolean isAllSuccess = false;
        try (Connection connection = HSQLDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EVENTS)) {
            connection.setAutoCommit(false);
            int batchTotal=0;
            for(EventEntity lee : entityList) {
                preparedStatement.setString(1, lee.getEventId());
                preparedStatement.setString(2, lee.getHost());
                preparedStatement.setString(3, lee.getType());
                preparedStatement.setLong(4, lee.getDuration());
                preparedStatement.setBoolean(5, lee.getAlert());
                preparedStatement.addBatch();
                batchTotal++;
            }
            if (batchTotal > 0) {
                int[] result = preparedStatement.executeBatch();
                isAllSuccess = Arrays.stream(result).allMatch(updateCount -> updateCount == 1);
                if(isAllSuccess){
                    connection.commit();
                    LOGGER.info("Batch executed successfully");
                }else{
                    LOGGER.info("Failed to execute batch, not committing to DB.");
                }
            }
        } catch (SQLException e) {
            JDBCUtils.logSQLException("Failed to batch insert events in table log_events.", e);
            throw e;
        }
        return isAllSuccess;
    }

}
