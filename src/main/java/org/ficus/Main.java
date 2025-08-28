package org.ficus;

import org.ficus.config.InitializationConfig;
import org.ficus.entity.Bank;
import org.ficus.entity.Media;
import org.ficus.entity.Spender;
import org.ficus.entity.Worker;
import org.ficus.factory.CityEntityFactory;
import org.ficus.factory.HappyCityFactory;
import org.ficus.service.MainService;
import org.ficus.service.MoneyCountService;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException{

        CityEntityFactory factory = new HappyCityFactory();

        List<Bank> banks = new ArrayList<>();
        List<Worker> workers = new ArrayList<>();
        List<Spender> spenders = new ArrayList<>();

        for (int i = 0; i < InitializationConfig.getBanksCount(); i++) {
            Bank bank = factory.createBank(i + 1);
            banks.add(bank);
            bank.start();
        }

        for (int i = 0; i < InitializationConfig.getWorkersCount(); i++) {
            Worker worker = factory.createWorker(i + 1, banks);
            workers.add(worker);
        }

        for (int i = 0; i < InitializationConfig.getSpendersCount(); i++) {
            Spender spender = factory.createSpender(i + 1, banks, workers);
            spenders.add(spender);
        }

        int initialTotalMoney = MoneyCountService.calculateTotalMoney(banks, workers, spenders);
        System.out.println("Total money amount in city on day start: " + initialTotalMoney + "$");

        Media media = new Media(banks, workers, spenders);
        media.start();

        workers.forEach(Thread::start);
        spenders.forEach(Thread::start);

        Thread.sleep(InitializationConfig.getDayDurationMs());

        MainService.shutdownAllThreads(banks, workers, spenders);

        int finalTotalMoney = MoneyCountService.calculateTotalMoney(banks, workers, spenders);
        System.out.println("\nTotal money amount in city on day end: " + finalTotalMoney + "$");

        if (initialTotalMoney == finalTotalMoney) {
            System.out.println("Money flow is cyclic. The total amount of money remains the same.");
        } else {
            System.out.println("ERROR: Money flow is NOT cyclic. Total money has changed.");
        }
    }

}