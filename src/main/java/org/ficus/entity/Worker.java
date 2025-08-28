package org.ficus.entity;

import org.ficus.config.InitializationConfig;
import org.ficus.service.RandomService;
import org.ficus.util.LoggerUtil;

import java.util.List;
import java.util.Random;

public class Worker extends AbstractClient {

    private volatile boolean isOccupied = false;

    private final Random random = new Random();
    HelpDesk helpDesk = HelpDesk.getInstance();

    public Worker(int id, List<Bank> banks) {
        super("Worker-" + id,
                InitializationConfig.getInitialClientMoney(),
                banks);
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

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (money > InitializationConfig.getWorkerMoneyLimitForBank()) {
                    depositMoneyToBank();
                }

                Thread.sleep(100 + random.nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

    private void depositMoneyToBank() throws InterruptedException {
        helpDesk.getHelp(super.getName() + " накопил " + money + "$ и решил отнести их в банк.");

        Bank bank = RandomService.getRandomEntity(banks);

        bank.occupy();
        helpDesk.getHelp(super.getName() + " начал вносить деньги в " + bank.getBankName());

        int moneyToDeposit = this.money;
        bank.addMoney(moneyToDeposit);
        this.setMoney(0);

        Thread.sleep(InitializationConfig.getWorkerWorkDurationMs());

        bank.release();
        helpDesk.getHelp(super.getName() + " завершил операцию в " + bank.getBankName()
                + ". Баланс: " + this.money + "$");
    }

    public synchronized void earnMoney(int salary) {
        this.money += salary;
        helpDesk.getHelp(super.getName() + " заработал " + salary + "$ и теперь имеет " + this.money + "$");
    }
}
