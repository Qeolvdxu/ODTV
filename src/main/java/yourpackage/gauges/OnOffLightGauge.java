package yourpackage.gauges;

import yourpackage.parsing.BooleanField;
import yourpackage.parsing.DataField;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import java.util.Random;

public class OnOffLightGauge extends Gauge{

    //private static final double TILE_SIZE = 150;
    private BooleanField field;

    public OnOffLightGauge()
    {
        this.gauge = GaugeType.OnOffLight;
        //tile = TileBuilder.create().skinType(Tile.SkinType.LED)
        //        .prefSize(TILE_SIZE, TILE_SIZE)
        //        .title("OnOff Gauge")
        //        .build();

        tile.setActive(false);
    }
    public void update() {

        Boolean newValue = field.getNext();

        if (newValue != null)
        {
            tile.setActive(newValue);
        }
    }

    public void setField(BooleanField field) {
        this.field = field;
    }

    public DataField getField() { return field; }
}
