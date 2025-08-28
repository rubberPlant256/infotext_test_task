package org.ficus.entity;

import org.ficus.config.InitializationConfig;
import org.ficus.service.MoneyCountService;

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

                Thread.sleep(InitializationConfig.getLunchDurationMs());

            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void printSummary() {

        int totalMoneyInCity = MoneyCountService.calculateTotalMoney(banks, workers, spenders);
        System.out.println("\nGood news for everyone! Total amount money in city is: " + totalMoneyInCity + "$");

        printClientBalances("Banks", banks);
        printClientBalances("Workers", workers);
        printClientBalances("Spenders", spenders);
    }

    private <T extends Thread> void printClientBalances(String clientType, List<T> clients) {
        System.out.println("--- " + clientType + " ---");
        clients.forEach(client -> {
            if (client instanceof Bank) {
                System.out.println("This " + ((Bank) client).getBankName() + " has money: " + ((Bank) client).getMoney() + "$");
            } else if (client instanceof Worker) {
                System.out.println("This " + ((Worker) client).getName() + " has money: " + ((Worker) client).getMoney() + "$");
            } else if (client instanceof Spender) {
                System.out.println("This " + ((Spender) client).getName() + " has money: " + ((Spender) client).getMoney() + "$");
            }
        });
    }
}
