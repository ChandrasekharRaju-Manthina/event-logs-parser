package com.creditsuisse.elp.service.impl;

import com.creditsuisse.elp.dao.EventLogsParserDao;
import com.creditsuisse.elp.domain.EventLog;
import com.creditsuisse.elp.service.EventLogsParserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventLogsParserServiceImpl implements EventLogsParserService {

    private static final Logger logger = LoggerFactory.getLogger(EventLogsParserServiceImpl.class);

    private final EventLogsParserDao eventLogsParserDao;

    private final ObjectMapper objectMapper;

    public EventLogsParserServiceImpl(EventLogsParserDao eventLogsParserDao, ObjectMapper objectMapper) {
        this.eventLogsParserDao = eventLogsParserDao;
        this.objectMapper = objectMapper;
    }

    @Override
    public void parse(String filePath) {
        logger.info("Event logs file parsing started");

        Instant start = Instant.now();

        try {
            ArrayList<String> logsFailedToProcess = new ArrayList<>();
            Map<String, EventLog> eventLogsMap = new HashMap<>();

            Files.lines(Paths.get(filePath)).forEach(line -> {
                try {
                    processEventLog(eventLogsMap, line);
                } catch (Exception e) {
                    logsFailedToProcess.add(line);
                    logger.warn("Record failed to process", e);
                }
            });

            checkIfAnyRecordsExistsInMap(eventLogsMap);

            logStatus(true, logsFailedToProcess);
        } catch (Exception e) {
            logStatus(false, null);
            throw new RuntimeException("Exception occurred when parsing the file", e);
        }

        Instant end = Instant.now();
        logger.info("Time taken to complete parsing: {}", Duration.between(start, end));

        logger.info("Event logs file parsing finished");
    }

    private void checkIfAnyRecordsExistsInMap(Map<String, EventLog> eventLogsMap) {
        if (!eventLogsMap.isEmpty()) {
            logger.error("Below event logs do NOT have corresponding START/FINISHED entry");
            eventLogsMap.values().forEach(System.out::println);
        }
    }

    private void processEventLog(Map<String, EventLog> eventLogs, String line) throws JsonProcessingException {
        EventLog eventLog = objectMapper.readValue(line, EventLog.class);

        // if the corresponding event exist in map then remove from map and save it
        // else add it to the map
        if (eventLogs.containsKey(eventLog.getId())) {
            EventLog eventLogToSave = eventLogs.remove(eventLog.getId());

            long duration = Math.abs(eventLogToSave.getTimestamp() - eventLog.getTimestamp());
            eventLogToSave.setDuration(duration);

            eventLogsParserDao.save(eventLogToSave);
        } else {
            eventLogs.put(eventLog.getId(), eventLog);
        }

    }

    private void logStatus(boolean jobStatus, List<String> logsFailedToProcess) {
        if (!jobStatus) {
            logger.warn("Please check logs. File parsing failed!! ");
            return;
        }

        if (logsFailedToProcess.isEmpty()) {
            logger.info("File parsing completed successfully without any errors!");
        } else {
            logger.warn("File parsing completed but {} lines are failed to process", logsFailedToProcess.size());
            logger.warn("-----------------------------------------------------------");
            logsFailedToProcess.forEach(System.out::println);
            logger.warn("-----------------------------------------------------------");
        }
    }

}
