package stu.cn.ua.rgr6;

import process.Dispatcher;
import process.MultiActor;
import process.QueueForTransactions;
import process.Store;
import stat.DiscretHisto;
import stat.Histo;
import stat.IHisto;
import widgets.experiments.IExperimentable;
import widgets.stat.IStatisticsable;

import java.util.HashMap;
import java.util.Map;

/**
 * Model – модель супермаркету (РГР-6).
 * Реалізує IStatisticsable та IExperimentable.
 * Фактором регресійного експерименту є кількість касирів.
 */
public class Model implements IStatisticsable, IExperimentable {

    private final Dispatcher dispatcher;
    private final Main gui;

    // ── Параметри, зчитані з GUI у конструкторі ───────────────────────────────
    private final double paramMaxQueueSize;
    private final double paramMaxCustomersInStore;
    private final rnd.Randomable paramArrivalRnd;
    private final rnd.Randomable paramShoppingRnd;
    private final rnd.Randomable paramServiceRnd;
    private final double paramFinishTime;
    private final int    paramCashiers;
    private final boolean paramConsoleLog;

    private Customer customer;
    private MultiActor multiCustomer;
    private Cashier cashier;
    private MultiActor multiCashier;

    private QueueForTransactions<Customer> queueToCashier;
    private Store customersInStore;
    private Store lostCustomers;

    DiscretHisto histoQueueToCashier      = new DiscretHisto();
    Histo        histoCustomersInStore    = new Histo();
    Histo        histoLostCustomers       = new Histo();
    Histo        histoCustomerWait        = new Histo();
    Histo        histoCashierWait         = new Histo();
    Histo        histoCustomerServiceTime = new Histo();

    public Model(Dispatcher d, Main g) {
        if (d == null || g == null) {
            System.out.println("Не визначено диспетчера або GUI для Model");
            System.exit(0);
        }
        dispatcher = d;
        gui = g;

        // Зчитуємо всі параметри GUI одразу — до старту Dispatcher
        paramMaxQueueSize        = g.getChooseDataMaxQueueSize().getDouble();
        paramMaxCustomersInStore = g.getChooseDataMaxCustomersInStore().getDouble();
        paramArrivalRnd          = g.getChooseRandomCustomerArrival().getRandom();
        paramShoppingRnd         = g.getChooseRandomShoppingTime().getRandom();
        paramServiceRnd          = g.getChooseRandomCashierService().getRandom();
        paramFinishTime          = g.getChooseDataSimulationTime().getDouble();
        paramCashiers            = g.getChooseDataCashiers().getInt();
        paramConsoleLog          = g.getConsoleLoggerCheckBox().isSelected();

        componentsToStartList();
    }

    private void componentsToStartList() {
        dispatcher.addStartingActor(getMultiCustomer());
        dispatcher.addStartingActor(getMultiCashier());
    }

    public void initForTest() {
        getCustomersInStore().setPainter(gui.getDiagramCustomersInStore().getPainter());
        getQueueToCashier().setPainter(gui.getDiagramQueueToCashier().getPainter());
        getLostCustomers().setPainter(gui.getDiagramLostCustomers().getPainter());
        dispatcher.setProtocolFileName(
            paramConsoleLog ? "Console" : "");
    }

    public Customer getCustomer() {
        if (customer == null) {
            customer = new Customer();
            customer.setNameForProtocol("Покупець");
            customer.setHistoForActorWaitingTime(histoCustomerWait);
            customer.setHistoForServiceTime(histoCustomerServiceTime);
            customer.setQueueToCashier(getQueueToCashier());
            customer.setCustomersInStore(getCustomersInStore());
            customer.setLostCustomers(getLostCustomers());
            customer.setMaxQueueSize(paramMaxQueueSize);
            customer.setMaxCustomersInStore(paramMaxCustomersInStore);
            customer.setArrivalRnd(paramArrivalRnd);
            customer.setShoppingRnd(paramShoppingRnd);
            customer.setFinishTime(paramFinishTime);
        }
        return customer;
    }

    public MultiActor getMultiCustomer() {
        if (multiCustomer == null) {
            multiCustomer = new MultiActor();
            multiCustomer.setNameForProtocol("Потоки покупців");
            multiCustomer.setOriginal(getCustomer());
            multiCustomer.setNumberOfClones(Math.max(1, paramCashiers * 3));
        }
        return multiCustomer;
    }

    public Cashier getCashier() {
        if (cashier == null) {
            cashier = new Cashier();
            cashier.setNameForProtocol("Касир");
            cashier.setHistoForActorWaitingTime(histoCashierWait);
            cashier.setQueueToCashier(getQueueToCashier());
            cashier.setServiceRnd(paramServiceRnd);
            cashier.setFinishTime(paramFinishTime);
        }
        return cashier;
    }

    public MultiActor getMultiCashier() {
        if (multiCashier == null) {
            multiCashier = new MultiActor();
            multiCashier.setNameForProtocol("Каси");
            multiCashier.setOriginal(getCashier());
            multiCashier.setNumberOfClones(paramCashiers);
        }
        return multiCashier;
    }

    public QueueForTransactions<Customer> getQueueToCashier() {
        if (queueToCashier == null) {
            queueToCashier = new QueueForTransactions<>("Черга до кас", dispatcher, histoQueueToCashier);
            queueToCashier.setNameForProtocol("Черга до кас");
        }
        return queueToCashier;
    }

    public Store getCustomersInStore() {
        if (customersInStore == null) {
            customersInStore = new Store();
            customersInStore.setNameForProtocol("Покупці у залі");
            customersInStore.setDispatcher(dispatcher);
            customersInStore.setHisto(histoCustomersInStore);
        }
        return customersInStore;
    }

    public Store getLostCustomers() {
        if (lostCustomers == null) {
            lostCustomers = new Store();
            lostCustomers.setNameForProtocol("Втрачені покупці");
            lostCustomers.setDispatcher(dispatcher);
            lostCustomers.setHisto(histoLostCustomers);
        }
        return lostCustomers;
    }

    // ─── IStatisticsable ───────────────────────────────────────────────────────

    @Override
    public void initForStatistics() { /* не потрібна додаткова ініціалізація */ }

    @Override
    public Map<String, IHisto> getStatistics() {
        Map<String, IHisto> map = new HashMap<>();
        map.put("Черга до кас",                        histoQueueToCashier);
        map.put("Покупці у залі",                      histoCustomersInStore);
        map.put("Втрачені покупці",                    histoLostCustomers);
        map.put("Час очікування покупця",              histoCustomerWait);
        map.put("Час очікування касира",               histoCashierWait);
        map.put("Повний час обслуговування покупця",   histoCustomerServiceTime);
        return map;
    }

    // ─── IExperimentable ──────────────────────────────────────────────────────

    /** Фактор = кількість відкритих кас. */
    @Override
    public void initForExperiment(double factor) {
        multiCashier.setNumberOfClones((int) factor);
        // Масштабуємо також кількість потоків покупців
        multiCustomer.setNumberOfClones(Math.max(1, (int) factor * 3));
    }

    @Override
    public Map<String, Double> getResultOfExperiment() {
        Map<String, Double> result = new HashMap<>();
        result.put("Середня черга до кас",              histoQueueToCashier.getAverage());
        result.put("Середній час обслуговування",       histoCustomerServiceTime.getAverage());
        result.put("Середня кількість втрачених",       histoLostCustomers.getAverage());
        return result;
    }
}
