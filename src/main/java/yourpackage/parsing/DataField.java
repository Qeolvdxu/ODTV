package yourpackage.parsing;

import java.io.Serializable;
import java.util.ArrayList;

public class DataField implements Serializable {
    private String fieldName;
    protected ArrayList<String> dataRows;

    public DataField(String name) {
        this.setFieldName(name);
        this.dataRows = new ArrayList<>();
    }

    /**
    * Set the fieldName for the selected DataField
     * @param fieldName string descriptor of the field
    */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Method to retrieve the name of a particular DataField
     * @return designated string name of DataField
     * */
    public String getFieldName() {
        return this.fieldName;
    }

    /**
     * Method to add a row of data to a DataField
     * @param dataRow string of parsed data to append to list of data elements
     */
    public void addDataRow(String dataRow) {
        this.dataRows.add(dataRow);
    }

    /**
     * Method to retrieve the elements of the DataField
     * @return strings of data from parsed file
     */
    public ArrayList<String> getDataRows() {
        return this.dataRows;
    }

    /**
     * Method to set a DataField's list of data to a new existing list
     * @param list already existing list to set as the data rows of a DataField
     */
    public void setDataRows(ArrayList<String> list) {
        if (list != null)
            this.dataRows = list;
    }

    public String getIndexOfString(int index) {
        return this.dataRows.get(index);
    }

    public int getDataRowsLength() { return this.dataRows.size(); }

    public String toString() {
        return this.fieldName;
    }
    public Object getNext() { return getNext();}
}
