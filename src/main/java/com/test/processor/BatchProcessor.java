package com.test.processor;

import com.google.gson.Gson;
import com.test.db.EventsDB;
import com.test.model.RawEvent;
import com.test.model.EventEntity;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class BatchProcessor {
    private static BatchProcessor processor;
    private final Gson gson = new Gson();
    private Map<String, RawEvent> eventsMap = new HashMap<>();
    private static final int BATCH_SIZE = 2;

    private BatchProcessor(){}

    public static BatchProcessor batchProcessor(){
        if(processor == null){
            processor = new BatchProcessor();
        }
        return processor;
    }

    public boolean readBatchAndProcess(Scanner scanner) throws IOException {
        List<RawEvent> rawEventsList = readBatch(scanner);
        return processBatch(rawEventsList);
    }

    private List<RawEvent> readBatch(Scanner sc) {
        List<RawEvent> result = new ArrayList<>();
        for (int i = 1; i <= BATCH_SIZE; i++) {
            try{
                String line = sc.nextLine();
                if (line != null) {
                    result.add(gson.fromJson(line, RawEvent.class));
                } else {
                    return result;
                }
            }catch (NoSuchElementException nsee){ }
        }
        return result;
    }

    public boolean processBatch(List<RawEvent> batch) {
        List<EventEntity> eventEntityList = new ArrayList<>();
        for (RawEvent currentEvent : batch) {
            if (eventsMap.get(currentEvent.getId()) != null) {
                RawEvent earlierEvent = eventsMap.get(currentEvent.getId());
                long duration = calculateDuration(earlierEvent, currentEvent);
                eventEntityList.add(new EventEntity(currentEvent.getId(),
                        currentEvent.getType(),
                        currentEvent.getHost(),
                        duration,
                        duration > 4));
            }
            eventsMap.put(currentEvent.getId(), currentEvent);
        }
        return processDBBatch(eventEntityList);
    }

    private long calculateDuration(RawEvent logEvent1, RawEvent logEvent2) {
        return logEvent1.getTimestamp() > logEvent2.getTimestamp() ? logEvent1.getTimestamp() - logEvent2.getTimestamp() :
                logEvent2.getTimestamp() - logEvent1.getTimestamp();
    }

    private boolean processDBBatch(List<EventEntity> eventEntityList) {
        try {
            return EventsDB.insertBatch(eventEntityList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
