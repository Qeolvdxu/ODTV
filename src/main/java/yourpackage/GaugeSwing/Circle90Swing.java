package yourpackage.GaugeSwing;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import yourpackage.app.Main;
import yourpackage.gauges.CircleGauge;
import yourpackage.parsing.NumericDataField;
//import yourpackage.parsing.UnitConverter;
import yourpackage.gauges.CircleGauge;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.tilesfx.tools.GradientLookup;
import yourpackage.parsing.UnitConvert;

//import javafx.scene.control.*;
//import javafx.scene.layout.HBox;
//import javafx.scene.paint.Stop;
//import javafx.stage.Stage;
//import javafx.util.StringConverter;




public class Circle90Swing {

    private NumericDataField field;

    private JTextField fieldTitle;

    private JTextField fieldGreen;

    private JTextField fieldYellow;

    private JTextField fieldRed;

    private JTextField fieldMin;

    private JTextField fieldMax;

    private JButton jbuttonClose;

    private JButton jbuttonUnit;

    private JTextField statMax;

    private JTextField statMin;

    private JTextField statAvg;

    private JTextField statStdDev;

    private JComboBox<String> alarmJComboBox;

    private JComboBox<String> unitTypeJComboBox;

    private JComboBox<String> currentUnitJComboBox;

    private JComboBox<String> desiredUnitJComboBox;

    private JPanel mainPanel;

    private UnitConvert uc = new UnitConvert();

    protected void UnitChangeClick() {
        String cur = currentUnitJComboBox.getSelectedItem().toString();
        String des = desiredUnitJComboBox.getSelectedItem().toString();
        if (cur==this.field.ogUnit || des==this.field.pickedUnit || cur==des) { return; }
        this.field.convert(unitTypeJComboBox.getSelectedItem().toString(), currentUnitJComboBox.getSelectedItem().toString(), desiredUnitJComboBox.getSelectedItem().toString());
        this.updateStats();
    }

    public void setField(NumericDataField relatedField) {
        field = relatedField;
        fieldTitle.setText(field.getFieldName());
        updateStats();
    }

    void updateStats() {
        statMax.setText(String.valueOf(field.getMaximum()));
        statMin.setText(String.valueOf(field.getMinimum()));
        statAvg.setText(String.valueOf(field.getAverage()));
        //statStdDev.setText(String.valueOf(field.getStdDev()));
    }

    protected void NewUnitType() {

    }

    protected void CancelClick() {
        System.out.println("Cancelled gauge creation");

        //Finished, close this Gauge Creation window.
        //JFrame frame;
        //frame.dispose();
    }

    public void initialize(URL url, ResourceBundle rb) {

    }

    protected void CompletedClick() throws IOException {

    }

    private void createGauge(String title, double min, double max, double green, double yellow, double red, String soundtheAlarm){
        CircleGauge gaugeObj = new CircleGauge(90);
        gaugeObj.setField(field);
        gaugeObj.setTitle(title);

        gaugeObj.tile.setMaxValue(max);
        gaugeObj.tile.setMinValue(min);

        GradientLookup gradient = new GradientLookup(Arrays.asList(
                new Stop(0, Bright.BLUE),
                new Stop(CircleGauge.map(green,min,max,0,1), Bright.GREEN),
                new Stop(CircleGauge.map(yellow,min,max,0,1), Bright.YELLOW),
                new Stop(CircleGauge.map(red,min,max,0,1), Bright.RED),
                new Stop(1, Bright.RED)));

        gaugeObj.setGradient(gradient);
        switch(soundtheAlarm)
        {
            case "criticalAlarm":
                gaugeObj.setAlarm(1);
                System.out.println("ran set alarm (1)");
                break;
            default:
                break;
        }

        Main.gauges.add(gaugeObj);

        JFrame frame = new JFrame();
        frame.setContentPane(mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(250, 250));
        frame.setTitle(title);
        frame.setVisible(true);
    }
    public NumericDataField getField()
    {
        return field;
    }
}