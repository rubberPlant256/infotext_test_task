package org.ficus.entity;

import org.ficus.config.InitializationConfig;

import java.util.Random;

public class Bank extends Thread {

    private final String name;
    private int money;
    private volatile boolean isOccupied = false;
    private volatile boolean isRunning = true;

    HelpDesk helpDesk = HelpDesk.getInstance();

    public Bank(int id) {
        this.name = "Bank-" + id;
        this.money = InitializationConfig.getInitialBankMoney();
    }

    public synchronized void occupy() throws InterruptedException {
        while (isOccupied) {
            wait();
        }
        this.isOccupied = true;
    }

    public synchronized void release() {
        this.isOccupied = false;
        notifyAll();
    }

    public synchronized void addMoney(int amount) {
        this.money += amount;
    }

    public synchronized void takeMoney(int amount) {
        this.money -= amount;
    }

    public String getBankName() {
        return name;
    }

    public synchronized int getMoney() {
        return money;
    }

    @Override
    public void run() {
        Random random = new Random();
        while (isRunning) {
            try {
                Thread.sleep(random.nextInt(100) + 1);

                if (random.nextInt(100) < 60) {
                    goOnLunchBreak();
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

    private void goOnLunchBreak() throws InterruptedException {
        helpDesk.getHelp(this.name + " уходит на обед.");
        synchronized (this) {
            isOccupied = false;
            notifyAll();
        }
        Thread.sleep(InitializationConfig.getLunchDurationMs());
        helpDesk.getHelp(this.name + " вернулся с обеда.");
    }

    public void shutdown() {
        this.isRunning = false;
        this.interrupt();
    }
}