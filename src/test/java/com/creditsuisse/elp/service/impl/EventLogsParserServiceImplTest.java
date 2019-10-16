package com.creditsuisse.elp.service.impl;

import com.creditsuisse.elp.dao.impl.EventLogsParserDaoImpl;
import com.creditsuisse.elp.domain.EventLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventLogsParserServiceImplTest {

    @Mock
    private EventLogsParserDaoImpl eventLogsParserDao;

    private EventLogsParserServiceImpl eventLogsParserService;

    @Before
    public void before() {
        ObjectMapper objectMapper = new ObjectMapper();
        eventLogsParserService = new EventLogsParserServiceImpl(eventLogsParserDao, objectMapper);
    }

    @Test
    public void whenEventLogsAreValidThenSaveThem() {
        File file = new File(
                getClass().getClassLoader().getResource("event-logs.txt").getFile()
        );
        eventLogsParserService.parse(file.getAbsolutePath());

        // 3 event logs present in class so it will call dao method 3 times
        verify(eventLogsParserDao, times(3)).save(any(EventLog.class));
    }

    @Test
    public void whenThereIsNoCorrespondingStartOrFinishEventThenDoNotSaveIt() {
        File file = new File(
                getClass().getClassLoader().getResource("event-logs-one-record-without-corresponding-event.txt").getFile()
        );
        eventLogsParserService.parse(file.getAbsolutePath());

        // only 2 event logs has corresponding START/FINISHED event so save will be called 2 times
        verify(eventLogsParserDao, times(2)).save(any(EventLog.class));
    }

    @Test
    public void whenTheEventLogFormatIsInValidThenSkipItAndProcessWithRemainingLogs() {
        File file = new File(
                getClass().getClassLoader().getResource("event-logs-one-record-with-invalid-formatting.txt").getFile()
        );
        eventLogsParserService.parse(file.getAbsolutePath());

        // only 2 event logs has proper formatting so save will be called 2 times
        verify(eventLogsParserDao, times(2)).save(any(EventLog.class));
    }

    @Test(expected = RuntimeException.class)
    public void whenFilePathIsNotValidThenThrowException() {
        eventLogsParserService.parse("dummyFilePath");
    }


}