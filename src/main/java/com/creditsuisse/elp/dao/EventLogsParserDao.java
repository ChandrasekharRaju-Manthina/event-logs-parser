package com.creditsuisse.elp.dao;

import com.creditsuisse.elp.domain.EventLog;

public interface EventLogsParserDao {
    void save(EventLog eventLogToSave);
}
