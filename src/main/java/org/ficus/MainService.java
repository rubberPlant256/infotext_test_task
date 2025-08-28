package org.ficus;

import org.ficus.entity.Bank;
import org.ficus.entity.Spender;
import org.ficus.entity.Worker;

import java.util.List;
import java.util.Random;

public class MainService {

    private static final Random random = new Random();

    // Метод, который использует Generics
    public static <T> T getRandomEntity(List<T> entities) {
        if (entities == null || entities.isEmpty()) {
            return null;
        }
        int randomIndex = random.nextInt(entities.size());
        return entities.get(randomIndex);
    }

    public static int calculateTotalMoney(List<Bank> banks, List<Worker> workers, List<Spender> spenders) {
        int totalMoneyInBanks = banks.stream().mapToInt(Bank::getMoney).sum();
        int totalMoneyInWorkers = workers.stream().mapToInt(Worker::getMoney).sum();
        int totalMoneyInSpenders = spenders.stream().mapToInt(Spender::getMoney).sum();
        return totalMoneyInBanks + totalMoneyInWorkers + totalMoneyInSpenders;
    }

    public static void shutdownAllThreads(List<Bank> banks, List<Worker> workers, List<Spender> spenders) throws InterruptedException {
        // Остановка рабочих и транжир
        System.out.println("\nShutting down all clients and banks...");
        workers.forEach(Worker::shutdown);
        spenders.forEach(Spender::shutdown);
        banks.forEach(Bank::shutdown);

        // Ждём завершения всех потоков (join)
        for (Worker worker : workers) {
            worker.join();
            System.out.println("Thread " + worker.getName() + " has been stopped.");
        }
        for (Spender spender : spenders) {
            spender.join();
            System.out.println("Thread " + spender.getName() + " has been stopped.");
        }
        for (Bank bank : banks) {
            bank.join();
            System.out.println("Thread " + bank.getBankName() + " has been stopped.");
        }
    }
}
