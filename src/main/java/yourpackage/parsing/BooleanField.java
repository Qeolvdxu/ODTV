package yourpackage.parsing;

import java.util.ArrayList;
import java.util.List;
import java.lang.Boolean;

public class BooleanField extends DataField{
    List<Boolean> myBools = new ArrayList<>();
    int nullCounter = 0;
    private int i;
    private int j;

    public BooleanField(DataField myField) {
        super(myField.getFieldName());
        super.dataRows = myField.dataRows;
        parseBoolField();
    }
    private void parseBoolField() {
        for (String b : super.getDataRows()) {
            if (b.toLowerCase() == "true" ) {
                myBools.add(Boolean.valueOf(true));
            } else if (b.toLowerCase() == "false") {
                myBools.add(Boolean.valueOf(false));
            } else {
                nullCounter++;
                myBools.add(null);
            }
        }
    }

    public boolean setIndex(int index) {
        if (0<=index && index<myBools.size()) {
            this.i = index;
            return true;
        } else { return false; }
    }

    public Boolean getNext() {
        this.j = this.i;
        this.i = this.i+1;
        if (!(this.j >= this.myBools.size())) {
            return this.myBools.get(this.j);
        } else {
            return null;
        }
    }

    public void printDataAt(int index) { System.out.println(myBools.get(index)); }
}