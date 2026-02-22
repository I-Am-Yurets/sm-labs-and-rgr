package stu.cn.ua.rgr3;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import rnd.Randomable;

import java.util.function.BooleanSupplier;

/**
 * Cashier – касир супермаркету.
 *
 * Сценарій (rule):
 *  1. Чекає, поки у черзі до каси з'явиться покупець.
 *  2. Забирає першого покупця з черги.
 *  3. Виконує обслуговування (час обслуговування залежить від кількості покупок – serviceRnd).
 *  4. Позначає покупця як обслуженого (customer.markServed()).
 *  5. Повторює цикл.
 */
public class Cashier extends Actor {

    private QueueForTransactions<Customer> queueToCashier;
    private double finishTime;
    /** Генератор часу обслуговування одного покупця касиром. */
    private Randomable serviceRnd;

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier hasCustomer = () -> queueToCashier.size() > 0;

        while (getDispatcher().getCurrentTime() <= finishTime) {
            // Чекаємо покупця
            waitForCondition(hasCustomer, "поки з'явиться покупець у черзі");

            Customer customer = queueToCashier.removeFirst();
            getDispatcher().printToProtocol(
                getNameForProtocol() + " починає обслуговувати " + customer.getNameForProtocol());

            // Час розрахунку на касі
            holdForTime(serviceRnd.next());

            getDispatcher().printToProtocol(
                getNameForProtocol() + " завершив обслуговування " + customer.getNameForProtocol());

            // Звільняємо покупця
            customer.markServed();
        }
    }

    // ── Setters ───────────────────────────────────────────────────────────────

    public void setQueueToCashier(QueueForTransactions<Customer> queueToCashier) {
        this.queueToCashier = queueToCashier;
    }

    public void setFinishTime(double finishTime) {
        this.finishTime = finishTime;
    }

    public void setServiceRnd(Randomable serviceRnd) {
        this.serviceRnd = serviceRnd;
    }
}
