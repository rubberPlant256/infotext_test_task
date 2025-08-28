package org.ficus;

import org.ficus.config.InitializationConfig;
import org.ficus.entity.Bank;
import org.ficus.entity.Media;
import org.ficus.entity.Spender;
import org.ficus.entity.Worker;
import org.ficus.factory.CityEntityFactory;
import org.ficus.factory.HappyCityFactory;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException{
        // Используем фабрику для создания объектов
        CityEntityFactory factory = new HappyCityFactory();

        // Создаём списки для хранения наших потоков
        List<Bank> banks = new ArrayList<>();
        List<Worker> workers = new ArrayList<>();
        List<Spender> spenders = new ArrayList<>();

        // Создаём банки и запускаем их
        for (int i = 0; i < InitializationConfig.getBanksCount(); i++) {
            Bank bank = factory.createBank(i + 1);
            banks.add(bank);
            bank.start();
        }

        // Создаём рабочих
        for (int i = 0; i < InitializationConfig.getWorkersCount(); i++) {
            Worker worker = factory.createWorker(i + 1, banks);
            workers.add(worker);
        }

        // Создаём транжир и передаём им списки банков и рабочих
        for (int i = 0; i < InitializationConfig.getSpendersCount(); i++) {
            Spender spender = factory.createSpender(i + 1, banks, workers);
            spenders.add(spender);
        }

        // Рассчитываем общее количество денег в городе на старте
        int initialTotalMoney = MainService.calculateTotalMoney(banks, workers, spenders);
        System.out.println("Total money amount in city on day start: " + initialTotalMoney + "$");

        // Создаём и запускаем СМИ
        Media media = new Media(banks, workers, spenders);
        media.start();

        // Запускаем всех рабочих и транжир
        workers.forEach(Thread::start);
        spenders.forEach(Thread::start);

        // Ждём, пока рабочий день не закончится
        Thread.sleep(InitializationConfig.getDayDurationMs());

        // Корректно завершаем все потоки
        MainService.shutdownAllThreads(banks, workers, spenders);

        // Рассчитываем общее количество денег в городе в конце дня
        int finalTotalMoney = MainService.calculateTotalMoney(banks, workers, spenders);
        System.out.println("\nTotal money amount in city on day end: " + finalTotalMoney + "$");

        // Проверяем условие из пункта 8
        if (initialTotalMoney == finalTotalMoney) {
            System.out.println("Money flow is cyclic. The total amount of money remains the same.");
        } else {
            System.out.println("ERROR: Money flow is NOT cyclic. Total money has changed.");
        }
    }

}