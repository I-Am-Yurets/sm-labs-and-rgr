/*
 * Created by JFormDesigner on Wed Feb 26 07:39:29 EET 2025
 */

package stu.cn.ua.lab1;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.*;
import net.miginfocom.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;

import stat.Histo;
import widgets.ChooseData;
import widgets.Diagram;

/**
 * @author I-Am-Yurets
 */
public class Main extends JFrame {
    private Histo histo;

    public Main() {
        initComponents();
        histo = new Histo();
        button.addActionListener(this::generateData);
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

    private void generateData(ActionEvent e) {
        String input = chooseData.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a sample size!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int sampleSize = Integer.parseInt(input);
            if (sampleSize <= 0) {
                JOptionPane.showMessageDialog(this, "Sample size must be a positive number!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            histo.initFromTo(0.0, 1.0, 10);
            for (int i = 0; i < sampleSize; i++) {
                histo.add(Math.random());
            }

            textArea.setText(histo.toString());
            SwingUtilities.invokeLater(() -> {
                histo.showRelFrec(diagram);
                diagram.repaint();
            });
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input! Please enter a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        diagram = new Diagram();
        scrollPane = new JScrollPane();
        textArea = new JTextArea();
        chooseData = new ChooseData();
        button = new JButton();

        //======== this ========
        setIconImage(null);
        setTitle("Laboratory work \u21161. Yurii Serhiienko, KI-231");
        setResizable(false);
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new MigLayout(
                        "insets dialog,hidemode 3",
                        // columns
                        "[496,fill]" +
                                "[339,fill]",
                        // rows
                        "[374]" +
                                "[]" +
                                "[]" +
                                "[]" +
                                "[]" +
                                "[45]" +
                                "[]" +
                                "[]" +
                                "[]" +
                                "[]" +
                                "[0]"));

                //---- diagram ----
                diagram.setTitleText("Histogram");
                diagram.setMinimumSize(new Dimension(470, 380));
                contentPanel.add(diagram, "cell 0 0,align center center,grow 0 0");

                //======== scrollPane ========
                {
                    scrollPane.setMinimumSize(new Dimension(18, 405));
                    scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                    scrollPane.setViewportBorder(new MatteBorder(1, 1, 1, 1, Color.black));

                    //---- textArea ----
                    textArea.setMinimumSize(new Dimension(62, 100));
                    textArea.setEditable(false);
                    textArea.setLineWrap(true);
                    textArea.setWrapStyleWord(true);
                    textArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                    scrollPane.setViewportView(textArea);
                }
                contentPanel.add(scrollPane, "cell 1 0,aligny top,grow 100 0");

                //---- chooseData ----
                chooseData.setBackground(new Color(0x3c3f41));
                chooseData.setTitle("Sample size");
                chooseData.setMinimumSize(new Dimension(100, 50));
                contentPanel.add(chooseData, "cell 0 2 1 3");

                //---- button ----
                button.setText("Start");
                button.setFont(button.getFont().deriveFont(button.getFont().getSize() + 1f));
                button.setMinimumSize(new Dimension(20, 26));
                contentPanel.add(button, "cell 0 2 1 3,align center center,grow 0 0");
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        setSize(855, 530);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel dialogPane;
    private JPanel contentPanel;
    private Diagram diagram;
    private JScrollPane scrollPane;
    private JTextArea textArea;
    private ChooseData chooseData;
    private JButton button;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}