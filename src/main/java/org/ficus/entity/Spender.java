package org.ficus.entity;

import org.ficus.config.InitializationConfig;
import org.ficus.service.RandomService;
import org.ficus.util.LoggerUtil;

import java.util.List;
import java.util.Random;

public class Spender extends AbstractClient {

    private final List<Worker> workers;

    private final Random random = new Random();
    HelpDesk helpDesk = HelpDesk.getInstance();

    public Spender(int id, List<Bank> banks, List<Worker> workers) {
        super("Spender-" + id,
                InitializationConfig.getInitialClientMoney(),
                banks);
        this.workers = workers;
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                if (money <= 0) {
                    takeCreditFromBank();
                } else {
                    hireWorker();
                }

                Thread.sleep(100 + random.nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                this.isRunning = false;
            }
        }
    }

    private void takeCreditFromBank() throws InterruptedException {
        helpDesk.getHelp(this.getName() + " обанкротился и пошёл за кредитом в банк.");

        Bank bank = RandomService.getRandomEntity(banks);

        bank.occupy();
        helpDesk.getHelp(this.getName() + " начал брать кредит в " + bank.getBankName());

        int creditAmount = InitializationConfig.getWorkerSalary();
        bank.takeMoney(creditAmount);
        this.setMoney(creditAmount);

        Thread.sleep(InitializationConfig.getWorkerWorkDurationMs());

        bank.release();
        helpDesk.getHelp(this.getName() + " успешно взял кредит в " + bank.getBankName() + ". Баланс: " + this.money + "$");
    }

    private void hireWorker() throws InterruptedException {
        helpDesk.getHelp(this.getName() + " хочет нанять рабочего.");

        Worker worker = RandomService.getRandomEntity(workers);

        worker.occupy();
        helpDesk.getHelp(this.getName() + " нанял " + worker.getName());

        int salary = InitializationConfig.getWorkerSalary();
        this.setMoney(this.getMoney() - salary);
        worker.earnMoney(salary);

        Thread.sleep(InitializationConfig.getWorkerWorkDurationMs());

        worker.release();
        helpDesk.getHelp(this.getName() + " заплатил " + worker.getName() + " и теперь имеет " + this.money + "$");
    }

}