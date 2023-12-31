package yourpackage.app;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import yourpackage.gauges.*;
import yourpackage.parsing.BooleanDataField;
import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
import yourpackage.parsing.TimeDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GaugeCreator {
    private JPanel mainPanelGC;
    private JComboBox gaugeTypeComboBox;
    private JTextField blueMinRangeTextField;
    private JTextField greenMinRangeTextField;
    private JTextField yellowMinRangeTextField;
    private JTextField redMinRangeTextField;

    private JTextField gaugeNameTextField;
    private JList fieldsJList;
    private JButton doneButton;
    private JCheckBox imperialUnitsCheckBox;
    private JButton createGaugeButton;
    private JCheckBox unitsCheckBox;
    private JLabel maxValueJLabel;
    private JTextField blueMaxRangeTextField;
    private JTextField greenMaxRangeTextField;
    private JTextField yellowMaxRangeTextField;
    private JTextField redMaxRangeTextField;
    private JComboBox yFieldComboBox;
    private final JFrame frame;
    private ArrayList<DataField> Fields;
    private ArrayList<NumericDataField> yFields;
    private double minBlueRange, maxBlueRange, minGreenRange, maxGreenRange, minYellowRange, maxYellowRange, minRedRange, maxRedRange;
    private boolean minBlueRangeSet, maxBlueRangeSet, minGreenRangeSet, maxGreenRangeSet, minYellowRangeSet, maxYellowRangeSet, minRedRangeSet, maxRedRangeSet = false;
    private String gaugeName;
    private String gaugeType;
    private String unit;
    private DataField selectedField;
    private NumericDataField selectedNumericField;
    private NumericDataField convertedNumericField;
    private NumericDataField yField;
    private NumericDataField convertedYField;
    private Boolean switchUnits;
    private double selectedNumericFieldMaxValue;
    private double maxValueMetric;
    private double frequency;
    private VideoPlayerSwingIntegration videoPlayer;
    private String fieldType;

    public GaugeCreator(ArrayList<DataField> inputFields, VideoPlayerSwingIntegration vp, double dataFrequency) {

        frame = new JFrame();
        $$$setupUI$$$();
        String iconPath = System.getProperty("user.dir") + "/src/main/resources/drone.png";
        ImageIcon img = new ImageIcon(iconPath);
        Fields = new ArrayList<>();
        yFields = new ArrayList<>();
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
        frequency = dataFrequency;

        for (DataField f : Fields) {
            if (f instanceof NumericDataField) {
                NumericDataField temp = (NumericDataField) f;
                yFields.add(temp);
            }
            System.out.println(yFields);
        }


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
                Boolean unitsCheckBoxSelected = unitsCheckBox.isSelected();

                if (unitsCheckBoxSelected == true) {
                    switchUnits = true;
                    convertedNumericField = selectedNumericField.copyDataField();
                    convertedNumericField.changeUnit();
                    if (yField != null) {
                        convertedYField = yField.copyDataField();
                        convertedYField.changeUnit();
                    }
                } else {
                    switchUnits = false;
                }

                String temp = blueMinRangeTextField.getText();
                if (temp.length() >= 1) {
                    minBlueRange = Double.parseDouble(temp);
                    minBlueRangeSet = true;
                }

                temp = blueMaxRangeTextField.getText();
                if (temp.length() >= 1) {
                    maxBlueRange = Double.parseDouble(temp);
                    maxBlueRangeSet = true;
                }

                temp = greenMinRangeTextField.getText();
                if (temp.length() >= 1) {
                    minGreenRange = Double.parseDouble(temp);
                    minGreenRangeSet = true;
                }

                temp = greenMaxRangeTextField.getText();
                if (temp.length() >= 1) {
                    maxGreenRange = Double.parseDouble(temp);
                    maxGreenRangeSet = true;
                }

                temp = yellowMinRangeTextField.getText();
                if (temp.length() >= 1) {
                    minYellowRange = Double.parseDouble(temp);
                    minYellowRangeSet = true;
                }

                temp = yellowMaxRangeTextField.getText();
                if (temp.length() >= 1) {
                    maxYellowRange = Double.parseDouble(temp);
                    maxYellowRangeSet = true;
                }

                temp = redMinRangeTextField.getText();
                if (temp.length() >= 1) {
                    minRedRange = Double.parseDouble(temp);
                    minRedRangeSet = true;
                }

                temp = redMaxRangeTextField.getText();
                if (temp.length() >= 1) {
                    maxRedRange = Double.parseDouble(temp);
                    maxRedRangeSet = true;
                }

                temp = gaugeNameTextField.getText();
                if (temp.length() >= 1) {
                    gaugeName = temp;
                } else {
                    int index = fieldsJList.getSelectedIndex();
                    if (switchUnits) {
                        gaugeName = ((convertedNumericField.getNameWithoutUnit()) + convertedNumericField.getUnit());
                    } else if (selectedField instanceof NumericDataField && ((NumericDataField) selectedField).hasUnit()) {
                        gaugeName = ((NumericDataField) selectedField).getNameWithoutUnit() + ((NumericDataField) selectedField).getUnit();
                    } else {
                        gaugeName = selectedField.getFieldName();
                    }
                }

                gaugeType = gaugeTypeComboBox.getSelectedItem().toString();

                createGauge(fieldType);
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
                    enableUnitsCheckbox();
                    setNumericListModel();
                    selectedNumericField = (NumericDataField) selectedField;
                    selectedNumericFieldMaxValue = selectedNumericField.getMaximum();
                    maxValueJLabel.setText("Max Value of Field: " + selectedNumericFieldMaxValue);
                    fieldType = "Numeric";
                } else if (selectedField instanceof TimeDataField) {
                    System.out.println("Time Field.");
                    disableRangeTextFields();
                    disableUnitsCheckbox();
                    setTimeListModel();
                    disableYFieldComboBox();
                    selectedNumericField = null;
                    maxValueJLabel.setText("Max Value of Field: NumericField not selected.");
                    fieldType = "Time";
                } else if (selectedField instanceof BooleanDataField) {
                    System.out.println("Boolean Field.");
                    disableRangeTextFields();
                    disableUnitsCheckbox();
                    setBooleanListModel();
                    disableYFieldComboBox();
                    selectedNumericField = null;
                    maxValueJLabel.setText("Max Value of Field: NumericField not selected.");
                    fieldType = "Boolean";
                } else {
                    System.out.println("String Field.");
                    disableRangeTextFields();
                    disableUnitsCheckbox();
                    setStringListModel();
                    disableYFieldComboBox();
                    selectedNumericField = null;
                    maxValueJLabel.setText("Max Value of Field: NumericField not selected.");
                    fieldType = "String";
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
        gaugeTypeComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String gaugeType = (String) gaugeTypeComboBox.getSelectedItem();
                if (gaugeType == "x by y plot") {
                    enableYFieldComboBox();
                } else {
                    disableYFieldComboBox();
                }
            }
        });
        yFieldComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int fieldIndex = yFieldComboBox.getSelectedIndex();
                if (yFields != null) {
                    yField = yFields.get(fieldIndex);
                }
            }
        });
    }

    private void populateFieldsJList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (DataField item : Fields) {
            if (item instanceof NumericDataField && ((NumericDataField) item).hasUnit()) {
                listModel.addElement(((NumericDataField) item).getNameWithoutUnit());
            } else {
                listModel.addElement(String.valueOf(item));
            }
        }
        fieldsJList.setModel(listModel);
    }

    private void createGauge(String type) {
        DataField inputField;
        NumericDataField yInputField;
        if (switchUnits == true && type == "Numeric") {
            inputField = convertedNumericField;
        } else if (type == "Numeric") {
            inputField = selectedNumericField;
        } else {
            inputField = selectedField;
        }

        if (switchUnits == true && gaugeType.equals("x by y plot")) {
            yInputField = convertedYField;
        } else {
            yInputField = yField;
        }

        if (gaugeType.equals("Circle 90")) {
            Gauge circle90 = new CircleGauge(90, gaugeName, (NumericDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(circle90);
            StaticGaugeArrayList.addGauge(circle90);
        } else if (gaugeType.equals("Circle 180")) {
            Gauge circle180 = new CircleGauge(180, gaugeName, (NumericDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(circle180);
            StaticGaugeArrayList.addGauge(circle180);
        } else if (gaugeType.equals("Circle 270")) {
            Gauge circle270 = new CircleGauge(270, gaugeName, (NumericDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(circle270);
            StaticGaugeArrayList.addGauge(circle270);
        } else if (gaugeType.equals("Circle 360")) {
            Gauge circle360 = new CircleGauge(360, gaugeName, (NumericDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(circle360);
            StaticGaugeArrayList.addGauge(circle360);
        } else if (gaugeType.equals("Bar")) {
            Gauge bar = new BarGauge(gaugeName, (NumericDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(bar);
            StaticGaugeArrayList.addGauge(bar);
        } else if (gaugeType.equals("x-plot")) {
            Gauge xPlot = new XPlotGauge(gaugeName, (NumericDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(xPlot);
            StaticGaugeArrayList.addGauge(xPlot);
        } else if (gaugeType.equals("x by y plot")) {
            Gauge xByYPlot = new XByYPlotGauge(gaugeName, (NumericDataField) inputField, yInputField, videoPlayer, frequency);
            setGaugeRanges(xByYPlot);
            StaticGaugeArrayList.addGauge(xByYPlot);
        } else if (gaugeType.equals("Number/character display")) {
            Gauge numOrChar = new NumOrSingleCharGauge(gaugeName, inputField, videoPlayer, frequency);
            if (inputField instanceof NumericDataField) {
                setGaugeRanges(numOrChar);
            }
            StaticGaugeArrayList.addGauge(numOrChar);
        } else if (gaugeType.equals("On/off light")) {
            Gauge OnOffLight = new OnOffLightGauge(gaugeName, (BooleanDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(OnOffLight);
            StaticGaugeArrayList.addGauge(OnOffLight);
        } else if (gaugeType.equals("Text display")) {
            Gauge TextDisplay = new TextDisplayGauge(gaugeName, inputField, videoPlayer, frequency);
            setGaugeRanges(TextDisplay);
            StaticGaugeArrayList.addGauge(TextDisplay);
            if (inputField instanceof NumericDataField) {
                setGaugeRanges(TextDisplay);
            }
        } else if (gaugeType.equals("Stopwatch")) {
            Gauge Stopwatch = new StopwatchGauge(gaugeName, (TimeDataField) inputField, videoPlayer, frequency);
            setGaugeRanges(Stopwatch);
            StaticGaugeArrayList.addGauge(Stopwatch);
        }
        resetRangeBooleans();
    }

    private void enableRangeTextFields() {
        blueMinRangeTextField.setEnabled(true);
        blueMaxRangeTextField.setEnabled(true);
        greenMinRangeTextField.setEnabled(true);
        greenMaxRangeTextField.setEnabled(true);
        yellowMinRangeTextField.setEnabled(true);
        yellowMaxRangeTextField.setEnabled(true);
        redMinRangeTextField.setEnabled(true);
        redMaxRangeTextField.setEnabled(true);
    }

    private void disableRangeTextFields() {
        blueMinRangeTextField.setEnabled(false);
        blueMaxRangeTextField.setEnabled(false);
        greenMinRangeTextField.setEnabled(false);
        greenMaxRangeTextField.setEnabled(false);
        yellowMinRangeTextField.setEnabled(false);
        yellowMaxRangeTextField.setEnabled(false);
        redMinRangeTextField.setEnabled(false);
        redMaxRangeTextField.setEnabled(false);
    }

    private void enableUnitsCheckbox() {
        NumericDataField temp = (NumericDataField) selectedField;
        if (temp.getUnit() != null)
            unitsCheckBox.setEnabled(true);
        else {
            disableUnitsCheckbox();
        }
    }

    private void disableUnitsCheckbox() {
        unitsCheckBox.setSelected(false);
        unitsCheckBox.setEnabled(false);
    }

    private void enableYFieldComboBox() {
        yFieldComboBox.setEnabled(true);
        for (NumericDataField f : yFields) {
            yFieldComboBox.addItem(f.getFieldName());
        }
    }

    private void disableYFieldComboBox() {
        yFieldComboBox.setEnabled(false);
        yFieldComboBox.removeAllItems();
        yField = null;
        convertedYField = null;
    }

    private void clearTextFields() {
        blueMinRangeTextField.setText("");
        blueMaxRangeTextField.setText("");
        greenMinRangeTextField.setText("");
        greenMaxRangeTextField.setText("");
        yellowMinRangeTextField.setText("");
        yellowMaxRangeTextField.setText("");
        redMinRangeTextField.setText("");
        redMaxRangeTextField.setText("");
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
        comboBoxModel.addElement("x by y plot");
        comboBoxModel.addElement("Number/character display");
        comboBoxModel.addElement("Text display");
        gaugeTypeComboBox.setModel(comboBoxModel);
    }

    private void setStringListModel() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Number/character display");
        comboBoxModel.addElement("Text display");
        gaugeTypeComboBox.setModel(comboBoxModel);
    }

    private void setTimeListModel() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Number/character display");
        comboBoxModel.addElement("Text display");
        comboBoxModel.addElement("Stopwatch");
        gaugeTypeComboBox.setModel(comboBoxModel);
    }

    private void setBooleanListModel() {
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("Number/character display");
        comboBoxModel.addElement("Text display");
        comboBoxModel.addElement("On/off light");
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

    private void setGaugeRanges(Gauge gauge) {
        if (minBlueRangeSet && maxBlueRangeSet) {
            gauge.setBlueRange(minBlueRange, maxBlueRange);
        }
        if (minGreenRangeSet && maxGreenRangeSet) {
            gauge.setGreenRange(minGreenRange, maxGreenRange);
        }
        if (minYellowRangeSet && maxYellowRangeSet) {
            gauge.setYellowRange(minYellowRange, maxYellowRange);
        }
        if (minRedRangeSet && maxRedRangeSet) {
            gauge.setRedRange(minRedRange, maxRedRange);
        }
    }

    private void resetRangeBooleans() {
        minBlueRangeSet = false;
        maxBlueRangeSet = false;
        minGreenRangeSet = false;
        maxGreenRangeSet = false;
        minYellowRangeSet = false;
        maxYellowRangeSet = false;
        minRedRangeSet = false;
        maxRedRangeSet = false;
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
        mainPanelGC.setLayout(new GridLayoutManager(10, 6, new Insets(0, 0, 0, 60), -1, -1));
        mainPanelGC.setBorder(BorderFactory.createTitledBorder(null, "", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JLabel label1 = new JLabel();
        label1.setText("Yellow Min Range");
        mainPanelGC.add(label1, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Green Min Range");
        mainPanelGC.add(label2, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Blue Min Range");
        mainPanelGC.add(label3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Red Min Range");
        mainPanelGC.add(label4, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Name (Optional)");
        mainPanelGC.add(label5, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Gauge Type");
        mainPanelGC.add(label6, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Data Fields");
        mainPanelGC.add(label7, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        redMinRangeTextField = new JTextField();
        redMinRangeTextField.setEnabled(false);
        mainPanelGC.add(redMinRangeTextField, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        yellowMinRangeTextField = new JTextField();
        yellowMinRangeTextField.setEnabled(false);
        mainPanelGC.add(yellowMinRangeTextField, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        greenMinRangeTextField = new JTextField();
        greenMinRangeTextField.setEnabled(false);
        mainPanelGC.add(greenMinRangeTextField, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        gaugeNameTextField = new JTextField();
        gaugeNameTextField.setEnabled(false);
        mainPanelGC.add(gaugeNameTextField, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        blueMinRangeTextField = new JTextField();
        blueMinRangeTextField.setEnabled(false);
        mainPanelGC.add(blueMinRangeTextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        gaugeTypeComboBox = new JComboBox();
        gaugeTypeComboBox.setEnabled(false);
        mainPanelGC.add(gaugeTypeComboBox, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Switch Units");
        mainPanelGC.add(label8, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane1 = new JScrollPane();
        mainPanelGC.add(scrollPane1, new GridConstraints(2, 0, 7, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        fieldsJList = new JList();
        fieldsJList.setSelectionMode(0);
        scrollPane1.setViewportView(fieldsJList);
        unitsCheckBox = new JCheckBox();
        unitsCheckBox.setEnabled(false);
        unitsCheckBox.setText("");
        mainPanelGC.add(unitsCheckBox, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxValueJLabel = new JLabel();
        maxValueJLabel.setText("Max Value of Field: NumericField not selected. ");
        mainPanelGC.add(maxValueJLabel, new GridConstraints(0, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Blue Max Range");
        mainPanelGC.add(label9, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Green Max Range");
        mainPanelGC.add(label10, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Yellow Max Range");
        mainPanelGC.add(label11, new GridConstraints(4, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Red Max Range");
        mainPanelGC.add(label12, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        blueMaxRangeTextField = new JTextField();
        blueMaxRangeTextField.setEnabled(false);
        mainPanelGC.add(blueMaxRangeTextField, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        greenMaxRangeTextField = new JTextField();
        greenMaxRangeTextField.setEnabled(false);
        greenMaxRangeTextField.setText("");
        mainPanelGC.add(greenMaxRangeTextField, new GridConstraints(3, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        yellowMaxRangeTextField = new JTextField();
        yellowMaxRangeTextField.setEnabled(false);
        mainPanelGC.add(yellowMaxRangeTextField, new GridConstraints(4, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        redMaxRangeTextField = new JTextField();
        redMaxRangeTextField.setEnabled(false);
        mainPanelGC.add(redMaxRangeTextField, new GridConstraints(5, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        doneButton = new JButton();
        doneButton.setText("Done");
        mainPanelGC.add(doneButton, new GridConstraints(9, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        createGaugeButton = new JButton();
        createGaugeButton.setEnabled(false);
        createGaugeButton.setText("Create Gauge");
        mainPanelGC.add(createGaugeButton, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("You MUST enter a min and max for the color ranges.");
        mainPanelGC.add(label13, new GridConstraints(1, 1, 1, 5, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label14 = new JLabel();
        label14.setText("Y Field (For XY Plot)");
        mainPanelGC.add(label14, new GridConstraints(8, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        yFieldComboBox = new JComboBox();
        yFieldComboBox.setEnabled(false);
        mainPanelGC.add(yFieldComboBox, new GridConstraints(8, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label15 = new JLabel();
        label15.setText("Represented by the white line");
        mainPanelGC.add(label15, new GridConstraints(7, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanelGC;
    }

}
