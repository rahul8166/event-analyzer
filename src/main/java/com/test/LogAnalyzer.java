package com.test;

import com.test.db.EventsDB;
import com.test.utils.JDBCUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.test.processor.BatchProcessor.batchProcessor;

public class LogAnalyzer {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogAnalyzer.class);

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1) {
            LOGGER.info("Please provide the path of logfile.txt");
            LOGGER.info("e.g. java com.test.LogAnalyzer /apps/logs");
        } else {
            String path = args[0];
            try (FileInputStream fis = new FileInputStream(path + File.separator + "logfile.txt");
                 Scanner scanner = new Scanner(fis)) {
                LOGGER.info("DB Initializing...");
                EventsDB.initDB();
                LOGGER.info("Processing events : START");
                long start = System.nanoTime();
                while (scanner.hasNextLine()) {
                    long batchStart = System.nanoTime();
                    Boolean batchResult = batchProcessor().readBatchAndProcess(scanner);
                    long batchEnd = System.nanoTime();
                    LOGGER.info("Batch completed in {}ms, IsSuccessful : {}",
                            TimeUnit.MILLISECONDS.convert(batchEnd-batchStart, TimeUnit.NANOSECONDS)
                            , batchResult);
                }
                if (scanner.ioException() != null) {
                    throw scanner.ioException();//throw suppressed exception
                }
                long end = System.nanoTime();
                LOGGER.info("Processing events : END : completed in {}ms", TimeUnit.MILLISECONDS.convert(end-start, TimeUnit.NANOSECONDS));
            } catch (FileNotFoundException e) {
                LOGGER.error("logfile.txt is not found on this path {}", path);
            } catch (IOException e) {
                LOGGER.error("Failed to read logfile.txt with path {}", path, e);
            } catch (SQLException e) {
                JDBCUtils.logSQLException("Error while executing a DB action.", e);
            }
            Thread.sleep(3000);
        }
    }
}
