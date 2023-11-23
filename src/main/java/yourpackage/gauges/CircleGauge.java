package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Stop;

import java.util.Random;


public class CircleGauge extends Gauge {
    private AnimationTimer  timer;
    private static final    Random RND = new Random();
    public CircleGauge(int angle, String title) {
        super();

        setGaugeTitle(title);

        // Create a JFXPanel for embedding JavaFX content
        JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        // Initialize the tile
        tile = TileBuilder.create()
                .skinType(SkinType.GAUGE2)
                .minSize(250, 250)
                .unit("Unit")
                .textVisible(true)
                .value(0)
                .gradientStops(new Stop(0.25, Tile.BLUE),
                        new Stop(0.50, Tile.GREEN),
                        new Stop(0.75, Tile.YELLOW),
                        new Stop(1, Tile.RED))
                .strokeWithGradient(true)
                .animated(true)
                .angleRange(angle)
                .maxValue(200)
                .value(100) // temp
                .build();

        Platform.runLater(() -> initFX(jfxPanel));
    }

    private void initFX(JFXPanel jfxPanel) {
        // Create JavaFX content (TilesFX tile in this case)
        tile = this.getTile();

        if (tile != null) {
            // Create a JavaFX Scene
            Scene scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }

        long lastTimerCall = System.currentTimeMillis();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + 3_500_000_000L) {
                    // tile.setValue(RND.nextDouble() * tile.getRange() + tile.getMinValue());
                    // This will be were we retrieve the field
                };
            }


        };

        // timer.start();
    }
}
