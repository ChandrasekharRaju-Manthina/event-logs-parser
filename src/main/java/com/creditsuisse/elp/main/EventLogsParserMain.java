package com.creditsuisse.elp.main;

import ch.qos.logback.classic.Level;
import com.creditsuisse.elp.dao.EventLogsParserDao;
import com.creditsuisse.elp.dao.impl.EventLogsParserDaoImpl;
import com.creditsuisse.elp.service.EventLogsParserService;
import com.creditsuisse.elp.service.impl.EventLogsParserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.nio.file.Paths;

public class EventLogsParserMain {

    private static final Logger logger = LoggerFactory.getLogger(EventLogsParserMain.class);

    private final JdbcTemplate jdbcTemplate;
    private final EventLogsParserService eventLogsParserService;

    public EventLogsParserMain() {
        // objects construction - we can use IOC/Dependency injection such as Spring container here!
        this.jdbcTemplate = getJdbcTemplate();
        EventLogsParserDao eventLogsParserDao = new EventLogsParserDaoImpl(jdbcTemplate);

        ObjectMapper objectMapper = new ObjectMapper();
        this.eventLogsParserService = new EventLogsParserServiceImpl(eventLogsParserDao, objectMapper);
    }

    public static void main(String[] args) {
        logger.info("Main method started");

        String filePath = "C:\\git\\event-logs-parser\\src\\main\\java\\com\\creditsuisse\\elp\\main\\event-logs.txt";
        new EventLogsParserMain().run(filePath);

        logger.info("Main method finished");
    }

    private void run(String filePath) {
        // TODO create a log configuration file
        setLoggingLevel(Level.INFO);

        //TODO move it to a sql script
        createTableIfNotExist(jdbcTemplate);

        eventLogsParserService.parse(filePath);
    }

    private static JdbcTemplate getJdbcTemplate() {
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();

        // set hsqldb file path to where application was initialized.
        String currentFolder = Paths.get("").toAbsolutePath().toString() + "\\db\\";
        logger.info("Database location {}", currentFolder);

        driverManagerDataSource.setUrl("jdbc:hsqldb:file:" + currentFolder);

        driverManagerDataSource.setUsername("sa");
        driverManagerDataSource.setPassword("");
        driverManagerDataSource.setDriverClassName("org.hsqldb.jdbcDriver");

        return new JdbcTemplate(driverManagerDataSource);
    }

    private static void setLoggingLevel(Level level) {
        ch.qos.logback.classic.Logger root =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
        root.setLevel(level);
    }

    private static void createTableIfNotExist(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS eventlog" +
                "(id VARCHAR(100)," +
                "duration NUMERIC," +
                "type VARCHAR(100)," +
                "host VARCHAR(100)," +
                "iseventslow VARCHAR(5))");
    }

}
