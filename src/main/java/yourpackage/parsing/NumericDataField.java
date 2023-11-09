package yourpackage.parsing;


import java.util.ArrayList;
import java.util.Collections;
import java.util.OptionalDouble;

public class NumericDataField extends DataField{

    private float minimum;
    private float maximum;
    private OptionalDouble average;
    private float standardDeviation;
    private ArrayList<Float> dataRows;
    public String pickedUnit="";
    public String ogUnit="";
    public String unitType="";
    public UnitConvert uc = new UnitConvert();

    public NumericDataField(String name) {
        super(name);
        this.dataRows = new ArrayList<>();
        this.minimum = 0.0F;
        this.maximum = 0.0F;
        this.standardDeviation = 0.0F;
    }

    public float getMinimum() {
        if (this.dataRows == null || this.dataRows.isEmpty()) {
            return 0;
        }
        else
            return this.minimum = Collections.min(this.dataRows);
    }

    public float getMaximum() {
        if (this.dataRows == null || this.dataRows.isEmpty()) {
            return 0;
        }
        else
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
    }

    public OptionalDouble getAverage() {
        return this.average = this.dataRows.stream().mapToDouble(a -> a).average();
    }
    /*public Float getStdDev(){

    }
    */
    @Override
    public void addDataRow(String dataRow) {
        this.dataRows.add(Float.valueOf(dataRow));
    }
}
