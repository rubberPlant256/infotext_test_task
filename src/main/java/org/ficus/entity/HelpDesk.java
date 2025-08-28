package org.ficus.entity;

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
        System.out.println("HelpDesk: " + message);
    }
}
