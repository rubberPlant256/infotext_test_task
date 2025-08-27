package org.ficus.entity;

import org.ficus.config.InitializationConfig;

import java.util.Random;

public class Bank extends Thread {
    private final String name;
    private int money;
    private volatile boolean isOccupied = false;
    private volatile boolean isRunning = true;

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
                // Имитация работы банка
                Thread.sleep(random.nextInt(100) + 1);

                // Банк "очень часто" уходит на обед (60% вероятность)
                if (random.nextInt(100) < 60) {
                    System.out.println(this.name + " уходит на обед.");

                    synchronized (this) {
                        isOccupied = false;
                        notifyAll();
                    }
                    Thread.sleep(InitializationConfig.getLunchDurationMs());
                    System.out.println(this.name + " вернулся с обеда.");
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

    public void shutdown() {
        this.isRunning = false;
        this.interrupt();
    }
}