/*
 * Created by JFormDesigner on Wed Feb 26 07:39:29 EET 2025
 */

package stu.cn.ua.rgr1;

import javax.imageio.ImageIO;
import javax.swing.border.*;
import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import widgets.*;
import widgets.ChooseData;
import rnd.Erlang;

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

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        splitPane = new JSplitPane();
        leftSettingModelPanel = new JPanel();
        Title = new JLabel();
        chooseRandomExcavator = new ChooseRandom();
        chooseRandomCrusher = new ChooseRandom();
        chooseRandomLoader = new ChooseRandom();
        chooseDataDumpTrucks = new ChooseData();
        chooseDataTransportTime = new ChooseData();
        chooseDataUnloadTimeBunker = new ChooseData();
        chooseDataUnloadTimeSite = new ChooseData();
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
        setTitle("Computational-graphical work. Denys Lysenok, KI-221");
        setResizable(false);
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
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]" +
                    "[]"));

                //---- Title ----
                Title.setText("Parameters of the system under study ");
                Title.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                leftSettingModelPanel.add(Title, "cell 0 0,alignx center,growx 0");

                //---- chooseRandomExcavator ----
                chooseRandomExcavator.setBorder(new CompoundBorder(
                    new EtchedBorder(),
                    new TitledBorder(LineBorder.createBlackLineBorder(), "Excavator productivity", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
                leftSettingModelPanel.add(chooseRandomExcavator, "cell 0 1,aligny center,growy 0");

                //---- chooseRandomCrusher ----
                chooseRandomCrusher.setBorder(new CompoundBorder(
                    new EtchedBorder(),
                    new TitledBorder(LineBorder.createBlackLineBorder(), "Stone crusher productivity", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
                leftSettingModelPanel.add(chooseRandomCrusher, "cell 0 2,aligny center,growy 0");

                //---- chooseRandomLoader ----
                chooseRandomLoader.setBorder(new CompoundBorder(
                    new EtchedBorder(),
                    new TitledBorder(LineBorder.createBlackLineBorder(), "Forklift productivity", TitledBorder.CENTER, TitledBorder.TOP,
                        new Font("Segoe UI", Font.PLAIN, 14), Color.lightGray)));
                leftSettingModelPanel.add(chooseRandomLoader, "cell 0 3,aligny center,growy 0");

                //---- chooseDataDumpTrucks ----
                chooseDataDumpTrucks.setBackground(new Color(0x3c3f41));
                chooseDataDumpTrucks.setTitle("Number of dumpers");
                chooseDataDumpTrucks.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataDumpTrucks.setMinimumSize(new Dimension(50, 55));
                chooseDataDumpTrucks.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Number of dumpers", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font(Font.DIALOG, Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                leftSettingModelPanel.add(chooseDataDumpTrucks, "cell 0 4,aligny center,growy 0");

                //---- chooseDataTransportTime ----
                chooseDataTransportTime.setBackground(new Color(0x3c3f41));
                chooseDataTransportTime.setTitle("Ore transport time by dumper truck");
                chooseDataTransportTime.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataTransportTime.setMinimumSize(new Dimension(50, 55));
                chooseDataTransportTime.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Ore transport time by dumper truck", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font(Font.DIALOG, Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                leftSettingModelPanel.add(chooseDataTransportTime, "cell 0 5,aligny center,growy 0");

                //---- chooseDataUnloadTimeBunker ----
                chooseDataUnloadTimeBunker.setBackground(new Color(0x3c3f41));
                chooseDataUnloadTimeBunker.setTitle("Ore unloading time in the hopper");
                chooseDataUnloadTimeBunker.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataUnloadTimeBunker.setMinimumSize(new Dimension(50, 55));
                chooseDataUnloadTimeBunker.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Ore unloading time in the hopper", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font(Font.DIALOG, Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                leftSettingModelPanel.add(chooseDataUnloadTimeBunker, "cell 0 6,aligny center,growy 0");

                //---- chooseDataUnloadTimeSite ----
                chooseDataUnloadTimeSite.setBackground(new Color(0x3c3f41));
                chooseDataUnloadTimeSite.setTitle("Ore unloading time at the site");
                chooseDataUnloadTimeSite.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                chooseDataUnloadTimeSite.setMinimumSize(new Dimension(50, 55));
                chooseDataUnloadTimeSite.setBorder(new CompoundBorder(
                    new TitledBorder(new EtchedBorder(EtchedBorder.RAISED), "Ore unloading time at the site", TitledBorder.CENTER, TitledBorder.BELOW_TOP,
                        new Font(Font.DIALOG, Font.PLAIN, 14)),
                    new BevelBorder(BevelBorder.LOWERED)));
                leftSettingModelPanel.add(chooseDataUnloadTimeSite, "cell 0 7,aligny center,growy 0");
            }
            splitPane.setLeftComponent(leftSettingModelPanel);

            //======== tabbedPane ========
            {

                //======== taskScrollPanel ========
                {

                    //---- textHtmTask ----
                    textHtmTask.setEditable(false);
                    textHtmTask.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    taskScrollPanel.setViewportView(textHtmTask);
                }
                tabbedPane.addTab("Task", taskScrollPanel);

                //======== testPanel ========
                {
                    testPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));
                }
                tabbedPane.addTab("Test", testPanel);

                //======== statPanel ========
                {
                    statPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));
                }
                tabbedPane.addTab("Stat", statPanel);

                //======== regresPanel ========
                {
                    regresPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));
                }
                tabbedPane.addTab("Regres", regresPanel);

                //======== transientPanel ========
                {
                    transientPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[fill]" +
                        "[fill]",
                        // rows
                        "[]" +
                        "[]" +
                        "[]"));
                }
                tabbedPane.addTab("Transient", transientPanel);

                //======== infoPanel ========
                {
                    infoPanel.setMinimumSize(new Dimension(887, 135));
                    infoPanel.setLayout(new MigLayout(
                        "hidemode 3",
                        // columns
                        "[649,fill]",
                        // rows
                        "[319]" +
                        "[108]"));

                    //======== photoPanel ========
                    {
                        photoPanel.setPreferredSize(new Dimension(500, 500));
                        photoPanel.setLayout(null);

                        {
                            // compute preferred size
                            Dimension preferredSize = new Dimension();
                            for(int i = 0; i < photoPanel.getComponentCount(); i++) {
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

                    //---- textInfoAuthor ----
                    textInfoAuthor.setText("Author of the project:\nLysenok Denys Vitaliiovych,\n3rd year student majoring in Computer Engineering\nEmail: cerobreath@gmail.com\nGitHub: cerobreath");
                    textInfoAuthor.setFont(new Font("Segoe UI", Font.PLAIN, 16));
                    textInfoAuthor.setEditable(false);
                    infoPanel.add(textInfoAuthor, "cell 0 1");
                }
                tabbedPane.addTab("Info", infoPanel);
            }
            splitPane.setRightComponent(tabbedPane);
        }
        contentPane.add(splitPane, BorderLayout.CENTER);
        setSize(1406, 859);
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
    private ChooseRandom chooseRandomExcavator;
    private ChooseRandom chooseRandomCrusher;
    private ChooseRandom chooseRandomLoader;
    private ChooseData chooseDataDumpTrucks;
    private ChooseData chooseDataTransportTime;
    private ChooseData chooseDataUnloadTimeBunker;
    private ChooseData chooseDataUnloadTimeSite;
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
