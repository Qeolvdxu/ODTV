package yourpackage.parsing;


public class TimeDataField extends DataField {
    private float frequency;
    public TimeDataField(String name) {
        super(name);
    }

    public String getDate(int index) {
        String str = this.getIndexOfString(index);
        String[] splitStr = str.split("\\s+");
        return splitStr[0];
    }

    public String getTime(int index) {
        String str = this.getIndexOfString(index);
        String[] splitStr = str.split("\\s+");
        return splitStr[1];
    }
}
