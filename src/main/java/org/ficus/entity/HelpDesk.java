package org.ficus.entity;

import org.ficus.util.LoggerUtil;

public class HelpDesk {

    private static volatile HelpDesk instance;

    private HelpDesk() {
    }

    public static HelpDesk getInstance() {
        if (instance == null) {
            synchronized (HelpDesk.class) {
                if (instance == null) {
                    instance = new HelpDesk();
                }
            }
        }
        return instance;
    }

    public synchronized void getHelp(String message) {
        LoggerUtil.LOGGER.info("HelpDesk: " + message);
    }
}
