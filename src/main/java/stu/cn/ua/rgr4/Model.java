package stu.cn.ua.rgr4;

import process.Dispatcher;
import process.MultiActor;
import process.QueueForTransactions;
import process.Store;
import stat.DiscretHisto;
import stat.Histo;

/**
 * Model – модель супермаркету (РГР-4).
 */
public class Model {

    private final Dispatcher dispatcher;
    private final Main gui;

    private Customer customer;
    private MultiActor multiCustomer;
    private Cashier cashier;
    private MultiActor multiCashier;

    private QueueForTransactions<Customer> queueToCashier;
    private Store customersInStore;
    private Store lostCustomers;

    DiscretHisto histoQueueToCashier   = new DiscretHisto();
    Histo        histoCustomersInStore = new Histo();
    Histo        histoLostCustomers    = new Histo();
    Histo        histoCustomerWait     = new Histo();
    Histo        histoCashierWait      = new Histo();

    public Model(Dispatcher d, Main g) {
        if (d == null || g == null) {
            System.out.println("Не визначено диспетчера або GUI для Model");
            System.exit(0);
        }
        dispatcher = d;
        gui = g;
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
                gui.getConsoleLoggerCheckBox().isSelected() ? "Console" : "");
    }

    public Customer getCustomer() {
        if (customer == null) {
            customer = new Customer();
            customer.setNameForProtocol("Покупець");
            customer.setHistoForActorWaitingTime(histoCustomerWait);
            customer.setQueueToCashier(getQueueToCashier());
            customer.setCustomersInStore(getCustomersInStore());
            customer.setLostCustomers(getLostCustomers());
            customer.setMaxQueueSize(gui.getChooseDataMaxQueueSize().getDouble());
            customer.setMaxCustomersInStore(Double.MAX_VALUE);
            customer.setArrivalRnd(gui.getChooseRandomCustomerArrival().getRandom());
            customer.setShoppingRnd(gui.getChooseRandomShoppingTime().getRandom());
            customer.setFinishTime(gui.getChooseDataSimulationTime().getDouble());
        }
        return customer;
    }

    public MultiActor getMultiCustomer() {
        if (multiCustomer == null) {
            multiCustomer = new MultiActor();
            multiCustomer.setNameForProtocol("Потоки покупців");
            multiCustomer.setOriginal(getCustomer());
            multiCustomer.setNumberOfClones(Math.max(1, gui.getChooseDataCashiers().getInt() * 3));
        }
        return multiCustomer;
    }

    public Cashier getCashier() {
        if (cashier == null) {
            cashier = new Cashier();
            cashier.setNameForProtocol("Касир");
            cashier.setHistoForActorWaitingTime(histoCashierWait);
            cashier.setQueueToCashier(getQueueToCashier());
            cashier.setServiceRnd(gui.getChooseRandomCashierService().getRandom());
            cashier.setFinishTime(gui.getChooseDataSimulationTime().getDouble());
        }
        return cashier;
    }

    public MultiActor getMultiCashier() {
        if (multiCashier == null) {
            multiCashier = new MultiActor();
            multiCashier.setNameForProtocol("Каси");
            multiCashier.setOriginal(getCashier());
            multiCashier.setNumberOfClones(gui.getChooseDataCashiers().getInt());
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
}
