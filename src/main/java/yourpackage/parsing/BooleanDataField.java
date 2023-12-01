package yourpackage.parsing;

import java.util.ArrayList;
import java.lang.Boolean;

public class BooleanDataField extends DataField{
    private ArrayList<Boolean> dataRows;

    public BooleanDataField(String name) {
        super(name);
        this.dataRows = new ArrayList<>();
    }

    @Override
    public void addDataRow(String dataRow) {
        if ((dataRow).equalsIgnoreCase("true") || dataRow.equals("1")) {
            this.dataRows.add(true);
        }
        else
            this.dataRows.add(false);
    }

    public Boolean getNext() {
        return null;
    }

    // Not sure if this is needed, but the functionality is there
    public ArrayList<Integer> getAsNumeric() {
         ArrayList<Integer> convertedValues = new ArrayList<>();
         for (Boolean b : dataRows) {
             if (b)
                 convertedValues.add(1);
             else
                 convertedValues.add(0);
         }
         return convertedValues;
    }

}