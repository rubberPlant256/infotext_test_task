package org.ficus.entity;

import org.ficus.config.InitializationConfig;

import java.util.List;

public class Media extends Thread {

    private final List<Bank> banks;
    private final List<Worker> workers;
    private final List<Spender> spenders;

    public Media(List<Bank> banks, List<Worker> workers, List<Spender> spenders) {
        this.banks = banks;
        this.workers = workers;
        this.spenders = spenders;

        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            try {
                printSummary();

                // СМИ тоже уходит на обед
                Thread.sleep(InitializationConfig.getLunchDurationMs());

            } catch (InterruptedException e) {
                // Прерываем цикл при прерывании потока
                break;
            }
        }
    }

    public void printSummary() {
        // Рассчитываем общее количество денег в городе
        int totalMoneyInBanks = banks.stream().mapToInt(Bank::getMoney).sum();
        int totalMoneyInWorkers = workers.stream().mapToInt(Worker::getMoney).sum();
        int totalMoneyInSpenders = spenders.stream().mapToInt(Spender::getMoney).sum();
        int totalMoneyInCity = totalMoneyInBanks + totalMoneyInWorkers + totalMoneyInSpenders;

        System.out.println("\nGood news for everyone! Total amount money in city is: " + totalMoneyInCity + "$");

        // Выводим информацию по каждому банку
        banks.forEach(bank -> System.out.println("This " + bank.getBankName() + " has money: " + bank.getMoney() + "$"));

        // Выводим информацию по каждому рабочему
        workers.forEach(worker -> System.out.println("This " + worker.getName() + " has money: " + worker.getMoney() + "$"));

        // Выводим информацию по каждому транжире
        spenders.forEach(spender -> System.out.println("This " + spender.getName() + " has money: " + spender.getMoney() + "$"));
    }
}
