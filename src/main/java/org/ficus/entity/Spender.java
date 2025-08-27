package org.ficus.entity;

import org.ficus.config.InitializationConfig;
import java.util.List;
import java.util.Random;

public class Spender extends Thread implements Client {
//    private final String name;
    private int money;
    private volatile boolean isRunning = true;
    private final List<Bank> banks;
    private final List<Worker> workers;

    private final Random random = new Random();

    public Spender(int id, List<Bank> banks, List<Worker> workers) {
        super("Spender-" + id);
        this.money = InitializationConfig.getInitialClientMoney();
        this.banks = banks;
        this.workers = workers;
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

    @Override
    public void run() {
        while (isRunning) {
            try {
                // Если у транжиры кончились деньги, он идёт в банк за кредитом
                if (money <= 0) {
                    System.out.println(this.getName() + " обанкротился и пошёл за кредитом в банк.");

                    // Выбор случайного банка
                    Bank bank = getRandomBank();

                    // Занимаем банк
                    bank.occupy();
                    System.out.println(this.getName() + " начал брать кредит в " + bank.getBankName());

                    // Берем кредит. Сумма кредита равна одной зарплате
                    int creditAmount = InitializationConfig.getWorkerSalary();
                    bank.takeMoney(creditAmount);
                    this.setMoney(creditAmount);

                    // TODO: должно ли быть время братия кредита или он моментальный
                    // Имитация работы с банком
                    Thread.sleep(InitializationConfig.getWorkerWorkDurationMs());

                    // Освобождаем банк
                    bank.release();
                    System.out.println(this.getName() + " успешно взял кредит в " + bank.getBankName()
                            + ". Баланс: " + this.money + "$");
                }

                // Если у транжиры есть деньги, он нанимает работника
                if (money > 0) {
                    System.out.println(this.getName() + " хочет нанять рабочего.");

                    // Выбор случайного рабочего
                    Worker worker = getRandomWorker();

                    // Занимаем рабочего
                    worker.occupy();
                    System.out.println(this.getName() + " нанял " + worker.getName());

                    // Платим зарплату
                    int salary = InitializationConfig.getWorkerSalary();
                    this.setMoney(this.getMoney() - salary);
                    worker.earnMoney(salary);

                    // Имитация работы с рабочим
                    Thread.sleep(InitializationConfig.getWorkerWorkDurationMs());

                    // Освобождаем рабочего
                    worker.release();
                    System.out.println(this.getName() + " заплатил " + worker.getName()
                            + " и теперь имеет " + this.money + "$");
                }

                // Имитация ожидания
                Thread.sleep(100 + random.nextInt(100));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

    private Bank getRandomBank() {
        return banks.get(random.nextInt(banks.size()));
    }

    private Worker getRandomWorker() {
        return workers.get(random.nextInt(workers.size()));
    }

    public void shutdown() {
        this.isRunning = false;
        this.interrupt();
    }
}