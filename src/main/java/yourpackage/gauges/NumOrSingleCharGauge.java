package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import yourpackage.app.Main;
import yourpackage.parsing.DataField;
import yourpackage.parsing.StringField;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class NumOrSingleCharGauge extends Gauge{

    private StringField field;

    public NumOrSingleCharGauge()
    {
        super();

        JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        this.gauge = GaugeType.NumOrSingleChar;
        field = null;

        tile = TileBuilder.create()
            .skinType(Tile.SkinType.CHARACTER)
            .title("Character Gauge")
            .titleAlignment(TextAlignment.CENTER)
            .description("C")
            .build();

        Platform.runLater(() -> initFX(jfxPanel));
    }

    private void initFX(JFXPanel jfxPanel) {
        tile = this.getTile();

        if (tile != null) {
            System.out.println("Creating a new gauge.");
            Scene scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }
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