/*
 * Created by JFormDesigner on Wed Feb 26 07:39:29 EET 2025
 */

package stu.cn.ua.rgr4;

import java.awt.event.*;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import process.Dispatcher;
import process.IModelFactory;
import rnd.Negexp;
import rnd.Norm;
import rnd.Erlang;
import widgets.ChooseData;
import widgets.ChooseRandom;
import widgets.Diagram;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Варіант 19 – Моделювання роботи супермаркету (SuperMarket)
 *
 * РГР-3: повна реалізація моделі + запуск симуляції.
 *
 * @author Student, КІ-23x
 */
public class Main extends JFrame {

    public Main() {
        initComponents();
        loadHtmlTask();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    // ── Getters for Model ──────────────────────────────────────────────────────

    public JButton getDiagramStartbutton()                { return diagramStartbutton; }

    public ChooseRandom getChooseRandomCustomerArrival()  { return chooseRandomCustomerArrival; }
    public ChooseRandom getChooseRandomShoppingTime()     { return chooseRandomShoppingTime; }
    public ChooseRandom getChooseRandomCashierService()   { return chooseRandomCashierService; }

    public ChooseData getChooseDataCashiers()              { return chooseDataCashiers; }
    public ChooseData getChooseDataMaxQueueSize()          { return chooseDataMaxQueueSize; }
    public ChooseRandom getChooseRandomPurchasesPerCustomer() { return chooseRandomPurchasesPerCustomer; }
    public ChooseData getChooseDataSimulationTime()        { return chooseDataSimulationTime; }

    public Diagram getDiagramQueueToCashier()    { return diagramQueueToCashier; }
    public Diagram getDiagramCustomersInStore()  { return diagramCustomersInStore; }
    public Diagram getDiagramLostCustomers()     { return diagramLostCustomers; }
    public Diagram getDiagramCashierLoad()       { return diagramCashierLoad; }

    public JCheckBox getConsoleLoggerCheckBox()  { return consoleLoggerCheckBox; }

    // ── Caret / tab listeners ──────────────────────────────────────────────────

    private void chooseDataSimulationTimeCaretUpdate(CaretEvent e) {
        if (testPanel.isShowing()) {
            try {
                String simTime = chooseDataSimulationTime.getText();
                diagramQueueToCashier.setHorizontalMaxText(simTime);
                diagramCustomersInStore.setHorizontalMaxText(simTime);
                diagramLostCustomers.setHorizontalMaxText(simTime);
                diagramCashierLoad.setHorizontalMaxText(simTime);
            } catch (Exception ex) {
                System.err.println("Error updating simulation time: " + ex.getMessage());
            }
        }
    }

    private void chooseDataMaxQueueSizeCaretUpdate(CaretEvent e) {
        if (testPanel.isShowing()) {
            try {
                diagramQueueToCashier.setVerticalMaxText(chooseDataMaxQueueSize.getText());
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    private void chooseDataCashiersCaretUpdate(CaretEvent e) {
        if (testPanel.isShowing()) {
            try {
                diagramCashierLoad.setVerticalMaxText(chooseDataCashiers.getText());
            } catch (Exception ex) {
                System.err.println("Error: " + ex.getMessage());
            }
        }
    }

    private void tabbedPaneStateChanged(ChangeEvent e) {
        if (tabbedPane.getSelectedComponent() == testPanel) {
            try {
                String simTime = chooseDataSimulationTime.getText();
                diagramQueueToCashier.setHorizontalMaxText(simTime);
                diagramCustomersInStore.setHorizontalMaxText(simTime);
                diagramLostCustomers.setHorizontalMaxText(simTime);
                diagramCashierLoad.setHorizontalMaxText(simTime);

                diagramQueueToCashier.setVerticalMaxText(chooseDataMaxQueueSize.getText());
                diagramCashierLoad.setVerticalMaxText(chooseDataCashiers.getText());
            } catch (Exception ex) {
                System.err.println("Error initializing diagram settings: " + ex.getMessage());
            }
        }
    }

    // ── Запуск симуляції ──────────────────────────────────────────────────────

    private void startTest(ActionEvent e) {
        getDiagramQueueToCashier().clear();
        getDiagramCustomersInStore().clear();
        getDiagramLostCustomers().clear();
        getDiagramCashierLoad().clear();

        Dispatcher dispatcher = new Dispatcher();
        IModelFactory factory = (d) -> new Model(d, this);
        Model model = (Model) factory.createModel(dispatcher);

        getDiagramStartbutton().setEnabled(false);
        dispatcher.addDispatcherFinishListener(() -> SwingUtilities.invokeLater(() -> getDiagramStartbutton().setEnabled(true)));

        model.initForTest();
        dispatcher.start();
    }

    // ── Component initialization ──────────────────────────────────────────────

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        splitPane = new JSplitPane();
        leftSettingModelPanel = new JPanel();
        Title = new JLabel();
        chooseRandomCustomerArrival = new ChooseRandom();
        chooseRandomShoppingTime = new ChooseRandom();
        chooseRandomCashierService = new ChooseRandom();
        chooseDataCashiers = new ChooseData();
        chooseDataMaxQueueSize = new ChooseData();
        chooseRandomPurchasesPerCustomer = new ChooseRandom();
        chooseDataSimulationTime = new ChooseData();
        tabbedPane = new JTabbedPane();
        taskScrollPanel = new JScrollPane();
        textHtmTask = new JTextPane();
        testPanel = new JPanel();
        diagramQueueToCashier = new Diagram();
        diagramCustomersInStore = new Diagram();
        diagramLostCustomers = new Diagram();
        diagramCashierLoad = new Diagram();
        diagramInteractionPanel = new JPanel();
        consoleLoggerCheckBox = new JCheckBox();
        diagramStartbutton = new JButton();
        statPanel = new JPanel();
        regresPanel = new JPanel();
        transientPanel = new JPanel();
        infoPanel = new JPanel();
        photoPanel = new JPanel();
        textInfoAuthor = new JTextArea();

        //======== this ========
        setTitle("РГР. Варіант 19 – Моделювання роботи супермаркету");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== splitPane ========
        {
            //======== leftSettingModelPanel ========
            {
                leftSettingModelPanel.setLayout(new MigLayout(
                        "hidemode 3,aligny center",
                        "[262,fill]",
                        "[][][][][][][][][]"));

                //---- Title ----
                Title.setText("Параметри системи, що досліджується");
                Title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                leftSettingModelPanel.add(Title, "cell 0 0,alignx center,growx 0");

                //---- chooseRandomCustomerArrival ----
                chooseRandomCustomerArrival.setBorder(new CompoundBorder(
                        new EtchedBorder(),
                        new TitledBorder(LineBorder.createBlackLineBorder(),
                                "Інтервал приходу покупців", TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Segoe UI", Font.PLAIN, 14), Color.DARK_GRAY)));
                chooseRandomCustomerArrival.setRandom(new Negexp(10));
                leftSettingModelPanel.add(chooseRandomCustomerArrival, "cell 0 1,aligny center,growy 0");

                //---- chooseRandomShoppingTime ----
                chooseRandomShoppingTime.setBorder(new CompoundBorder(
                        new EtchedBorder(),
                        new TitledBorder(LineBorder.createBlackLineBorder(),
                                "Час перебування покупця в залі", TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Segoe UI", Font.PLAIN, 14), Color.DARK_GRAY)));
                chooseRandomShoppingTime.setRandom(new Norm(30, 8));
                leftSettingModelPanel.add(chooseRandomShoppingTime, "cell 0 2,aligny center,growy 0");

                //---- chooseRandomCashierService ----
                chooseRandomCashierService.setBorder(new CompoundBorder(
                        new EtchedBorder(),
                        new TitledBorder(LineBorder.createBlackLineBorder(),
                                "Час обслуговування касиром", TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Segoe UI", Font.PLAIN, 14), Color.DARK_GRAY)));
                chooseRandomCashierService.setRandom(new Norm(5, 2));
                leftSettingModelPanel.add(chooseRandomCashierService, "cell 0 3,aligny center,growy 0");

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
                leftSettingModelPanel.add(chooseDataCashiers, "cell 0 4,aligny center,growy 0");

                //---- chooseDataMaxQueueSize ----

                chooseDataMaxQueueSize.setTitle("Критичний розмір черги до каси");
                chooseDataMaxQueueSize.setBorder(new CompoundBorder(
                        new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),
                                "Критичний розмір черги до каси", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                                new Font("Dialog", Font.PLAIN, 14)),
                        new BevelBorder(BevelBorder.LOWERED)));
                chooseDataMaxQueueSize.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataMaxQueueSize.setMinimumSize(new Dimension(50, 55));
                chooseDataMaxQueueSize.addCaretListener(e -> chooseDataMaxQueueSizeCaretUpdate(e));
                chooseDataMaxQueueSize.setInt(5);
                leftSettingModelPanel.add(chooseDataMaxQueueSize, "cell 0 5,aligny center,growy 0");

                //---- chooseRandomPurchasesPerCustomer ----
                chooseRandomPurchasesPerCustomer.setBorder(new CompoundBorder(
                        new EtchedBorder(),
                        new TitledBorder(LineBorder.createBlackLineBorder(),
                                "Кількість покупок (Ерланг)", TitledBorder.CENTER, TitledBorder.TOP,
                                new Font("Segoe UI", Font.PLAIN, 14), Color.DARK_GRAY)));
                chooseRandomPurchasesPerCustomer.setRandom(new Erlang(5, 2, true));
                leftSettingModelPanel.add(chooseRandomPurchasesPerCustomer, "cell 0 6,aligny center,growy 0");

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
                leftSettingModelPanel.add(chooseDataSimulationTime, "cell 0 7,aligny center,growy 0");
            }
            splitPane.setLeftComponent(leftSettingModelPanel);

            //======== tabbedPane ========
            {
                tabbedPane.addChangeListener(e -> tabbedPaneStateChanged(e));

                //======== taskScrollPanel ========
                {
                    textHtmTask.setEditable(false);
                    textHtmTask.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    taskScrollPanel.setViewportView(textHtmTask);
                }
                tabbedPane.addTab("Завдання", taskScrollPanel);

                //======== testPanel ========
                {
                    testPanel.setLayout(new MigLayout(
                            "hidemode 3",
                            "[646,grow,fill]",
                            "[grow][grow]rel[grow][grow][][]"));

                    //---- diagramQueueToCashier ----
                    diagramQueueToCashier.setTitleText("Черга покупців до кас");
                    diagramQueueToCashier.setPanelBackground(Color.WHITE);
                    diagramQueueToCashier.setGridColor(new Color(0xCCCCCC));
                    diagramQueueToCashier.setPainterColor(new Color(0x3399ff));
                    diagramQueueToCashier.setGridByX(10);
                    testPanel.add(diagramQueueToCashier, "cell 0 0,grow");

                    //---- diagramCustomersInStore ----
                    diagramCustomersInStore.setTitleText("Покупці у торговельному залі");
                    diagramCustomersInStore.setPanelBackground(Color.WHITE);
                    diagramCustomersInStore.setGridColor(new Color(0xCCCCCC));
                    diagramCustomersInStore.setPainterColor(new Color(0x33cc33));
                    diagramCustomersInStore.setGridByX(10);
                    testPanel.add(diagramCustomersInStore, "cell 0 1,grow");

                    //---- diagramLostCustomers ----
                    diagramLostCustomers.setTitleText("Втрачені покупці (не зайшли до магазину)");
                    diagramLostCustomers.setPanelBackground(Color.WHITE);
                    diagramLostCustomers.setGridColor(new Color(0xCCCCCC));
                    diagramLostCustomers.setPainterColor(new Color(0xcc0000));
                    diagramLostCustomers.setGridByX(10);
                    testPanel.add(diagramLostCustomers, "cell 0 2,grow");

                    //---- diagramCashierLoad ----
                    diagramCashierLoad.setTitleText("Завантаженість касирів");
                    diagramCashierLoad.setPanelBackground(Color.WHITE);
                    diagramCashierLoad.setGridColor(new Color(0xCCCCCC));
                    diagramCashierLoad.setPainterColor(new Color(0xffaa00));
                    diagramCashierLoad.setGridByX(10);
                    testPanel.add(diagramCashierLoad, "cell 0 3,grow");

                    //======== diagramInteractionPanel ========
                    {
                        diagramInteractionPanel.setLayout(new BorderLayout());

                        //---- consoleLoggerCheckBox ----
                        consoleLoggerCheckBox.setText("Лог у консоль");
                        consoleLoggerCheckBox.setPreferredSize(new Dimension(200, 20));
                        consoleLoggerCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        diagramInteractionPanel.add(consoleLoggerCheckBox, BorderLayout.WEST);

                        //---- diagramStartbutton ----
                        diagramStartbutton.setText("Старт");
                        diagramStartbutton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                        diagramStartbutton.addActionListener(e -> startTest(e));
                        diagramInteractionPanel.add(diagramStartbutton, BorderLayout.EAST);
                    }
                    testPanel.add(diagramInteractionPanel, "cell 0 4");
                }
                JScrollPane testScrollPanel = new JScrollPane(testPanel);
                testScrollPanel.getVerticalScrollBar().setUnitIncrement(16);
                tabbedPane.addTab("Тест", testScrollPanel);

                //======== statPanel ========
                {
                    statPanel.setLayout(new MigLayout("hidemode 3", "[fill][fill]", "[][][]"));
                }
                tabbedPane.addTab("Статистика", statPanel);

                //======== regresPanel ========
                {
                    regresPanel.setLayout(new MigLayout("hidemode 3", "[fill][fill]", "[][][]"));
                }
                tabbedPane.addTab("Регресія", regresPanel);

                //======== transientPanel ========
                {
                    transientPanel.setLayout(new MigLayout("hidemode 3", "[fill][fill]", "[][][]"));
                }
                tabbedPane.addTab("Перехідний процес", transientPanel);

                //======== infoPanel ========
                {
                    infoPanel.setMinimumSize(new Dimension(887, 135));
                    infoPanel.setLayout(new MigLayout("hidemode 3", "[649,grow,fill]", "[319,grow][108]"));

                    //======== photoPanel ========
                    {
                        photoPanel = createPhotoPanel();
                        photoPanel.setPreferredSize(new Dimension(1500, 1500));
                        photoPanel.setMinimumSize(new Dimension(1500, 1500));
                        photoPanel.setLayout(null);

                        {
                            Dimension preferredSize = new Dimension();
                            for (int i = 0; i < photoPanel.getComponentCount(); i++) {
                                Rectangle bounds = photoPanel.getComponent(i).getBounds();
                                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                            }
                            Insets insets = photoPanel.getInsets();
                            preferredSize.width += insets.right;
                            preferredSize.height += insets.bottom;
                            photoPanel.setMinimumSize(preferredSize);
                            photoPanel.setPreferredSize(preferredSize);
                        }
                    }
                    infoPanel.add(photoPanel, "cell 0 0");

                    photoPanel.setPreferredSize(new Dimension(1500, 1500));
                    textInfoAuthor.setText(
                            "Автор проекту:\nСергієнко Юрій Сергійович,\nстудент 3 курсу, спеціальність Комп'ютерна інженерія\n" +
                                    "Варіант 19 – Моделювання роботи супермаркету");
                    textInfoAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    textInfoAuthor.setEditable(false);
                    infoPanel.add(textInfoAuthor, "cell 0 1");
                }
                tabbedPane.addTab("Інфо", infoPanel);
            }
            splitPane.setRightComponent(tabbedPane);
        }
        contentPane.add(splitPane, BorderLayout.CENTER);
        setSize(960, 665);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    private void loadHtmlTask() {
        String str = "/tz.htm";
        URL url = getClass().getResource(str);
        if (url != null) {
            try {
                textHtmTask.setPage(url);
            } catch (IOException e) {
                System.err.println("Problems with file " + str);
            }
        } else {
            System.err.println("File not found: " + str);
        }
    }

    private JPanel createPhotoPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                BufferedImage img;
                URL url = getClass().getResource("/foto.jpg");
                if (url != null) {
                    try {
                        img = ImageIO.read(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    double k = (double) img.getHeight() / img.getWidth();
                    int width = getWidth();
                    int height = getHeight();
                    if ((double) height / width > k)
                        height = (int) (width * k);
                    else
                        width = (int) (height / k);
                    Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    g2d.drawImage(scaledImg, 0, 0, null);
                } else {
                    System.err.println("Image not found");
                }
            }
        };
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JSplitPane splitPane;
    private JPanel leftSettingModelPanel;
    private JLabel Title;
    private ChooseRandom chooseRandomCustomerArrival;
    private ChooseRandom chooseRandomShoppingTime;
    private ChooseRandom chooseRandomCashierService;
    private ChooseData chooseDataCashiers;
    private ChooseData chooseDataMaxQueueSize;
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
    private JPanel regresPanel;
    private JPanel transientPanel;
    private JPanel infoPanel;
    private JPanel photoPanel;
    private JTextArea textInfoAuthor;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
