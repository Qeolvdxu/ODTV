package yourpackage.gauges;

import yourpackage.parsing.DataField;
import yourpackage.parsing.StringField;
import eu.hansolo.tilesfx.Tile;

import java.util.Random;

public class TextDisplayGauge extends Gauge{

    private DataField field;

    public TextDisplayGauge()
    {
        super();
        this.gauge = GaugeType.TextDisplay;
        tile.setTitle("TextDisplay Gauge");
        tile.setDescription("String");
        //tile.setSkinType(Tile.SkinType.CENTER_TEXT);
    }
    public void update() {
        Object newValue = field.getNext();
        if (newValue == null) {
            return;
        }
        tile.setDescription((String) newValue);
    }

    public DataField getField() { return field; }

    public void setField(DataField field) {
        this.field = field;
    }
}



