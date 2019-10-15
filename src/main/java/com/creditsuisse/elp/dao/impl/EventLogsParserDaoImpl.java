package com.creditsuisse.elp.dao.impl;

import com.creditsuisse.elp.dao.EventLogsParserDao;
import com.creditsuisse.elp.domain.EventLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

public class EventLogsParserDaoImpl implements EventLogsParserDao {

    private static final Logger logger = LoggerFactory.getLogger(EventLogsParserDaoImpl.class);

    private final JdbcTemplate jdbcTemplate;

    public EventLogsParserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(EventLog eventLogToSave) {
        logger.debug("Save started");
        jdbcTemplate.update(
                "INSERT INTO eventlog(id, duration, type, host, iseventslow) VALUES(?,?,?,?,?)",
                eventLogToSave.getId(), eventLogToSave.getDuration(), eventLogToSave.getType(),
                eventLogToSave.getHost(), eventLogToSave.isEventSlow()
        );
        logger.debug("Save finished");
    }

}
