package com.creditsuisse.elp.main;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class EventLogsParserMainTest {

    @Test
    public void whenFileIsValidThenNoExceptionShouldBeThrown() {
        File file = new File(
                getClass().getClassLoader().getResource("event-logs.txt").getFile()
        );
        
        EventLogsParserMain.main(new String[]{file.getAbsolutePath()});
    }
}