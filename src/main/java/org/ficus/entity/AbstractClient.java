package org.ficus.entity;

import java.util.List;

public abstract class AbstractClient extends Thread {

    protected int money;
    protected volatile boolean isRunning = true;
    protected final List<Bank> banks;

    public AbstractClient(String name, int initialMoney, List<Bank> banks) {
        super(name);
        this.money = initialMoney;
        this.banks = banks;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int amount) {
        this.money = amount;
    }

    public void shutdown() {
        this.isRunning = false;
        this.interrupt();
    }
}
