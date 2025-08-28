package org.ficus.util;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class LoggerUtil {
    public static final Logger LOGGER = Logger.getLogger(LoggerUtil.class.getName());

    static {
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {
            @Override
            public String format(LogRecord record) {
                return record.getMessage() + "\n";
            }
        });
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(handler);
    }
}
