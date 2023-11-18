package yourpackage.parsing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.lang.Long;
import java.util.Date;
import java.util.List;

public class TimeField extends DataField {
    SimpleDateFormat timeTemplate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    SimpleDateFormat decimalTimeTemplate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
    List<Long> myTimes = new ArrayList<>();
    int nullCounter = 0;
    private int timeCURR = 0;   // Index pointing to the next available data point
    private int timePREV = 0;   // Index pointing to the last used data point
    private Long start;  // Start time as an epoch time with nanosecond resolution
    private Long prevInterval;

    public TimeField(DataField myField) {
        super(myField.getFieldName());
        super.dataRows = myField.dataRows;
        parseTimeField();
    }

    private void parseTimeField() {
        for (String time : super.getDataRows()) {
            try {
                myTimes.add(1000000*decimalTimeTemplate.parse(time).getTime());
            } catch (ParseException e) {
                try {
                    myTimes.add(timeTemplate.parse(time).getTime()*1_000_000);
                } catch (ParseException ex) {
                    e.printStackTrace();
                    nullCounter++;
                    myTimes.add(null);
                    return;
                }

            }
        }
        start = myTimes.get(0);
    }

    public boolean setIndex(int index) {
        if (0<=index && index<myTimes.size()) {
            this.timeCURR = index;
            return true;
        } else { return false; }
    }

    public Long getNextInterval(){
        this.timePREV = this.timeCURR;
        this.timeCURR = this.timeCURR+1;
        if (!(this.timePREV >= this.myTimes.size() || this.timeCURR >= this.myTimes.size())) {
            Long prev_o = myTimes.get(timePREV);
            Long curr_o = myTimes.get(timeCURR);
            if (prev_o == null || curr_o == null) {
                return prevInterval;
            }
            prevInterval = curr_o.longValue() - prev_o.longValue();
            return prevInterval;
        } else {
            return null;
        }
    }

    public Long getNext() {
        this.timePREV = this.timeCURR;
        this.timeCURR = this.timeCURR+1;
        if (!(this.timePREV >= this.myTimes.size())) {
            return this.myTimes.get(this.timePREV);
        } else {
            return null;
        }
    }

    // Returns the next time as a Date object
    public Date getNextDate() {
        this.timePREV = this.timeCURR;
        this.timeCURR = this.timeCURR+1;
        if (!(this.timePREV >= this.myTimes.size())) {
            return new Date(this.myTimes.get(this.timePREV)/1000000);
        } else {
            return null;
        }
    }

    public void printDataAt(int index) {
        System.out.println(myTimes.get(index));
    }
}
