package yourpackage.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class NumericDataField extends DataField{

    private String unit;
    private double minimum;
    private double maximum;
    private double average;
    private double standardDeviation;
    private boolean isMetric;
    private ArrayList<Double> dataRows;
    public NumericDataField(String name) {
        super(name);
        this.dataRows = new ArrayList<>();
        this.minimum = 0.0;
        this.maximum = 0.0;
        this.standardDeviation = 0.0;
        this.isMetric = true;
        if (name.contains(" ")) {
            String[] strings = name.split(" ", 2);
            if (strings[1].contains("["))
            {
                this.unit = strings[1];
            }
        }
    }

    public double getMinimum() {
        if (this.dataRows == null || this.dataRows.isEmpty()) {
            return 0;
        }
        else
            return this.minimum = Collections.min(this.dataRows);
    }

    public double getMaximum() {
        if (this.dataRows == null || this.dataRows.isEmpty()) {
            return 0;
        }
        else
            return this.maximum = Collections.max(this.dataRows);
    }

    public double getAverage() {
        if (this.dataRows == null || this.dataRows.isEmpty())
            return 0.0;
        else {
            double sum = 0.0;
            for (double d : this.dataRows) {
                sum += d;
            }
            return this.average = (sum)/(this.dataRows.size());
        }
    }

    public double getStandardDeviation () {
        if (this.dataRows == null || this.dataRows.isEmpty())
            return 0.0;
        else {
            double temp = 0.0;
            for (Double dataRow : this.dataRows) {
                temp += Math.pow(dataRow - this.getAverage(), 2);
            }
            return this.average = Math.sqrt((temp)/(this.dataRows.size()-1));
        }
    }

    public void changeUnit() {
        if (isMetric) {
            if (this.unit.equals("[m/s]")) {
                for (double d : this.dataRows) {
                    //compute to mph
                    d = (d*3600)/1609.3;
                }
                this.unit = "[mph]";
            }
            else if (this.unit.equals("[m]")) {
                for (double d : this.dataRows) {
                    //compute to ft
                    d = d*3.28084;
                }
                this.unit = "[ft]";
            }
        }
        else {
            if (this.unit.equals("[mph]")) {
                for (double d : this.dataRows) {
                    //compute to m/s
                    d = (d*1609.3)/3600;
                }
                this.unit = "[m/s]";
            }
            else if (this.unit.equals("[ft]")) {
                for (double d : this.dataRows) {
                    //compute to m
                    d = d/3.28084;
                }
                this.unit = "[m]";
            }
        }
    }

    @Override
    public void addDataRow(String dataRow) {
        this.dataRows.add(Double.valueOf(dataRow));
    }

    @Override
    public ArrayList<String> getDataRows() {
        ArrayList<String> data = new ArrayList<>();
        for (double d : this.dataRows) {
            data.add(Double.toString(d));
        }
        return data;
    }

    @Override
    public String toString() {
        return this.getFieldName() + this.unit;
    }
}
