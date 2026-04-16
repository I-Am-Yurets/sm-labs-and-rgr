package stu.cn.ua.rgr7;

import process.Dispatcher;
import process.MultiActor;
import process.QueueForTransactions;
import process.Store;
import stat.DiscretHisto;
import stat.Histo;
import stat.IHisto;
import widgets.experiments.IExperimentable;
import widgets.stat.IStatisticsable;
import widgets.trans.ITransMonitoring;
import widgets.trans.ITransProcesable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Model – модель супермаркету (РГР-7).
 * Всі параметри GUI зчитуються одноразово в конструкторі,
 * щоб уникнути звернень до Swing-компонентів з потоку Dispatcher/TransMonitor.
 */
public class Model implements IStatisticsable, IExperimentable, ITransProcesable {

    private final Dispatcher dispatcher;
    private final Main gui;

    // ── Параметри, зчитані з GUI у конструкторі ───────────────────────────────
    private final rnd.Randomable paramArrivalRnd;
    private final rnd.Randomable paramShoppingRnd;
    private final rnd.Randomable paramServiceRnd;
    private final rnd.Randomable paramPurchasesRnd;
    private final double paramFinishTime;
    private final int    paramCashiers;
    private final int    paramMaxQueueSize;
    private final boolean paramConsoleLog;

    private Customer customer;
    private MultiActor multiCustomer;
    private Cashier cashier;
    private MultiActor multiCashier;

    private QueueForTransactions<Customer> queueToCashier;
    private Store customersInStore;
    private Store lostCustomers;
    private Store busyCashiers;

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
        paramArrivalRnd          = g.getChooseRandomCustomerArrival().getRandom();
        paramShoppingRnd         = g.getChooseRandomShoppingTime().getRandom();
        paramServiceRnd          = g.getChooseRandomCashierService().getRandom();
        paramPurchasesRnd        = g.getChooseRandomPurchasesPerCustomer().getRandom();
        paramFinishTime          = g.getChooseDataSimulationTime().getDouble();
        paramCashiers            = g.getChooseDataCashiers().getInt();
        paramMaxQueueSize        = readMaxQueueFromGui(g);
        paramConsoleLog          = g.getConsoleLoggerCheckBox().isSelected();

        componentsToStartList();
    }

    private void componentsToStartList() {
        dispatcher.addStartingActor(getMultiCustomer());
        dispatcher.addStartingActor(getMultiCashier());
    }

    /**
     * Сумісність із версіями Main, де ще немає getChooseDataMaxQueue().
     */
    private int readMaxQueueFromGui(Main g) {
        try {
            Object chooseData = g.getClass().getMethod("getChooseDataMaxQueue").invoke(g);
            if (chooseData != null) {
                Object value = chooseData.getClass().getMethod("getInt").invoke(chooseData);
                if (value instanceof Integer) {
                    return Math.max(1, (Integer) value);
                }
            }
        } catch (Exception ignored) {
            // fallback для старого GUI
        }
        return 5;
    }

    public void initForTest() {
        getCustomersInStore().setPainter(gui.getDiagramCustomersInStore().getPainter());
        getQueueToCashier().setPainter(gui.getDiagramQueueToCashier().getPainter());
        getLostCustomers().setPainter(gui.getDiagramLostCustomers().getPainter());
        getBusyCashiers().setPainter(gui.getDiagramCashierLoad().getPainter());
        dispatcher.setProtocolFileName(paramConsoleLog ? "Console" : "");
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
            customer.setMaxQueueSize(Math.max(1, paramMaxQueueSize));
            customer.setMaxCustomersInStore(Integer.MAX_VALUE);
            customer.setArrivalRnd(paramArrivalRnd);
            customer.setShoppingRnd(paramShoppingRnd);
            customer.setPurchasesRnd(paramPurchasesRnd);
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
            cashier.setBusyCashiers(getBusyCashiers());
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

    public Store getBusyCashiers() {
        if (busyCashiers == null) {
            busyCashiers = new Store();
            busyCashiers.setNameForProtocol("Завантаженість касирів");
            busyCashiers.setDispatcher(dispatcher);
        }
        return busyCashiers;
    }

    // ─── IStatisticsable ──────────────────────────────────────────────────────

    @Override public void initForStatistics() { }

    @Override
    public Map<String, IHisto> getStatistics() {
        Map<String, IHisto> map = new HashMap<>();
        map.put("Черга до кас",                       histoQueueToCashier);
        map.put("Покупці у залі",                     histoCustomersInStore);
        map.put("Втрачені покупці",                   histoLostCustomers);
        map.put("Час очікування покупця",             histoCustomerWait);
        map.put("Час очікування касира",              histoCashierWait);
        map.put("Повний час обслуговування покупця",  histoCustomerServiceTime);
        return map;
    }

    // ─── IExperimentable ─────────────────────────────────────────────────────

    @Override
    public void initForExperiment(double factor) {
        int n = Math.max(1, (int) Math.round(factor));
        getMultiCashier().setNumberOfClones(n);
        // Кількість потоків покупців масштабується разом з касирами,
        // щоб навантаження на систему залишалося пропорційним.
        getMultiCustomer().setNumberOfClones(n * 3);
        // Оновлюємо maxQueueSize у Customer, щоб відповідав поточному параметру GUI
        getCustomer().setMaxQueueSize(Math.max(1, paramMaxQueueSize));

        // Скидаємо Store-и: без цього getSize() накопичується між повторами
        // одного й того самого рівня фактора і дає хибно завищені результати.
        getLostCustomers().init();
        getCustomersInStore().init();
        getBusyCashiers().init();

        histoQueueToCashier.init();
        histoCustomersInStore.init();
        histoLostCustomers.init();
        histoCustomerWait.init();
        histoCashierWait.init();
        histoCustomerServiceTime.init();
    }

    @Override
    public Map<String, Double> getResultOfExperiment() {
        // LinkedHashMap зберігає порядок вставки — перший ключ буде вибраний
        // у комбо за замовчуванням, тому ставимо найцікавішу метрику першою.
        //
        // ВАЖЛИВО: для "Втрачених покупців" використовуємо getSize() Store-а,
        // а не histoLostCustomers.getAverage(). Store.histo записує значення
        // зважено за часом (addFrequencyForValue(dt, поточний_розмір)), тому
        // getAverage() повертає середній розмір у часі — що зростає монотонно
        // і завжди > 0 після першої відмови. getSize() — це проста сума add(1),
        // тобто загальна кількість відмов за моделювання (коректна метрика).
        Map<String, Double> result = new LinkedHashMap<>();
        result.put("Середня черга до кас",           histoQueueToCashier.getAverage());
        result.put("Середній час обслуговування",    histoCustomerServiceTime.getAverage());
        result.put("Загальна кількість втрачених",   getLostCustomers().getSize());
        return result;
    }

    // ─── ITransProcesable ────────────────────────────────────────────────────

    @Override
    public void initForTrans(double finishTime) {
        getCustomer().setFinishTime(finishTime);
        getCashier().setFinishTime(finishTime);
        // Не оновлюємо GUI звідси — TransProcessManager керує часом сам,
        // а виклики EDT з потоків моделей викликають залипання інтерфейсу
    }


    /**
     * Обгортка над ITransMonitoring, що повертає мінімум 0.001 замість 0.
     * Потрібна через баг у TransProcessManager.onComboAction:
     * при max==0 цикл while(max*k < 1) іде нескінченно і заморожує EDT.
     */
    private static class NonZeroMonitoring implements widgets.trans.ITransMonitoring {
        private final widgets.trans.ITransMonitoring delegate;
        NonZeroMonitoring(widgets.trans.ITransMonitoring delegate) {
            this.delegate = delegate;
        }
        @Override public void resetAccum() { delegate.resetAccum(); }
        @Override public double getAccumAverage() {
            double v = delegate.getAccumAverage();
            return v == 0.0 ? 0.001 : v;
        }
    }

    public Map<String, ITransMonitoring> getMonitoringObjects() {
        // LinkedHashMap — фіксований порядок ключів для комбо і keyStrings[0]
        // Всі метрики загорнуті в NonZeroMonitoring — захист від баgу бібліотеки
        // (TransProcessManager.onComboAction зависає при max==0)
        Map<String, ITransMonitoring> map = new LinkedHashMap<>();
        map.put("Черга до кас",      new NonZeroMonitoring(getQueueToCashier()));
        map.put("Покупці у залі",    new NonZeroMonitoring(getCustomersInStore()));
        map.put("Втрачені покупці",  new NonZeroMonitoring(getLostCustomers()));
        return map;
    }
}