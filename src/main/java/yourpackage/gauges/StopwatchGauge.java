package yourpackage.gauges;

import eu.hansolo.tilesfx.TileBuilder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import yourpackage.parsing.DataField;
import yourpackage.parsing.TimeField;
import eu.hansolo.tilesfx.Tile;

public class StopwatchGauge extends Gauge{

    private TimeField field;

    public StopwatchGauge()
    {
        super();

        JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        this.gauge = GaugeType.Stopwatch;

        tile = TileBuilder.create()
                .title("Clock Gauge")
                .skinType(Tile.SkinType.CLOCK)
                //.time(System.currentTimeMillis() / 1000);
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