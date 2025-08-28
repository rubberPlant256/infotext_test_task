package org.ficus.factory;

import org.ficus.entity.Bank;
import org.ficus.entity.Spender;
import org.ficus.entity.Worker;

import java.util.List;

public class HappyCityFactory implements CityEntityFactory{

    @Override
    public Bank createBank(int id) {
        return new Bank(id);
    }

    @Override
    public Worker createWorker(int id, List<Bank> banks) {
        return new Worker(id, banks);
    }

    @Override
    public Spender createSpender(int id, List<Bank> banks, List<Worker> workers) {
        return new Spender(id, banks, workers);
    }
}
