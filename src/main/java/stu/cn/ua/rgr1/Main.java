/*
 * Created by JFormDesigner on Wed Feb 26 07:39:29 EET 2025
 */

package stu.cn.ua.rgr1;

import javax.imageio.ImageIO;
import javax.swing.border.*;
import com.formdev.flatlaf.FlatDarculaLaf;
import net.miginfocom.swing.MigLayout;
import widgets.*;
import widgets.ChooseData;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * Варіант 19 – Моделювання роботи супермаркету (SuperMarket)
 *
 * Покупці приходять у магазин через випадкові проміжки часу.
 * У магазині покупець може зробити кілька покупок.
 * Покупець розраховується за покупки в касах на виході.
 * Час розрахунку на касі й час перебування покупця в магазині залежить від кількості покупок.
 * Якщо черга перевищує критичне значення (випадкова величина),
 * то покупець не заходить до магазину.
 * Кількість покупців, що втрачено за одиницю часу теж обчислювати.
 *
 * Активні абстракції моделі:
 *   Customer    – покупець (приходить через випадковий інтервал, ходить по магазину,
 *                           стає у чергу до каси, розраховується)
 *   Cashier     – касир (обслуговує покупців у черзі)
 *   StoreWorker – продавець / персонал магазину (формує полиці, може блокуватись
 *                                                   при переповненні торговельної зали)
 *   Manager     – менеджер (керує критичним рівнем черги; при переповненні зали
 *                            покупець відмовляється від входу)
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
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            Main frame = new Main();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

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
        chooseDataMaxCustomersInStore = new ChooseData();
        chooseDataPurchasesPerCustomer = new ChooseData();
        tabbedPane = new JTabbedPane();
        taskScrollPanel = new JScrollPane();
        textHtmTask = new JTextPane();
        testPanel = new JPanel();
        statPanel = new JPanel();
        regresPanel = new JPanel();
        transientPanel = new JPanel();
        infoPanel = new JPanel();
        photoPanel = new JPanel();
        textInfoAuthor = new JTextArea();

        //======== this ========
        setTitle("Розрахунково-графічна робота. Варіант 19 – Супермаркет");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== splitPane ========
        {
            //======== leftSettingModelPanel ========
            {
                leftSettingModelPanel.setLayout(new MigLayout(
                    "hidemode 3,aligny center",
                    // columns
                    "[262,fill]",
                    // rows
                    "[][][][] [][][][] []"));

                //---- Title ----
                Title.setText("Параметри системи, що досліджується");
                Title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                leftSettingModelPanel.add(Title, "cell 0 0,alignx center,growx 0");

                //---- chooseRandomCustomerArrival ----
                chooseRandomCustomerArrival.setBorder(new CompoundBorder(
                    new EtchedBorder(),
                    new TitledBorder(LineBorder.createBlackLineBorder(),
                        "Інтервал приходу покупців", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
                leftSettingModelPanel.add(chooseRandomCustomerArrival, "cell 0 1,aligny center,growy 0");

                //---- chooseRandomShoppingTime ----
                chooseRandomShoppingTime.setBorder(new CompoundBorder(
                    new EtchedBorder(),
                    new TitledBorder(LineBorder.createBlackLineBorder(),
                        "Час перебування покупця в залі", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
                leftSettingModelPanel.add(chooseRandomShoppingTime, "cell 0 2,aligny center,growy 0");

                //---- chooseRandomCashierService ----
                chooseRandomCashierService.setBorder(new CompoundBorder(
                    new EtchedBorder(),
                    new TitledBorder(LineBorder.createBlackLineBorder(),
                        "Час обслуговування касиром", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
                leftSettingModelPanel.add(chooseRandomCashierService, "cell 0 3,aligny center,growy 0");

                //---- chooseDataCashiers ----
                chooseDataCashiers.setBackground(new Color(0x3c3f41));
                chooseDataCashiers.setTitle("Кількість кас (касирів)");
                chooseDataCashiers.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),
                        "Кількість кас (касирів)", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font("Dialog", Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                chooseDataCashiers.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataCashiers.setMinimumSize(new Dimension(50, 55));
                leftSettingModelPanel.add(chooseDataCashiers, "cell 0 4,aligny center,growy 0");

                //---- chooseDataMaxQueueSize ----
                chooseDataMaxQueueSize.setBackground(new Color(0x3c3f41));
                chooseDataMaxQueueSize.setTitle("Критичний розмір черги до каси");
                chooseDataMaxQueueSize.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),
                        "Критичний розмір черги до каси", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font("Dialog", Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                chooseDataMaxQueueSize.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataMaxQueueSize.setMinimumSize(new Dimension(50, 55));
                leftSettingModelPanel.add(chooseDataMaxQueueSize, "cell 0 5,aligny center,growy 0");

                //---- chooseDataMaxCustomersInStore ----
                chooseDataMaxCustomersInStore.setBackground(new Color(0x3c3f41));
                chooseDataMaxCustomersInStore.setTitle("Макс. кількість покупців у залі");
                chooseDataMaxCustomersInStore.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),
                        "Макс. кількість покупців у залі", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font("Dialog", Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                chooseDataMaxCustomersInStore.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataMaxCustomersInStore.setMinimumSize(new Dimension(50, 55));
                leftSettingModelPanel.add(chooseDataMaxCustomersInStore, "cell 0 6,aligny center,growy 0");

                //---- chooseDataPurchasesPerCustomer ----
                chooseDataPurchasesPerCustomer.setBackground(new Color(0x3c3f41));
                chooseDataPurchasesPerCustomer.setTitle("Середня кількість покупок");
                chooseDataPurchasesPerCustomer.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED),
                        "Середня кількість покупок", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font("Dialog", Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                chooseDataPurchasesPerCustomer.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataPurchasesPerCustomer.setMinimumSize(new Dimension(50, 55));
                leftSettingModelPanel.add(chooseDataPurchasesPerCustomer, "cell 0 7,aligny center,growy 0");
            }
            splitPane.setLeftComponent(leftSettingModelPanel);

            //======== tabbedPane ========
            {
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
                        "[fill][fill]",
                        "[][][]"));
                }
                tabbedPane.addTab("Тест", testPanel);

                //======== statPanel ========
                {
                    statPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        "[fill][fill]",
                        "[][][]"));
                }
                tabbedPane.addTab("Статистика", statPanel);

                //======== regresPanel ========
                {
                    regresPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        "[fill][fill]",
                        "[][][]"));
                }
                tabbedPane.addTab("Регресія", regresPanel);

                //======== transientPanel ========
                {
                    transientPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        "[fill][fill]",
                        "[][][]"));
                }
                tabbedPane.addTab("Перехідний процес", transientPanel);

                //======== infoPanel ========
                {
                    infoPanel.setMinimumSize(new Dimension(887, 135));
                    infoPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        "[649,grow,fill]",
                        "[319,grow][108]"));

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

                    //---- textInfoAuthor ----
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
        setSize(900, 580);
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
    private ChooseData chooseDataMaxCustomersInStore;
    private ChooseData chooseDataPurchasesPerCustomer;
    private JTabbedPane tabbedPane;
    private JScrollPane taskScrollPanel;
    private JTextPane textHtmTask;
    private JPanel testPanel;
    private JPanel statPanel;
    private JPanel regresPanel;
    private JPanel transientPanel;
    private JPanel infoPanel;
    private JPanel photoPanel;
    private JTextArea textInfoAuthor;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
