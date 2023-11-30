package yourpackage.parsing;

import java.util.ArrayList;
public class StaticSelectedFieldsArrayList {
    private static ArrayList<DataField> Fields = new ArrayList<DataField>();

    public static void addField(DataField df) {
        Fields.add(df);
    }

    public static void removeFields() {
        Fields.clear();
    }

    public static ArrayList<DataField> getFields() {
        return Fields;
    }
}
