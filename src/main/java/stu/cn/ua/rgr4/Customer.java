package stu.cn.ua.rgr4;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

import java.util.function.BooleanSupplier;

/**
 * Customer – покупець супермаркету (РГР-4).
 *
 * Сценарій:
 *  1. Очікує між приходами (arrivalRnd).
 *  2. Якщо черга до кас >= maxQueueSize або зал переповнений – покупець втрачається.
 *  3. Інакше заходить, ходить по магазину (shoppingRnd), стає у чергу, чекає касира.
 *  4. Після розрахунку виходить.
 */
public class Customer extends Actor {

    private QueueForTransactions<Customer> queueToCashier;
    private Store customersInStore;
    private Store lostCustomers;
    private double maxQueueSize;
    private double maxCustomersInStore;
    private double finishTime;
    private Randomable arrivalRnd;
    private Randomable shoppingRnd;
    private volatile boolean isServed = false;

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier servedCondition = () -> isServed;

        while (getDispatcher().getCurrentTime() <= finishTime) {
            holdForTime(arrivalRnd.next());

            boolean queueOverflow = queueToCashier.size() >= maxQueueSize;
            boolean storeOverflow = customersInStore.getSize() >= maxCustomersInStore;

            if (queueOverflow || storeOverflow) {
                lostCustomers.add(1);
                getDispatcher().printToProtocol(
                    getNameForProtocol() + " не зайшов до магазину (черга=" +
                    queueToCashier.size() + ", зал=" + customersInStore.getSize() + ")");
                continue;
            }

            customersInStore.add(1);
            getDispatcher().printToProtocol(
                getNameForProtocol() + " зайшов до залу (" + customersInStore.getSize() + ")");

            holdForTime(shoppingRnd.next());

            isServed = false;
            queueToCashier.addLast(this);
            waitForCondition(servedCondition, "поки касир обслужить");

            customersInStore.remove(1);
            getDispatcher().printToProtocol(
                getNameForProtocol() + " покинув магазин (" + customersInStore.getSize() + " у залі)");
        }
    }

    public void markServed() { isServed = true; }

    public void setQueueToCashier(QueueForTransactions<Customer> q) { this.queueToCashier = q; }
    public void setCustomersInStore(Store s) { this.customersInStore = s; }
    public void setLostCustomers(Store s) { this.lostCustomers = s; }
    public void setMaxQueueSize(double v) { this.maxQueueSize = v; }
    public void setMaxCustomersInStore(double v) { this.maxCustomersInStore = v; }
    public void setFinishTime(double v) { this.finishTime = v; }
    public void setArrivalRnd(Randomable r) { this.arrivalRnd = r; }
    public void setShoppingRnd(Randomable r) { this.shoppingRnd = r; }
}
