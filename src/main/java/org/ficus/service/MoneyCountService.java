package org.ficus.service;

import org.ficus.entity.Bank;
import org.ficus.entity.Spender;
import org.ficus.entity.Worker;

import java.util.List;

public class MoneyCountService {

    public static int calculateTotalMoney(List<Bank> banks, List<Worker> workers, List<Spender> spenders) {
        int totalMoneyInBanks = banks.stream().mapToInt(Bank::getMoney).sum();
        int totalMoneyInWorkers = workers.stream().mapToInt(Worker::getMoney).sum();
        int totalMoneyInSpenders = spenders.stream().mapToInt(Spender::getMoney).sum();
        return totalMoneyInBanks + totalMoneyInWorkers + totalMoneyInSpenders;
    }
}
