package yourpackage.app;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import yourpackage.parsing.StaticSelectedFieldsArrayList;

public class StatisticsWindow {

    private JPanel mainPanelSW;
    private JList selectedFieldsJList;
    private JList minimumValuesJList;
    private JList maximumValuesJList;
    private JList averageValuesJList;
    private JList stdDevValuesJList;
    private JButton closeButton;
    private JLabel emptyFieldsLabel;
    private final JFrame frame;

    public StatisticsWindow() {
        frame = new JFrame();
        String iconPath = System.getProperty("user.dir") + "/src/main/resources/drone.png";
        ImageIcon img = new ImageIcon(iconPath);
        frame.setIconImage(img.getImage()); // Get and set a custom icon for the GUI.
        frame.setContentPane(mainPanelSW);
        frame.setDefaultCloseOperation(0); // Window shouldn't be closeable, or else it will mess up the rest of the program's execution.
        frame.pack();
        frame.setResizable(false);
        frame.setTitle("Data Statistics");
        frame.setVisible(true);

        selectedFieldsJList.setVisible(false);
        minimumValuesJList.setVisible(false);
        maximumValuesJList.setVisible(false);
        averageValuesJList.setVisible(false);
        stdDevValuesJList.setVisible(false);


        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
    }

    private void populateSelectedFieldsJList() {
        selectedFieldsJList.setVisible(true);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField df : StaticSelectedFieldsArrayList.getFields()) {
            if (df instanceof NumericDataField) {
                listModel.addElement(String.valueOf(df.getFieldName()));
            }
        }
        selectedFieldsJList.setModel(listModel);
    }

    private void populateMinimumValuesJList() {
        minimumValuesJList.setVisible(true);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField df : StaticSelectedFieldsArrayList.getFields()) {
            if (df instanceof NumericDataField) {
                listModel.addElement(String.valueOf(((NumericDataField) df).getMinimum()));
            } else
                listModel.addElement("0.0");
        }
        minimumValuesJList.setModel(listModel);
    }

    private void populateMaximumValuesJList() {
        maximumValuesJList.setVisible(true);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField df : StaticSelectedFieldsArrayList.getFields()) {
            if (df instanceof NumericDataField) {
                listModel.addElement(String.valueOf(((NumericDataField) df).getMaximum()));
            } else
                listModel.addElement("0.0");
        }
        maximumValuesJList.setModel(listModel);
    }

    private void populateAverageValuesJList() {
        averageValuesJList.setVisible(true);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField df : StaticSelectedFieldsArrayList.getFields()) {
            if (df instanceof NumericDataField) {
                Double temp = ((NumericDataField) df).getAverage();
                DecimalFormat d = new DecimalFormat("#.###");
                d.setRoundingMode(RoundingMode.CEILING);
                listModel.addElement((d.format(temp)));
            } else
                listModel.addElement("0.0");
        }
        averageValuesJList.setModel(listModel);
    }

    private void populateStdDevValuesJList() {
        stdDevValuesJList.setVisible(true);
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField df : StaticSelectedFieldsArrayList.getFields()) {
            if (df instanceof NumericDataField) {
                Double temp = ((NumericDataField) df).getStdDev();
                DecimalFormat d = new DecimalFormat("#.###");
                d.setRoundingMode(RoundingMode.CEILING);
                listModel.addElement((d.format(temp)));
            } else
                listModel.addElement("0.0");
        }
        stdDevValuesJList.setModel(listModel);
    }

    public void populateStatistics() {
        if (!StaticSelectedFieldsArrayList.getFields().isEmpty()) {
            emptyFieldsLabel.setVisible(false);
            populateSelectedFieldsJList();
            populateMinimumValuesJList();
            populateMaximumValuesJList();
            populateAverageValuesJList();
            populateStdDevValuesJList();
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanelSW = new JPanel();
        mainPanelSW.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 7, new Insets(0, 50, 150, 375), -1, -1));
        mainPanelSW.add(panel1, BorderLayout.CENTER);
        final JLabel label1 = new JLabel();
        label1.setText("Selected Field");
        panel1.add(label1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Minimum");
        panel1.add(label2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Maximum");
        panel1.add(label3, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Average");
        panel1.add(label4, new GridConstraints(0, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Standard Deviation");
        panel1.add(label5, new GridConstraints(0, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectedFieldsJList = new JList();
        selectedFieldsJList.setVisible(false);
        panel1.add(selectedFieldsJList, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 25), null, 0, false));
        minimumValuesJList = new JList();
        minimumValuesJList.setVisible(false);
        panel1.add(minimumValuesJList, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(100, 25), null, 0, false));
        maximumValuesJList = new JList();
        maximumValuesJList.setVisible(false);
        panel1.add(maximumValuesJList, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(100, 25), null, 0, false));
        averageValuesJList = new JList();
        averageValuesJList.setVisible(false);
        panel1.add(averageValuesJList, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 25), null, 0, false));
        stdDevValuesJList = new JList();
        stdDevValuesJList.setVisible(false);
        panel1.add(stdDevValuesJList, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 25), null, 0, false));
        closeButton = new JButton();
        closeButton.setText("Close");
        panel1.add(closeButton, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(2, 4, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(2, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        panel1.add(spacer4, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        panel1.add(spacer5, new GridConstraints(3, 0, 1, 7, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        emptyFieldsLabel = new JLabel();
        emptyFieldsLabel.setForeground(new Color(-1744795));
        emptyFieldsLabel.setText("No statistics to display - no fields selected.");
        panel1.add(emptyFieldsLabel, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer6 = new Spacer();
        panel1.add(spacer6, new GridConstraints(4, 4, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer7 = new Spacer();
        panel1.add(spacer7, new GridConstraints(4, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer8 = new Spacer();
        panel1.add(spacer8, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanelSW;
    }

}
