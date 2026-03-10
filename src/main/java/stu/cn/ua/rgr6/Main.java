/*
 * Created by JFormDesigner on Wed Feb 26 07:39:29 EET 2025
 */

package stu.cn.ua.rgr6;

import net.miginfocom.swing.MigLayout;
import process.Dispatcher;
import process.IModelFactory;
import rnd.Erlang;
import rnd.Negexp;
import rnd.Norm;
import widgets.ChooseData;
import widgets.ChooseRandom;
import widgets.Diagram;

import javax.imageio.ImageIO;
import com.formdev.flatlaf.FlatIntelliJLaf;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import widgets.experiments.*;
import widgets.stat.*;

/**
 * Варіант 19 – Моделювання роботи супермаркету (РГР-6).
 * Додано StatisticsManager та ExperimentManager.
 */
public class Main extends JFrame {

    public Main() {
        initComponents();
        loadHtmlTask();
        statisticsManager.setFactory((d) -> new Model(d, this));
        experimentManager.setFactory((d) -> new Model(d, this));
    }

    public static void main(String[] args) {
        FlatIntelliJLaf.setup();
        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public JButton getDiagramStartbutton()                { return diagramStartbutton; }
    public ChooseRandom getChooseRandomCustomerArrival()  { return chooseRandomCustomerArrival; }
    public ChooseRandom getChooseRandomShoppingTime()     { return chooseRandomShoppingTime; }
    public ChooseRandom getChooseRandomCashierService()   { return chooseRandomCashierService; }
    public ChooseData   getChooseDataCashiers()           { return chooseDataCashiers; }
    public ChooseRandom getChooseRandomPurchasesPerCustomer() { return chooseRandomPurchasesPerCustomer; }
    public ChooseData   getChooseDataSimulationTime()     { return chooseDataSimulationTime; }
    public Diagram      getDiagramQueueToCashier()        { return diagramQueueToCashier; }
    public Diagram      getDiagramCustomersInStore()      { return diagramCustomersInStore; }
    public Diagram      getDiagramLostCustomers()         { return diagramLostCustomers; }
    public Diagram      getDiagramCashierLoad()           { return diagramCashierLoad; }
    public JCheckBox    getConsoleLoggerCheckBox()        { return consoleLoggerCheckBox; }

    // ─── Diagram caret updates ────────────────────────────────────────────────

    private void chooseDataCashiersCaretUpdate(CaretEvent e) {
        if (testPanel.isShowing()) {
            try {
                diagramCashierLoad.setVerticalMaxText(chooseDataCashiers.getText());
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    private void chooseDataSimulationTimeCaretUpdate(CaretEvent e) {
        if (testPanel.isShowing()) {
            try {
                String t = chooseDataSimulationTime.getText();
                diagramQueueToCashier.setHorizontalMaxText(t);
                diagramCustomersInStore.setHorizontalMaxText(t);
                diagramLostCustomers.setHorizontalMaxText(t);
                diagramCashierLoad.setHorizontalMaxText(t);
            } catch (Exception ex) { System.err.println(ex.getMessage()); }
        }
    }

    private void tabbedPaneStateChanged(ChangeEvent e) {
        try {
            if (tabbedPane.getSelectedComponent() == testPanel) {
                chooseDataSimulationTime.setInt(500);
                String t = chooseDataSimulationTime.getText();
                diagramQueueToCashier.setHorizontalMaxText(t);
                diagramCustomersInStore.setHorizontalMaxText(t);
                diagramLostCustomers.setHorizontalMaxText(t);
                diagramCashierLoad.setHorizontalMaxText(t);
                diagramCashierLoad.setVerticalMaxText(chooseDataCashiers.getText());
                System.out.println("Switched to Test tab: sim time = 500");
            } else if (tabbedPane.getSelectedComponent() == statPanel
                    || tabbedPane.getSelectedComponent() == regresPanel) {
                chooseDataSimulationTime.setInt(10000);
                System.out.println("Switched to Stat/Regres tab: sim time = 10000");
            }
        } catch (Exception ex) { System.err.println(ex.getMessage()); }
    }

    // ─── startTest ────────────────────────────────────────────────────────────

    private void startTest(ActionEvent e) {
        diagramQueueToCashier.clear();
        diagramCustomersInStore.clear();
        diagramLostCustomers.clear();
        diagramCashierLoad.clear();

        Dispatcher dispatcher = new Dispatcher();
        IModelFactory factory = (d) -> new Model(d, this);
        Model model = (Model) factory.createModel(dispatcher);

        diagramStartbutton.setEnabled(false);
        dispatcher.addDispatcherFinishListener(() -> SwingUtilities.invokeLater(() -> diagramStartbutton.setEnabled(true)));
        model.initForTest();
        dispatcher.start();
    }

    // ─── initComponents ───────────────────────────────────────────────────────

    private void initComponents() {
        splitPane                 = new JSplitPane();
        leftSettingModelPanel     = new JPanel();
        Title                     = new JLabel();
        chooseRandomCustomerArrival = new ChooseRandom();
        chooseRandomShoppingTime  = new ChooseRandom();
        chooseRandomCashierService= new ChooseRandom();
        chooseDataCashiers        = new ChooseData();
        chooseRandomPurchasesPerCustomer = new ChooseRandom();
        chooseDataSimulationTime  = new ChooseData();
        tabbedPane                = new JTabbedPane();
        taskScrollPanel           = new JScrollPane();
        textHtmTask               = new JTextPane();
        testPanel                 = new JPanel();
        diagramQueueToCashier     = new Diagram();
        diagramCustomersInStore   = new Diagram();
        diagramLostCustomers      = new Diagram();
        diagramCashierLoad        = new Diagram();
        diagramInteractionPanel   = new JPanel();
        consoleLoggerCheckBox     = new JCheckBox();
        diagramStartbutton        = new JButton();
        statPanel                 = new JPanel();
        statisticsManager         = new StatisticsManager();
        regresPanel               = new JPanel();
        experimentManager         = new ExperimentManager();
        transientPanel            = new JPanel();
        infoPanel                 = new JPanel();
        photoPanel                = new JPanel();
        textInfoAuthor            = new JTextArea();

        setTitle("РГР. Варіант 19 – Супермаркет");
        getContentPane().setLayout(new BorderLayout());

        // ── Left panel ────────────────────────────────────────────────────────
        leftSettingModelPanel.setLayout(new MigLayout("hidemode 3,aligny center", "[262,fill]",
                "[][][][][][][]"));
        Title.setText("Параметри системи, що досліджується");
        Title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leftSettingModelPanel.add(Title, "cell 0 0,alignx center,growx 0");

        chooseRandomCustomerArrival.setBorder(new CompoundBorder(new EtchedBorder(),
                new TitledBorder(LineBorder.createBlackLineBorder(), "Інтервал приходу покупців",
                        TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
        chooseRandomCustomerArrival.setRandom(new Negexp(10));
        leftSettingModelPanel.add(chooseRandomCustomerArrival, "cell 0 1,aligny center,growy 0");

        chooseRandomShoppingTime.setBorder(new CompoundBorder(new EtchedBorder(),
                new TitledBorder(LineBorder.createBlackLineBorder(), "Час перебування покупця в залі",
                        TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
        chooseRandomShoppingTime.setRandom(new Norm(30, 8));
        leftSettingModelPanel.add(chooseRandomShoppingTime, "cell 0 2,aligny center,growy 0");

        chooseRandomCashierService.setBorder(new CompoundBorder(new EtchedBorder(),
                new TitledBorder(LineBorder.createBlackLineBorder(), "Час обслуговування касиром",
                        TitledBorder.CENTER, TitledBorder.TOP, new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
        chooseRandomCashierService.setRandom(new Norm(5, 2));
        leftSettingModelPanel.add(chooseRandomCashierService, "cell 0 3,aligny center,growy 0");

        //---- chooseRandomPurchasesPerCustomer ----
        chooseRandomPurchasesPerCustomer.setRandom(new Erlang(3, 5, true));
        chooseRandomPurchasesPerCustomer.setTitle("Кількість покупок (Ерланг)");
        chooseRandomPurchasesPerCustomer.setBorder(new CompoundBorder(
                new EtchedBorder(),
                new TitledBorder(LineBorder.createBlackLineBorder(),
                        "Кількість покупок (Ерланг)", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
        leftSettingModelPanel.add(chooseRandomPurchasesPerCustomer, "cell 0 4,aligny center,growy 0");

        //---- chooseDataCashiers ----
        chooseDataCashiers.setTitle("Кількість кас (касирів)");
        chooseDataCashiers.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),
                        "Кількість кас (касирів)", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font("Dialog", Font.PLAIN, 14)),
                new BevelBorder(BevelBorder.LOWERED)));
        chooseDataCashiers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chooseDataCashiers.setMinimumSize(new Dimension(50, 55));
        chooseDataCashiers.addCaretListener(e -> chooseDataCashiersCaretUpdate(e));
        chooseDataCashiers.setInt(3);
        leftSettingModelPanel.add(chooseDataCashiers, "cell 0 5,aligny center,growy 0");

        //---- chooseDataSimulationTime ----
        chooseDataSimulationTime.setTitle("Час моделювання");
        chooseDataSimulationTime.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),
                        "Час моделювання", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font("Dialog", Font.PLAIN, 14)),
                new BevelBorder(BevelBorder.LOWERED)));
        chooseDataSimulationTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        chooseDataSimulationTime.setMinimumSize(new Dimension(50, 55));
        chooseDataSimulationTime.addCaretListener(e -> chooseDataSimulationTimeCaretUpdate(e));
        chooseDataSimulationTime.setInt(500);
        leftSettingModelPanel.add(chooseDataSimulationTime, "cell 0 6,aligny center,growy 0");

        splitPane.setLeftComponent(leftSettingModelPanel);

        // ── Tabbed pane ───────────────────────────────────────────────────────
        tabbedPane.addChangeListener(e -> tabbedPaneStateChanged(e));

        textHtmTask.setEditable(false);
        textHtmTask.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        taskScrollPanel.setViewportView(textHtmTask);
        tabbedPane.addTab("Завдання", taskScrollPanel);

        // Test tab
        testPanel.setLayout(new MigLayout("hidemode 3", "[646,grow,fill]",
                "[grow][grow]rel[grow][grow][][]"));

        setupDiagram(diagramQueueToCashier,   "Черга покупців до кас",          new Color(0x3399ff));
        testPanel.add(diagramQueueToCashier,   "cell 0 0,grow");
        setupDiagram(diagramCustomersInStore,  "Покупці у торговельному залі",   new Color(0x33cc33));
        testPanel.add(diagramCustomersInStore,  "cell 0 1,grow");
        setupDiagram(diagramLostCustomers,     "Втрачені покупці",               new Color(0xcc0000));
        testPanel.add(diagramLostCustomers,     "cell 0 2,grow");
        setupDiagram(diagramCashierLoad,       "Завантаженість касирів",         new Color(0xffaa00));
        testPanel.add(diagramCashierLoad,       "cell 0 3,grow");

        diagramInteractionPanel.setLayout(new BorderLayout());
        consoleLoggerCheckBox.setText("Лог у консоль");
        consoleLoggerCheckBox.setPreferredSize(new Dimension(200, 20));
        consoleLoggerCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        diagramInteractionPanel.add(consoleLoggerCheckBox, BorderLayout.WEST);
        diagramStartbutton.setText("Старт");
        diagramStartbutton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        diagramStartbutton.addActionListener(e -> startTest(e));
        diagramInteractionPanel.add(diagramStartbutton, BorderLayout.EAST);
        testPanel.add(diagramInteractionPanel, "cell 0 4");
        JScrollPane testScrollPanel = new JScrollPane(testPanel);
        testScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab("Тест", testScrollPanel);

        // Stat tab
        statPanel.setLayout(new MigLayout("hidemode 3", "[646,grow,fill]", "[grow]rel"));
        statisticsManager.setBorder(LineBorder.createBlackLineBorder());
        statPanel.add(statisticsManager, "cell 0 0,grow");
        tabbedPane.addTab("Статистика", statPanel);

        // Regres tab
        regresPanel.setLayout(new MigLayout("hidemode 3", "[646,grow,fill]", "[grow]rel"));
        experimentManager.getChooseDataRepeat().setTitle("Кількість касирів");
        regresPanel.add(experimentManager, "cell 0 0,grow");
        tabbedPane.addTab("Регресія", regresPanel);

        // Transient tab (placeholder)
        transientPanel.setLayout(new MigLayout("hidemode 3", "[fill][fill]", "[][][]"));
        tabbedPane.addTab("Перехідний процес", transientPanel);

        // Info tab
        buildInfoPanel();
        tabbedPane.addTab("Інфо", infoPanel);

        splitPane.setRightComponent(tabbedPane);
        getContentPane().add(splitPane, BorderLayout.CENTER);
        setSize(960, 665);
        setLocationRelativeTo(getOwner());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private void setupChooseData(ChooseData cd, String title, int defaultVal) {
        cd.setTitle(title);
        cd.setBorder(new CompoundBorder(
                new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), title,
                        TitledBorder.CENTER, TitledBorder.BELOW_TOP, new Font("Dialog", Font.PLAIN, 14)),
                new BevelBorder(BevelBorder.LOWERED)));
        cd.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cd.setMinimumSize(new Dimension(50, 55));
        cd.setInt(defaultVal);
    }

    private void setupDiagram(Diagram d, String title, Color painterColor) {
        d.setTitleText(title);
        d.setPanelBackground(new Color(0xf0f0f0));
        d.setGridColor(new Color(0xcccccc));
        d.setPainterColor(painterColor);
        d.setGridByX(10);
    }

    private void buildInfoPanel() {
        infoPanel.setMinimumSize(new Dimension(887, 135));
        infoPanel.setLayout(new MigLayout("hidemode 3", "[649,grow,fill]", "[319][108]"));
        photoPanel = createPhotoPanel();
        photoPanel.setPreferredSize(new Dimension(1500, 1500));
        photoPanel.setMinimumSize(new Dimension(0, 0));
        photoPanel.setLayout(null);
        infoPanel.add(photoPanel, "cell 0 0");
        photoPanel.setPreferredSize(new Dimension(1500, 1500));
        textInfoAuthor.setText(
                "Автор проекту:\nСергієнко Юрій Сергійович,\nстудент 3 курсу, спеціальність Комп'ютерна інженерія\n" +
                        "Варіант 19 – Моделювання роботи супермаркету");
        textInfoAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        textInfoAuthor.setEditable(false);
        infoPanel.add(textInfoAuthor, "cell 0 1");
    }

    private void loadHtmlTask() {
        URL url = getClass().getResource("/tz.htm");
        if (url != null) {
            try { textHtmTask.setPage(url); }
            catch (IOException e) { System.err.println("Problems with /tz.htm"); }
        }
    }

    private JPanel createPhotoPanel() {
        return new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                URL url = getClass().getResource("/foto.jpg");
                if (url != null) {
                    try {
                        BufferedImage img = ImageIO.read(url);
                        double k = (double) img.getHeight() / img.getWidth();
                        int w = getWidth(), h = getHeight();
                        if ((double) h / w > k) h = (int)(w * k); else w = (int)(h / k);
                        g.drawImage(img.getScaledInstance(w, h, Image.SCALE_SMOOTH), 0, 0, null);
                    } catch (IOException e) { e.printStackTrace(); }
                }
            }
        };
    }

    // ─── Variables ────────────────────────────────────────────────────────────
    private JSplitPane splitPane;
    private JPanel leftSettingModelPanel;
    private JLabel Title;
    private ChooseRandom chooseRandomCustomerArrival;
    private ChooseRandom chooseRandomShoppingTime;
    private ChooseRandom chooseRandomCashierService;
    private ChooseData chooseDataCashiers;
    private ChooseRandom chooseRandomPurchasesPerCustomer;
    private ChooseData chooseDataSimulationTime;
    private JTabbedPane tabbedPane;
    private JScrollPane taskScrollPanel;
    private JTextPane textHtmTask;
    private JPanel testPanel;
    private Diagram diagramQueueToCashier;
    private Diagram diagramCustomersInStore;
    private Diagram diagramLostCustomers;
    private Diagram diagramCashierLoad;
    private JPanel diagramInteractionPanel;
    private JCheckBox consoleLoggerCheckBox;
    private JButton diagramStartbutton;
    private JPanel statPanel;
    private StatisticsManager statisticsManager;
    private JPanel regresPanel;
    private ExperimentManager experimentManager;
    private JPanel transientPanel;
    private JPanel infoPanel;
    private JPanel photoPanel;
    private JTextArea textInfoAuthor;
}
