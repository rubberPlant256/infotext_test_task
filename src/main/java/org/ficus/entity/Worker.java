package org.ficus.entity;

import org.ficus.MainService;
import org.ficus.config.InitializationConfig;

import java.util.List;
import java.util.Random;

public class Worker extends Thread implements Client {
//    private final String name;
    private int money;
    private volatile boolean isOccupied = false;
    private volatile boolean isRunning = true;
    private final List<Bank> banks;
    private final Random random = new Random();

    public Worker(int id, List<Bank> banks) {
//        this.name = "Worker-" + id;
        super("Worker-" + id);
        this.money = InitializationConfig.getInitialClientMoney();
        this.banks = banks;
    }

//    @Override
//    public String getName() {
//        return name;
//    }

    @Override
    public int getMoney() {
        return money;
    }

    @Override
    public void setMoney(int amount) {
        this.money = amount;
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
                // Если денег много, относим их в банк
                if (money > InitializationConfig.getWorkerMoneyLimitForBank()) {
                    System.out.println(super.getName() + " накопил " + money + "$ и решил отнести их в банк.");

                    // Выбор случайного банка
//                    Bank bank = getRandomBank();
                    Bank bank = MainService.getRandomEntity(banks);

                    // Занимаем банк
                    bank.occupy();
                    System.out.println(super.getName() + " начал вносить деньги в " + bank.getBankName());

                    // Выполняем транзакцию
                    int moneyToDeposit = this.money;
                    bank.addMoney(moneyToDeposit);
                    this.setMoney(0);

                    // Имитация работы с банком
                    Thread.sleep(InitializationConfig.getWorkerWorkDurationMs());

                    // Освобождаем банк
                    bank.release();
                    System.out.println(super.getName() + " завершил операцию в " + bank.getBankName()
                            + ". Баланс: " + this.money + "$");
                }

                // Имитация ожидания нового задания
                Thread.sleep(100 + random.nextInt(100));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

//    private Bank getRandomBank() {
//        return banks.get(random.nextInt(banks.size()));
//    }

    public void shutdown() {
        this.isRunning = false;
        this.interrupt();
    }

    public synchronized void earnMoney(int salary) {
        this.money += salary;
        System.out.println(super.getName() + " заработал " + salary + "$ и теперь имеет " + this.money + "$");
    }
}
