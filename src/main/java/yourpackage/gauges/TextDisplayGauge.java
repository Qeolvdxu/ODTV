package yourpackage.gauges;

import eu.hansolo.tilesfx.TileBuilder;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import yourpackage.parsing.DataField;
import yourpackage.parsing.StringField;
import eu.hansolo.tilesfx.Tile;

import java.util.Random;

public class TextDisplayGauge extends Gauge{

    private DataField field;

    public TextDisplayGauge()
    {
        super();

        JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        this.gauge = GaugeType.TextDisplay;

        tile = TileBuilder.create()
            .skinType(Tile.SkinType.TEXT)
            .prefSize(150, 150)
            .title("Text Tile")
            .text("Whatever text")
            .description("May the force be with you\n...always")
            .descriptionAlignment(Pos.TOP_LEFT)
            .textVisible(true)
            .build();
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



