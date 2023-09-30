import java.util.ArrayList;
import java.util.Collections;

public class DataField {
    private String fieldName;
    ArrayList<String> dataRows = new ArrayList<>();

    public DataField(String name) {
        this.setFieldName(name);
    }
    public DataField() {
        this.setFieldName("");
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return this.fieldName;
    }

    public void addDataRow(String dataRow) {
        this.dataRows.add(dataRow);
    }

    public String getDataRows() {
        return Collections.unmodifiableList(dataRows).toString();
    }

    public void setDataRows(ArrayList<String> list) {
        if (list != null)
            this.dataRows = list;
    }

    public int getDataRowLength() {
        return this.dataRows.size();
    }

    public String toString() {
        return this.fieldName;
    }

}
