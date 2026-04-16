package stu.cn.ua.rgr7;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

import java.util.function.BooleanSupplier;

/**
 * Cashier – касир супермаркету (РГР-7).
 *
 * Сценарій (rule):
 *  1. Чекає покупця у черзі.
 *  2. Збільшує лічильник зайнятих касирів (busyCashiers +1).
 *  3. Виконує обслуговування (serviceRnd).
 *  4. Зменшує лічильник (busyCashiers -1), позначає покупця обслуженим.
 */
public class Cashier extends Actor {

    private QueueForTransactions<Customer> queueToCashier;
    private Store busyCashiers;
    private double finishTime;
    private Randomable serviceRnd;

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier hasCustomer = () -> queueToCashier.size() > 0;

        while (true) {
            waitForCondition(hasCustomer, "поки з'явиться покупець");
            Customer customer = queueToCashier.removeFirst();
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " обслуговує " + customer.getNameForProtocol());
            if (busyCashiers != null) busyCashiers.add(1);
            int purchases = Math.max(1, customer.getPurchases());
            double servicePerItem = Math.max(0.001, serviceRnd.next());
            holdForTime(servicePerItem * purchases);
            if (busyCashiers != null) busyCashiers.remove(1);
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " завершив обслуговування " + customer.getNameForProtocol());
            customer.markServed();

            // Після завершення часу моделювання доробляємо лише поточну чергу.
            if (getDispatcher().getCurrentTime() > finishTime && queueToCashier.size() == 0) {
                break;
            }
        }
    }

    public void setQueueToCashier(QueueForTransactions<Customer> q) { this.queueToCashier = q; }
    public void setBusyCashiers(Store s) { this.busyCashiers = s; }
    public void setFinishTime(double v) { this.finishTime = v; }
    public void setServiceRnd(Randomable r) { this.serviceRnd = r; }
}