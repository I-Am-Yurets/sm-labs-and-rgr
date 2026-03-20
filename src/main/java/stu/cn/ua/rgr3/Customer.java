package stu.cn.ua.rgr3;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

import java.util.function.BooleanSupplier;

/**
 * Customer – покупець супермаркету.
 *
 * Сценарій (rule):
 *  1. Чекає між приходами (інтервал приходу – випадковий).
 *  2. Перевіряє чергу: якщо черга до кас >= критичного рівня – відмовляється і рахується
 *     як «втрачений».
 *  3. Якщо торговельний зал заповнений (customersInStore >= maxCustomersInStore),
 *     також відмовляється і рахується як «втрачений».
 *  4. Інакше – заходить до залу (customersInStore.add(1)), ходить по магазину (shoppingRnd),
 *     генерує кількість покупок (purchasesRnd, розподіл Ерланга з округленням до цілих),
 *     потім стає у чергу до каси і чекає обслуговування.
 *  5. Після обслуговування покидає зал та починає цикл знову.
 */
public class Customer extends Actor {

    private QueueForTransactions<Customer> queueToCashier;
    private Store customersInStore;
    private Store lostCustomers;
    private double maxQueueSize;
    private double maxCustomersInStore;
    private double finishTime;

    /** Генератор інтервалів між приходами покупців. */
    private Randomable arrivalRnd;
    /** Генератор часу перебування у торговельному залі. */
    private Randomable shoppingRnd;
    /** Генератор кількості покупок (Ерланг, з округленням до цілих). */
    private Randomable purchasesRnd;

    /** Кількість покупок поточного візиту (для протоколу). */
    private int purchases;

    /** Прапор, який касир встановлює в true після розрахунку. */
    private volatile boolean isServed = false;

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier servedCondition = () -> isServed;

        while (getDispatcher().getCurrentTime() <= finishTime) {
            // Інтервал між приходами покупців
            holdForTime(arrivalRnd.next());

            // Перевірка черги та залу
            boolean queueOverflow = queueToCashier.size() >= maxQueueSize;
            boolean storeOverflow = customersInStore.getSize() >= maxCustomersInStore;

            if (queueOverflow || storeOverflow) {
                lostCustomers.add(1);
                getDispatcher().printToProtocol(
                        getNameForProtocol() + " не зайшов до магазину (черга=" +
                                queueToCashier.size() + ", зал=" + customersInStore.getSize() + ")");
                continue;
            }

            // Покупець заходить у зал
            customersInStore.add(1);
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " зайшов до залу (зараз у залі: " +
                            customersInStore.getSize() + ")");

            // Час перебування у залі (незалежно від кількості покупок)
            holdForTime(shoppingRnd.next());

            // Кількість покупок за розподілом Ерланга (вже заокруглено через round=true)
            purchases = Math.max(1, (int) purchasesRnd.next());
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " зібрав " + purchases + " покупок, стає у чергу до каси");

            // Стає в чергу
            isServed = false;
            queueToCashier.addLast(this);

            // Чекає поки касир обслужить
            waitForCondition(servedCondition, "поки касир обслужить");

            // Виходить з магазину
            customersInStore.remove(1);
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " покинув магазин (у залі залишилось: " +
                            customersInStore.getSize() + ")");
        }
    }

    /** Викликається касиром після завершення обслуговування. */
    public void markServed() {
        isServed = true;
    }

    /** Повертає кількість покупок поточного відвідувача. */
    public int getPurchases() {
        return purchases;
    }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setQueueToCashier(QueueForTransactions<Customer> queueToCashier) {
        this.queueToCashier = queueToCashier;
    }

    public void setCustomersInStore(Store customersInStore) {
        this.customersInStore = customersInStore;
    }

    public void setLostCustomers(Store lostCustomers) {
        this.lostCustomers = lostCustomers;
    }

    public void setMaxQueueSize(double maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
    }

    public void setMaxCustomersInStore(double maxCustomersInStore) {
        this.maxCustomersInStore = maxCustomersInStore;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public void setArrivalRnd(Randomable arrivalRnd) {
        this.arrivalRnd = arrivalRnd;
    }

    public void setShoppingRnd(Randomable shoppingRnd) {
        this.shoppingRnd = shoppingRnd;
    }

    public void setPurchasesRnd(Randomable purchasesRnd) {
        this.purchasesRnd = purchasesRnd;
    }
}
