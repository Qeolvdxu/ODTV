package yourpackage.gauges;

import yourpackage.app.Main;
import yourpackage.parsing.DataField;
import yourpackage.parsing.StringField;
import eu.hansolo.tilesfx.Tile;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class NumOrSingleCharGauge extends Gauge{

    private StringField field;

    public NumOrSingleCharGauge()
    {
        super();
        this.gauge = GaugeType.NumOrSingleChar;
        field = null;
        tile.setSkinType(Tile.SkinType.CHARACTER);
        tile.setTitle("Character Gauge");
        tile.setTitleAlignment(TextAlignment.CENTER);
        tile.setDescription("C");

    }

    public void update() {
        String newValue = field.getNext();
        if (newValue != null){
            tile.setDescription(newValue);
        }
    }

    public DataField getField() {
        return field;
    }

    public void setField(StringField field) {
        this.field = field;
    }
}