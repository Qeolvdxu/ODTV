package yourpackage.parsing;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class NumericDataField extends DataField {

    private String unit;
    private double minimum;
    private double maximum;
    private double average;
    private double stdDev;
    private boolean isMetric;
    private ArrayList<Double> dataRows;

    /**
     * Construct NumericDataField with default statistical values of 0 and defaulted to metric units.
     * If the field is found to be parsed with a space and a set of brackets containing a string, it
     * will be interpreted as its unit.
     * */

    public String pickedUnit = "";
    public String ogUnit = "";
    public String unitType = "";
    public UnitConvert uc = new UnitConvert();

    public NumericDataField(String name) {
        super(name);
        this.dataRows = new ArrayList<>();
        this.minimum = 0.0F;
        this.maximum = 0.0F;
        this.stdDev = 0.0F;
    }

    private String unit;
    private double minimum;
    private double maximum;
    private double average;
    private double stdDev;
    private boolean isMetric;
    private ArrayList<Double> dataRows;

    /**
     * Construct NumericDataField with default statistical values of 0 and defaulted to metric units.
     * If the field is found to be parsed with a space and a set of brackets containing a string, it
     * will be interpreted as its unit.
     * */
    public NumericDataField(String name) {
        super(name);
        this.dataRows = new ArrayList<>();
        this.minimum = 0.0;
        this.maximum = 0.0;
        this.stdDev = 0.0;
        this.isMetric = true;
        if (name.contains(" ")) {
            String[] strings = name.split(" ", 2);
            if (strings[1].contains("["))
            {
                this.unit = strings[1];
            }
        }
    }

    /**
     * Get the minimum value of this field's data rows
     * @return 0 if dataRows are empty or null, else return the minimum element
     */

  public double getMinimum() {
        if (this.dataRows == null || this.dataRows.isEmpty()) {
            return 0;
        } else
            return this.minimum = Collections.min(this.dataRows);
    }

    /**
     * Get the maximum value of this field's data rows
     * @return 0 if dataRows are empty or null, else return the maximum element
     */
    public double getMaximum() {
        if (this.dataRows == null || this.dataRows.isEmpty()) {
            return 0;
        } else
            return this.maximum = Collections.max(this.dataRows);
    }

    // Retrieves next Double field in the index
    public Double getNext() {
        //Needed for the update function
        return 0.0;
    }

    public void convert(String type, String from, String to) {
        this.unitType = type;
        this.ogUnit = from;
        this.pickedUnit = to;
        //this.dataRows = this.uc.convert(this.unitType, this.pickedUnit, this.ogUnit, this.dataRows);
=======
    /**
     * Compute the average of the numerical values within the dataRows of this field
     * @return 0 if dataRows are empty or null, else calculate and return average
     */
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

    /**
     * Compute the standard deviation of the numerical values within the dataRows of this field
     * @return 0 if dataRows are empty or null, else calculate and return standard deviation
     */
    public double getStdDev() {
        if (this.dataRows == null || this.dataRows.isEmpty())
            return 0.0;
        else {
            double temp = 0.0;
            for (Double dataRow : this.dataRows) {
                temp += Math.pow(dataRow - this.getAverage(), 2);
            }
            return this.stdDev = Math.sqrt((temp)/(this.dataRows.size()-1));
        }
    }

    /**
     * @return String of the unit for this data field if it exists
     */
    public String getUnit() {
        if (this.unit != null)
            return this.unit;
        else
            return null;
    }

    /**
     * Method to convert the unit of the dataRows between metric and imperial. Data is assumed
     * metric by default, and either m/s or m (converted to mph or ft).
     * */
    public void changeUnit() {
        if (this.isMetric) {
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

    /**
     * Method to append a value of type double to the ArrayList of dataRows.
     */
    @Override
    public void addDataRow(String dataRow) {
        this.dataRows.add(Double.valueOf(dataRow));
    }

    /**

     * Compute the average of the numerical values within the dataRows of this field
     *
     * @return 0 if dataRows are empty or null, else calculate and return average
     */
    public double getAverage() {
        if (this.dataRows == null || this.dataRows.isEmpty())
            return 0.0;
        else {
            double sum = 0.0;
            for (double d : this.dataRows) {
                sum += d;
            }
            return this.average = (sum) / (this.dataRows.size());
        }
    }

    /**
     * Compute the standard deviation of the numerical values within the dataRows of this field
     *
     * @return 0 if dataRows are empty or null, else calculate and return standard deviation
     */
    public double getStdDev() {
        if (this.dataRows == null || this.dataRows.isEmpty())
            return 0.0;
        else {
            double temp = 0.0;
            for (Double dataRow : this.dataRows) {
                temp += Math.pow(dataRow - this.getAverage(), 2);
            }
            return this.stdDev = Math.sqrt((temp) / (this.dataRows.size() - 1));
        }
    }

    /**
     * @return String of the unit for this data field if it exists
     */
    public String getUnit() {
        if (this.unit != null)
            return this.unit;
        else
            return null;
    }

    /**
     * Method to convert the unit of the dataRows between metric and imperial. Data is assumed
     * metric by default, and either m/s or m (converted to mph or ft).
     */
    public void changeUnit() {
        if (this.isMetric) {
            if (this.unit.equals("[m/s]")) {
                for (double d : this.dataRows) {
                    //compute to mph
                    d = (d * 3600) / 1609.3;
                }
                this.unit = "[mph]";
            } else if (this.unit.equals("[m]")) {
                for (double d : this.dataRows) {
                    //compute to ft
                    d = d * 3.28084;
                }
                this.unit = "[ft]";
            }
        } else {
            if (this.unit.equals("[mph]")) {
                for (double d : this.dataRows) {
                    //compute to m/s
                    d = (d * 1609.3) / 3600;
                }
                this.unit = "[m/s]";
            } else if (this.unit.equals("[ft]")) {
                for (double d : this.dataRows) {
                    //compute to m
                    d = d / 3.28084;
                }
                this.unit = "[m]";
            }
        }
      
     * Method to return ArrayList of dataRows in String format
     * @return data, a temporary ArrayList to convert the contents of dataRows to String
     */
    @Override
    public ArrayList<String> getDataRows() {
        ArrayList<String> data = new ArrayList<>();
        for (double d : this.dataRows) {
            data.add(Double.toString(d));
        }
        return data;
    }

    /**
     * @return the name of this field plus its unit
     */
    @Override
    public String toString() {
        return this.getFieldName() + this.getUnit();
    }
}
