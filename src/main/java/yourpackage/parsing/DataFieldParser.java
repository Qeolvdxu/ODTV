package yourpackage.parsing;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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
        }
        catch (IOException ex) {
            System.err.println(ex);
            return;
        }
    }

    public ArrayList<DataField> getFoundFields() {
        int i = 0;
        int j = 0;
        for (String s : this.foundRecords.get(1)) {
            while (s.isEmpty()) {
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
            this.foundFields.add(df);   // Add new data field to list of found fields
            j++;
        }
        return this.foundFields;
    }

    private DataField getDataFieldType(String s, int j) {
        DataField df;
        if (s.matches("\\d{4}[/]\\d{1,2}[/]\\d{1,2} \\d{1,2}[:]\\d{2}[:]\\d{2}[.]\\d{3}"))  // If the string matches the regex for yyyy/mm/dd 00:00:00.000
            df = new TimeDataField(this.foundRecords.get(0).get(j));
        else if (s.matches("\\d+") || s.matches("[-]\\d+")) // Else if the string matches the regex for a positive or negative digit
            df = new NumericDataField(this.foundRecords.get(0).get(j));
        else
            df = new DataField(this.foundRecords.get(0).get(j));    // Else the field will be read as a string
        return df;
    }

    public ArrayList<String> getData(int index) {
        if (this.foundFields == null || this.foundFields.isEmpty())
            this.getFoundFields();
        for (CSVRecord r: this.foundRecords) {
            if (r.getRecordNumber() > 1) {
                if (r.get(index).isEmpty())
                    this.foundFields.get(index).addDataRow(" ");
                else
                    this.foundFields.get(index).addDataRow(r.get(index));
            }
        }
        return this.foundFields.get(index).getDataRows();
    }
}
