package stu.cn.ua.rgr7;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;
import stat.Histo;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

/**
 * Customer – покупець супермаркету (РГР-5).
 * Додатково збирає статистику часу обслуговування (від входу до виходу).
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
    private Randomable purchasesRnd;
    private int purchases;
    private volatile boolean isServed = false;

    /** Час від входу у магазин до виходу (включаючи очікування у черзі). */
    private Histo histoServiceTime;
    private double serviceStartTime;

    /** Точний лічильник відмов (thread-safe, без прив'язки до Store-часу). */
    private AtomicInteger lostCount = new AtomicInteger(0);

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier servedCondition = () -> isServed;

        while (getDispatcher().getCurrentTime() <= finishTime) {
            holdForTime(arrivalRnd.next());

            int criticalQueueSize = ThreadLocalRandom.current().nextInt(
                    1, Math.max(2, (int) Math.round(maxQueueSize) + 1));
            boolean queueOverflow = queueToCashier.size() >= criticalQueueSize;
            boolean storeOverflow = customersInStore.getSize() >= maxCustomersInStore;

            if (queueOverflow || storeOverflow) {
                lostCustomers.add(1);
                lostCount.incrementAndGet();
                getDispatcher().printToProtocol(
                        getNameForProtocol() + " не зайшов до магазину (черга=" +
                                queueToCashier.size() + ", критична=" + criticalQueueSize +
                                ", зал=" + customersInStore.getSize() + ")");
                continue;
            }

            // Початок відліку часу обслуговування
            serviceStartTime = getDispatcher().getCurrentTime();

            customersInStore.add(1);
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " зайшов до залу (" + customersInStore.getSize() + ")");

            purchases = Math.max(1, (int) purchasesRnd.next());
            double shoppingTime = Math.max(0.001, shoppingRnd.next()) * purchases;
            holdForTime(shoppingTime);
            isServed = false;
            queueToCashier.addLast(this);
            waitForCondition(servedCondition, "поки касир обслужить");

            // Фіксуємо повний час перебування
            double serviceTime = getDispatcher().getCurrentTime() - serviceStartTime;
            histoServiceTime.add(serviceTime);

            customersInStore.remove(1);
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " покинув магазин (" + customersInStore.getSize() + " у залі)");
        }
    }

    public void markServed() { isServed = true; }
    public int getPurchases() { return purchases; }
    public int getLostCount() { return lostCount.get(); }

    public void setQueueToCashier(QueueForTransactions<Customer> q) { this.queueToCashier = q; }
    public void setCustomersInStore(Store s) { this.customersInStore = s; }
    public void setLostCustomers(Store s) { this.lostCustomers = s; }
    public void setMaxQueueSize(double v) { this.maxQueueSize = v; }
    public void setMaxCustomersInStore(double v) { this.maxCustomersInStore = v; }
    public void setFinishTime(double v) { this.finishTime = v; }
    public void setArrivalRnd(Randomable r) { this.arrivalRnd = r; }
    public void setShoppingRnd(Randomable r) { this.shoppingRnd = r; }
    public void setPurchasesRnd(Randomable r) { this.purchasesRnd = r; }
    public void setHistoForServiceTime(Histo h) { this.histoServiceTime = h; }
}