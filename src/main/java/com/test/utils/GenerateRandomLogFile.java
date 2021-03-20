package com.test.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class GenerateRandomLogFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenerateRandomLogFile.class);

    public static void main(String[] args) throws IOException {
        Random rnd = new Random();
        List<String> states = Arrays.asList("STARTED", "FINISHED");
        List<String> types = Arrays.asList("", "\"type\"=\"APPLICATION_LOG\",");

        Path path = Paths.get("/apps/logs/logfile.txt");
        Files.createDirectories(path.getParent());
        try {
            Files.createFile(path);
        } catch (FileAlreadyExistsException e) {
            LOGGER.info("logfile.txt file already exists, deleting it and creating new...");
            Files.deleteIfExists(path);
            Files.createFile(path);
        }

        FileWriter fileWriter = new FileWriter(new File("/apps/logs/logfile.txt"), true);
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.print("");
        for (int i = 0; i < 100000; i++) {
            String event = UUID.randomUUID().toString();
            String type = types.get(rnd.nextInt(types.size()));
            long hostId = rnd.nextInt(100000);
            long timestamp = ZonedDateTime.now().toInstant().toEpochMilli() + rnd.nextInt(8);
            printWriter.println(String.format("{ \"id\"=\"%s\", \"state\"=\"STARTED\", %s \"host\":\"%d\", \"timestamp\": %d }",
                    event, type, hostId, timestamp));
            printWriter.println(String.format("{ \"id\"=\"%s\", \"state\"=\"FINISHED\", %s \"host\":\"%d\", \"timestamp\": %d }",
                    event, type, hostId, timestamp + rnd.nextInt(10)));
        }
        printWriter.close();
        LOGGER.info("logfile.txt created at path {}", path.toAbsolutePath());
    }
}
