package yourpackage.app;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import yourpackage.gauges.CircleGauge;
import yourpackage.gauges.Gauge;
import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
import yourpackage.parsing.TimeDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GaugeCreator {
    private JPanel mainPanelGC;
    private JComboBox gaugeTypeComboBox;
    private JTextField blueRangeTextField;
    private JTextField greenRangeTextField;
    private JTextField yellowRangeTextField;
    private JTextField redRangeTextField;
    private JTextField gaugeNameTextField;
    private JList fieldsJList;
    private JButton doneButton;
    private JCheckBox imperialUnitsCheckBox;
    private JButton createGaugeButton;
    private JCheckBox unitsCheckBox;
    private JLabel maxValueJLabel;
    private JComboBox unitComboBox;

    private final JFrame frame;

    private ArrayList<DataField> Fields;

    int blueRange;
    int greenRange;
    int yellowRange;
    int redRange;
    String gaugeName;
    String gaugeType;
    String unit;

    DataField selectedField;

    NumericDataField selectedNumericField;
    NumericDataField convertedNumericField;

    Boolean switchUnits;
    double selectedNumericFieldMaxValue;
    double maxValueMetric;

    VideoPlayerSwingIntegration videoPlayer;

    public GaugeCreator(ArrayList<DataField> inputFields, VideoPlayerSwingIntegration vp) {
        frame = new JFrame();
        $$$setupUI$$$();
        String iconPath = System.getProperty("user.dir") + "/src/main/resources/drone.png";
        ImageIcon img = new ImageIcon(iconPath);
        Fields = new ArrayList<>();
        frame.setIconImage(img.getImage()); // Get and set a custom icon for the GUI.
        frame.setContentPane(mainPanelGC);
        frame.setDefaultCloseOperation(0); // Window shouldn't be closeable, or else it will mess up the rest of the program's execution.
        frame.pack();
        frame.setResizable(true);
        frame.setTitle("Gauge Creator");
        frame.setVisible(true);
        Fields.addAll(inputFields);
        populateFieldsJList();
        videoPlayer = vp;
        System.out.println("videoPlaying() called from GaugeCreator. Value: " + videoPlayer.isPlaying());

        doneButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispose(); // Close the window when the user is done.
            }
        });
        createGaugeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int selectedFieldIndex = fieldsJList.getSelectedIndex();
                selectedField = Fields.get(selectedFieldIndex);

                String temp = blueRangeTextField.getText();
                if (temp.length() > 1) {
                    blueRange = Integer.parseInt(temp);
                } else {
                    blueRange = 0;
                }

                temp = greenRangeTextField.getText();
                if (temp.length() > 1) {
                    greenRange = Integer.parseInt(temp);
                } else {
                    greenRange = 0;
                }

                temp = yellowRangeTextField.getText();
                if (temp.length() > 1) {
                    yellowRange = Integer.parseInt(temp);
                } else {
                    yellowRange = 0;
                }

                temp = redRangeTextField.getText();
                if (temp.length() > 1) {
                    redRange = Integer.parseInt(temp);
                } else {
                    redRange = 0;
                }

                temp = gaugeNameTextField.getText();
                if (temp.length() > 1) {
                    gaugeName = temp;
                } else {
                    gaugeName = fieldsJList.getSelectedValue().toString();
                }

                gaugeType = gaugeTypeComboBox.getSelectedItem().toString();

                Boolean temp2 = unitsCheckBox.isSelected();

                if (temp2 == true) {
                    switchUnits = true;
                    convertedNumericField = selectedNumericField.copyDataField();
                    convertedNumericField.changeUnit();
                } else {
                    switchUnits = false;
                }

                createGauge();
            }
        });
        fieldsJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                gaugeNameTextField.setEnabled(true);
                gaugeTypeComboBox.setEnabled(true);
                createGaugeButton.setEnabled(true);
                clearTextFields();
                unitsCheckBox.setSelected(false);
                switchUnits = false;

                int selectedFieldIndex = fieldsJList.getSelectedIndex();
                selectedField = Fields.get(selectedFieldIndex);

                System.out.println(selectedField.getFieldName());
                if (selectedField instanceof NumericDataField) {
                    System.out.println("Numeric Data Field.");
                    enableRangeTextFields();
                    unitsCheckBox.setEnabled(true);
                    setNumericListModel();
                    selectedNumericField = (NumericDataField) selectedField;
                    selectedNumericFieldMaxValue = selectedNumericField.getMaximum();
                    maxValueJLabel.setText("Max Value of Field: " + selectedNumericFieldMaxValue);
                } else if (selectedField instanceof TimeDataField) {
                    System.out.println("Time Field.");
                    disableRangeTextFields();
                    disableUnitsCheckbox();
                    setTimeListModel();
                    selectedNumericField = null;
                    maxValueJLabel.setText("Max Value of Field: NumericField not selected.");
                } else {
                    System.out.println("String Field.");
                    disableRangeTextFields();
                    disableUnitsCheckbox();
                    setStringListModel();
                    selectedNumericField = null;
                    maxValueJLabel.setText("Max Value of Field: NumericField not selected.");
                }
            }
        });
        unitsCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (unitsCheckBox.isSelected()) {
                    convertToImperial();
                } else {
                    revertToMetric();
                }
            }
        });
    }

    private void populateFieldsJList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField item : Fields) {
            listModel.addElement(String.valueOf(item));
        }
        fieldsJList.setModel(listModel);
    }

    private void createGauge() {
        if (gaugeType.equals("Circle 90")) {
            if (switchUnits == true) {
                Gauge circle90 = new CircleGauge(90, gaugeName, convertedNumericField, videoPlayer);
            } else {
                Gauge circle90 = new CircleGauge(90, gaugeName, selectedNumericField, videoPlayer);
            }
        } else if (gaugeType.equals("Circle 180")) {
            if (switchUnits == true) {
                Gauge circle180 = new CircleGauge(180, gaugeName, convertedNumericField, videoPlayer);
            } else {
                Gauge circle180 = new CircleGauge(180, gaugeName, selectedNumericField, videoPlayer);
            }
        } else if (gaugeType.equals("Circle 270")) {
            if (switchUnits == true) {
                Gauge circle180 = new CircleGauge(270, gaugeName, convertedNumericField, videoPlayer);
            } else {
                Gauge circle180 = new CircleGauge(270, gaugeName, selectedNumericField, videoPlayer);
            }
        } else if (gaugeType.equals("Circle 360")) {
            if (switchUnits == true) {
                Gauge circle180 = new CircleGauge(360, gaugeName, convertedNumericField, videoPlayer);
            } else {
                Gauge circle180 = new CircleGauge(360, gaugeName, selectedNumericField, videoPlayer);
            }
        }
    }

    private void enableRangeTextFields() {
        blueRangeTextField.setEnabled(true);
        greenRangeTextField.setEnabled(true);
        yellowRangeTextField.setEnabled(true);
        redRangeTextField.setEnabled(true);
    }

    private void disableRangeTextFields() {
        blueRangeTextField.setEnabled(false);
        greenRangeTextField.setEnabled(false);
        yellowRangeTextField.setEnabled(false);
        redRangeTextField.setEnabled(false);
    }

    private void disableUnitsCheckbox() {
        unitsCheckBox.setSelected(false);
        unitsCheckBox.setEnabled(false);
    }

    private void clearTextFields() {
        blueRangeTextField.setText("");
        greenRangeTextField.setText("");
        yellowRangeTextField.setText("");
        redRangeTextField.setText("");
        gaugeNameTextField.setText("");
    }

    private void setNumericListModel() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Circle 90");
        comboBoxModel.addElement("Circle 180");
        comboBoxModel.addElement("Circle 270");
        comboBoxModel.addElement("Circle 360");
        comboBoxModel.addElement("Bar");
        comboBoxModel.addElement("x-plot");
        comboBoxModel.addElement("x by plot");
        comboBoxModel.addElement("Number/character display");
        comboBoxModel.addElement("Text display");
        gaugeTypeComboBox.setModel(comboBoxModel);
    }

    private void setStringListModel() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Number/character display");
        comboBoxModel.addElement("Text display");
        comboBoxModel.addElement("On/off light");
        gaugeTypeComboBox.setModel(comboBoxModel);
    }

    private void setTimeListModel() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Number/character display");
        comboBoxModel.addElement("Text display");
        comboBoxModel.addElement("Stopwatch");
        gaugeTypeComboBox.setModel(comboBoxModel);
    }

    private void convertToImperial() {
        maxValueMetric = selectedNumericFieldMaxValue;
        if (selectedNumericField.getUnit().equals("[m/s]")) {
            selectedNumericFieldMaxValue = (selectedNumericFieldMaxValue * 3600) / 1609.3; //compute to mph
        } else if (selectedNumericField.getUnit().equals("[m]")) {
            selectedNumericFieldMaxValue = selectedNumericFieldMaxValue * 3.28084; //compute to ft
        }

        System.out.println(selectedNumericFieldMaxValue);
        maxValueJLabel.setText("Max Value of Field: " + selectedNumericFieldMaxValue);
    }

    private void revertToMetric() {
        selectedNumericFieldMaxValue = maxValueMetric;
        maxValueJLabel.setText("Max Value of Field: " + selectedNumericFieldMaxValue);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanelGC = new JPanel();
        mainPanelGC.setLayout(new GridLayoutManager(9, 3, new Insets(0, 0, 0, 60), -1, -1));
        mainPanelGC.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Yellow Range");
        mainPanelGC.add(label1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Green Range");
        mainPanelGC.add(label2, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Blue Range");
        mainPanelGC.add(label3, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Red Range");
        mainPanelGC.add(label4, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Name (Optional)");
        mainPanelGC.add(label5, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Gauge Type");
        mainPanelGC.add(label6, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Data Fields");
        mainPanelGC.add(label7, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createGaugeButton = new JButton();
        createGaugeButton.setEnabled(false);
        createGaugeButton.setText("Create Gauge");
        mainPanelGC.add(createGaugeButton, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        redRangeTextField = new JTextField();
        redRangeTextField.setEnabled(false);
        mainPanelGC.add(redRangeTextField, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        yellowRangeTextField = new JTextField();
        yellowRangeTextField.setEnabled(false);
        mainPanelGC.add(yellowRangeTextField, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        greenRangeTextField = new JTextField();
        greenRangeTextField.setEnabled(false);
        mainPanelGC.add(greenRangeTextField, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        doneButton = new JButton();
        doneButton.setText("Done");
        mainPanelGC.add(doneButton, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        gaugeNameTextField = new JTextField();
        gaugeNameTextField.setEnabled(false);
        mainPanelGC.add(gaugeNameTextField, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        blueRangeTextField = new JTextField();
        blueRangeTextField.setEnabled(false);
        mainPanelGC.add(blueRangeTextField, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        gaugeTypeComboBox = new JComboBox();
        gaugeTypeComboBox.setEnabled(false);
        mainPanelGC.add(gaugeTypeComboBox, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Switch Units");
        mainPanelGC.add(label8, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanelGC.add(scrollPane1, new GridConstraints(1, 0, 7, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldsJList = new JList();
        fieldsJList.setSelectionMode(0);
        scrollPane1.setViewportView(fieldsJList);
        unitsCheckBox = new JCheckBox();
        unitsCheckBox.setEnabled(false);
        unitsCheckBox.setText("");
        mainPanelGC.add(unitsCheckBox, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxValueJLabel = new JLabel();
        maxValueJLabel.setText("Max Value of Field: NumericField not selected. ");
        mainPanelGC.add(maxValueJLabel, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanelGC;
    }

}
