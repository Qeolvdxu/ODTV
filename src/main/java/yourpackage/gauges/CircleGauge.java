package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.Section;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Stop;
import javafx.scene.paint.Color;
import org.opencv.video.Video;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import java.util.Random;




public class CircleGauge extends Gauge {
    private AnimationTimer timer;
    VideoPlayerSwingIntegration videoPlayer;
    public CircleGauge(int angle, String title, NumericDataField dataField, VideoPlayerSwingIntegration vp) {
        super();

        System.out.println(dataField.getMaximum());
        setGaugeTitle(title);

        // Create a JFXPanel for embedding JavaFX content
        JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        System.out.println("videoPlaying() called from CircleGauge. Value: " + videoPlayer.isPlaying());

        // Initialize the tile
        tile = TileBuilder.create()
                .skinType(SkinType.GAUGE2)
                .minSize(250, 250)
                .unit("Unit")
                .textVisible(true)
                .value(0)
                .gradientStops(new Stop(0, Tile.GRAY))
                .strokeWithGradient(true)
                .animated(true)
                .angleRange(angle)
                .maxValue(Math.ceil(dataField.getMaximum()))
                .unit(dataField.getUnit())
                //value(100) // temp
                .build();

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp) {
        // Create JavaFX content (TilesFX tile in this case)
        tile = this.getTile();
        VideoPlayerSwingIntegration videoPlayer = vp;

        if (tile != null) {
            // Create a JavaFX Scene
            Scene scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }

        // Once the data uses a certain threshold, we'll change the color of the gauge by doing this
        // tile.setGradientStops(new Stop(0, Tile.BLUE));
        // tile.setGradientStops(new Stop(0, Tile.RED));

        long lastTimerCall = System.currentTimeMillis();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (now > lastTimerCall + 3_500_000_000L) {
                    // tile.setValue(RND.nextDouble() * tile.getRange() + tile.getMinValue());
                    // This will be were we retrieve the field
                    if(videoPlayer.isPlaying())
                    {
                        System.out.println("CircleGauge can detect that the video is playing!");
                    }
                };
            }


        };

        timer.start();
    }
}
