package yourpackage.parsing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class NumericDataField extends DataField{

    private double minimum;
    private double maximum;
    private double average;
    private double standardDeviation;
    private ArrayList<Double> dataRows;
    public NumericDataField(String name) {
        super(name);
        this.dataRows = new ArrayList<>();
        this.minimum = 0.0;
        this.maximum = 0.0;
        this.standardDeviation = 0.0;
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

    // Evaluation is currently close, but not close enough.
    public double getStandardDeviation () {
        if (this.dataRows == null || this.dataRows.isEmpty())
            return 0.0;
        else {
            double temp = 0.0;
            for (Double dataRow : this.dataRows) {
                temp += Math.pow(dataRow - this.getAverage(), 2);
            }
            return this.average = Math.sqrt((temp)/(this.dataRows.size()));
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
}
