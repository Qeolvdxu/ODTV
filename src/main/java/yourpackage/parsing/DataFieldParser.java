package yourpackage.parsing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DataFieldParser {

    private ArrayList<DataField> foundFields;   // Arraylist of data fields found in the CSV File
    private List<CSVRecord> foundRecords;       // List of CSV Records found within the CSV File

    public DataFieldParser(File f) {
        try {
            CSVParser fileParser = CSVParser.parse(f, Charset.defaultCharset(), CSVFormat.DEFAULT);
            this.foundFields = new ArrayList<>();
            this.foundRecords = fileParser.getRecords();
            fileParser.close();
        } catch (IOException ex) {
            System.err.println(ex);
            return;
        }
    }

        /**
         * Method to parse through a CSV (Comma-Separated Value) file and retrieve data for each column.
         * Begins by iterating through the second record (row) of the CSV file to determine data typing
         * for each column. If the particular cell is empty, the loop iterates through each record at
         * that index to find a non-empty cell, or until it reaches the last record. Constructs a new
         * DataField object depending upon the regular expression found within the cell, and loops
         * through each record at a given index to append data to the DataField's list of elements.
         * (Assumes that first row contains the name of each column).
         */
    public void parseData() {
        int i = 0;
        int j = 0;
        for (String s : this.foundRecords.get(2)) {
            while (s.isEmpty()){
                for (CSVRecord r: this.foundRecords) {
                    if (r.getRecordNumber() > 0) {
                        s = r.get(i);
                    }
                    if (r.getRecordNumber() == this.foundRecords.size())
                        s = " ";
                }

                i++;
            }
            DataField df = getDataFieldType(s, j);
            for (CSVRecord r: this.foundRecords) {
                String row = r.get(j);
                if (r.getRecordNumber() > 1) {
                    if (row.isEmpty())
                        df.addDataRow(" ");
                    else
                        df.addDataRow(row);
                }
            }
            this.foundFields.add(df);   // Add new data field to list of found fields
            j++;
        }
    }

    public ArrayList<DataField> getFoundFields() {
        return this.foundFields;
    }

    /**
     * Private method used by getFoundFields() to return the specific type of DataField based on a
     * regular expression found within a data cell
     * @param s string found within data cell, passed to detect if it matches a numerical value, date, or string
     * @param j integer passed for the current index in the record, used to get the name of the corresponding data field
     * @return DataField either of subtype TimeDataField, NumericalDataField, or just DataField
     */
    private DataField getDataFieldType(String s,int j){
        DataField df;
        String name = this.foundRecords.get(0).get(j);
        if (s.matches("\\d{4}[/]\\d{1,2}[/]\\d{1,2} \\d{1,2}[:]\\d{2}[:]\\d{2}[.]\\d{3}"))  // If the string matches the regex for yyyy/mm/dd 00:00:00.000
            df = new TimeDataField(name);
        else {
            HashSet<String> uniqueValues = new HashSet<>();
            for (CSVRecord r: this.foundRecords) {
                String row = r.get(j);
                if (r.getRecordNumber() > 1 && ((!(row.isEmpty() || row.isBlank())))) {
                    uniqueValues.add(r.get(j).toLowerCase());
                }
            }
            if (uniqueValues.contains("True") || uniqueValues.contains("False") || name.contains(".is")) {
                df = new BooleanDataField(name);
            }
            else {
                if ((s.matches("((\\+|-)?([0-9]+)(\\.[0-9]+)?)|((\\+|-)?\\.?[0-9]+)")) || (this.foundRecords.get(0).get(j).contains("[")))
                    df = new NumericDataField(name);

                else
                    df = new DataField(name);    // Else the field will be read as a string
            }
        }

        return df;
    }

    public void clearFields() { this.foundFields.clear(); }
}
