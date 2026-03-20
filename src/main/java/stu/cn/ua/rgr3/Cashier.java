package stu.cn.ua.rgr3;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import process.Store;
import rnd.Randomable;

import java.util.function.BooleanSupplier;

/**
 * Cashier – касир супермаркету.
 *
 * Сценарій (rule):
 *  1. Чекає, поки у черзі до каси з'явиться покупець.
 *  2. Забирає першого покупця з черги.
 *  3. Збільшує лічильник зайнятих касирів (busyCashiers +1).
 *  4. Виконує обслуговування (час обслуговування – serviceRnd).
 *  5. Зменшує лічильник зайнятих касирів (busyCashiers -1).
 *  6. Позначає покупця як обслуженого (customer.markServed()).
 *  7. Повторює цикл.
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
            // Чекаємо покупця у черзі
            waitForCondition(hasCustomer, "поки з'явиться покупець у черзі");

            Customer customer = queueToCashier.removeFirst();
            getDispatcher().printToProtocol(
                    getNameForProtocol() + " починає обслуговувати " + customer.getNameForProtocol());

            if (busyCashiers != null) busyCashiers.add(1);

            holdForTime(serviceRnd.next());

            if (busyCashiers != null) busyCashiers.remove(1);

            getDispatcher().printToProtocol(
                    getNameForProtocol() + " завершив обслуговування " + customer.getNameForProtocol());

            customer.markServed();

            // Виходимо лише якщо час вийшов І черга вже порожня
            if (getDispatcher().getCurrentTime() > finishTime && queueToCashier.size() == 0) {
                break;
            }
        }
    }

    public void setQueueToCashier(QueueForTransactions<Customer> queueToCashier) {
        this.queueToCashier = queueToCashier;
    }

    public void setBusyCashiers(Store busyCashiers) {
        this.busyCashiers = busyCashiers;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public void setServiceRnd(Randomable serviceRnd) {
        this.serviceRnd = serviceRnd;
    }
}
