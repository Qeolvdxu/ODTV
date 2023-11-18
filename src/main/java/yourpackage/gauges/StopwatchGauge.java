package yourpackage.gauges;

import yourpackage.parsing.DataField;
import yourpackage.parsing.TimeField;
import eu.hansolo.tilesfx.Tile;

public class StopwatchGauge extends Gauge{

    private TimeField field;

    public StopwatchGauge()
    {
        super();
        this.gauge = GaugeType.Stopwatch;
        tile.setTitle("Clock Gauge");
        tile.setSkinType(Tile.SkinType.CLOCK);
        tile.setTime(System.currentTimeMillis() / 1000);
    }
    public void update() {
        Long newValue = field.getNext();
        if (newValue == null) {
            return;
        }
        tile.setTime(newValue);
    }

    public void setField(TimeField field) {
        this.field = field;
    }

    public DataField getField() { return field; }
}