package org.ficus.factory;

import org.ficus.entity.Bank;
import org.ficus.entity.Spender;
import org.ficus.entity.Worker;

import java.util.List;

public interface CityEntityFactory {

    Bank createBank(int id);
    Worker createWorker(int id, List<Bank> banks);
    Spender createSpender(int id, List<Bank> banks, List<Worker> workers);
}
