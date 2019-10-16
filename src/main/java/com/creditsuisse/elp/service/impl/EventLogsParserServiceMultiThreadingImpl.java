package com.creditsuisse.elp.service.impl;

import com.creditsuisse.elp.service.EventLogsParserService;

public class EventLogsParserServiceMultiThreadingImpl implements EventLogsParserService {

    @Override
    public void parse(String filePath) {
        // Approach 1
        // Read a fixed number of lines from the file and pass it to worker thread pool (use executor framework?)
        // worker thread pool uses a ConcurrentHashMap ans same logic which other impl used
        // Repeat step 1 till the end of the file

        // Approach 2
        // Have multiple threads read the file simultaneously and process the records

        throw new UnsupportedOperationException("Not yet implemented. Hire me to implement it...!! :)");
    }

}
