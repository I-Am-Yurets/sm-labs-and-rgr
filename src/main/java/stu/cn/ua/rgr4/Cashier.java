package stu.cn.ua.rgr4;

import process.Actor;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import rnd.Randomable;

import java.util.function.BooleanSupplier;

/**
 * Cashier – касир супермаркету (РГР-4).
 */
public class Cashier extends Actor {

    private QueueForTransactions<Customer> queueToCashier;
    private double finishTime;
    private Randomable serviceRnd;

    @Override
    protected void rule() throws DispatcherFinishException {
        BooleanSupplier hasCustomer = () -> queueToCashier.size() > 0;

        while (getDispatcher().getCurrentTime() <= finishTime) {
            waitForCondition(hasCustomer, "поки з'явиться покупець");
            Customer customer = queueToCashier.removeFirst();
            getDispatcher().printToProtocol(
                getNameForProtocol() + " обслуговує " + customer.getNameForProtocol());
            holdForTime(serviceRnd.next());
            getDispatcher().printToProtocol(
                getNameForProtocol() + " завершив обслуговування " + customer.getNameForProtocol());
            customer.markServed();
        }
    }

    public void setQueueToCashier(QueueForTransactions<Customer> q) { this.queueToCashier = q; }
    public void setFinishTime(double v) { this.finishTime = v; }
    public void setServiceRnd(Randomable r) { this.serviceRnd = r; }
}
