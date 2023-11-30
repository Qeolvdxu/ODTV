package yourpackage.parsing;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class StringField extends DataField{
    transient List<String> myStrings = new ArrayList<>();
    transient int nullCounter = 0;
    transient private int i;
    transient private int j;

    public StringField(DataField myField) {
        super(myField.getFieldName());
        super.dataRows = myField.dataRows;
        parseStringField();
    }

    private void parseStringField() {
        for (String s : super.getDataRows()) {
            if (s!="") {
                myStrings.add(s);
            } else {
                nullCounter++;
                myStrings.add(null);
            }
        }
    }

    public boolean setIndex(int index) {
        if (0<=index && index<myStrings.size()) {
            this.i = index;
            return true;
        } else { return false; }
    }

    public String getNext() {
        this.j = this.i;
        this.i = this.i+1;
        if (!(this.j >= this.myStrings.size())) {
            return this.myStrings.get(this.j);
        } else {
            return null;
        }
    }

    public void printDataAt(int index) {
        System.out.println(myStrings.get(index));

    }
}
