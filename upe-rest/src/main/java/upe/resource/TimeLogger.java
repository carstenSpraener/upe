package upe.resource;

import java.util.logging.Logger;

public class TimeLogger implements AutoCloseable {
    private static Logger LOGGER = Logger.getLogger(TimeLogger.class.getName());

    private String description;
    private long timeStartMillis;

    public TimeLogger(String description) {
        this.description = description;
        this.timeStartMillis = System.currentTimeMillis();
    }

    @Override
    public void close() throws Exception {
        long timeEndMillis = System.currentTimeMillis();
        LOGGER.info(String.format("Operation '%s' took %d ms.%n", description, timeEndMillis- timeStartMillis));
    }
}
