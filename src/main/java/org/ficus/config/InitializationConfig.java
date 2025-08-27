package org.ficus.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class InitializationConfig {

    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = InitializationConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.out.println("Sorry, unable to find " + CONFIG_FILE);
                throw new RuntimeException("Config file not found!");
            }
            PROPERTIES.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Error loading config file!");
        }
    }

    public static long getDayDurationMs() {
        return Long.parseLong(PROPERTIES.getProperty("dayDurationMs"));
    }

    public static long getWorkerWorkDurationMs() {
        return Long.parseLong(PROPERTIES.getProperty("workerWorkDurationMs"));
    }

    public static long getLunchDurationMs() {
        return Long.parseLong(PROPERTIES.getProperty("lunchDurationMs"));
    }

    public static int getBanksCount() {
        return Integer.parseInt(PROPERTIES.getProperty("banksCount"));
    }

    public static int getWorkersCount() {
        return Integer.parseInt(PROPERTIES.getProperty("workersCount"));
    }

    public static int getSpendersCount() {
        return Integer.parseInt(PROPERTIES.getProperty("spendersCount"));
    }

    public static int getInitialBankMoney() {
        return Integer.parseInt(PROPERTIES.getProperty("initialBankMoney"));
    }

    public static int getInitialClientMoney() {
        return Integer.parseInt(PROPERTIES.getProperty("initialClientMoney"));
    }

    public static int getWorkerSalary() {
        return Integer.parseInt(PROPERTIES.getProperty("workerSalary"));
    }

    public static int getWorkerMoneyLimitForBank() {
        return Integer.parseInt(PROPERTIES.getProperty("workerMoneyLimitForBank"));
    }
}
