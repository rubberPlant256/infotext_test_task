package org.ficus.service;

import org.ficus.entity.Bank;
import org.ficus.entity.Spender;
import org.ficus.entity.Worker;

import java.util.List;

public class MainService {

    public static void shutdownAllThreads(List<Bank> banks, List<Worker> workers, List<Spender> spenders)
            throws InterruptedException {

        System.out.println("\nShutting down all clients and banks...");

        signalShutdown(banks);
        signalShutdown(workers);
        signalShutdown(spenders);

        waitForThreads(banks);
        waitForThreads(workers);
        waitForThreads(spenders);
    }

    public static <T extends Thread> void signalShutdown(List<T> threads) {
        threads.forEach(thread -> {
            if (thread instanceof Worker) {
                ((Worker) thread).shutdown();
            } else if (thread instanceof Spender) {
                ((Spender) thread).shutdown();
            } else if (thread instanceof Bank) {
                ((Bank) thread).shutdown();
            }
        });
    }

    public static <T extends Thread> void waitForThreads(List<T> threads) throws InterruptedException {
        for (T thread : threads) {
            thread.join();
            System.out.println("Thread " + thread.getName() + " has been stopped.");
        }
    }
}
