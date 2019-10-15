package com.creditsuisse.elp.dao.impl;

import com.creditsuisse.elp.domain.EventLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class EventLogsParserDaoImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private EventLog eventLog;

    @InjectMocks
    private EventLogsParserDaoImpl eventLogsParserDao;

    @Test
    public void whenEventLogSavedThenInsertItInDatabase() {
        eventLogsParserDao.save(eventLog);

        verify(eventLog, times(1)).isEventSlow();
    }


}