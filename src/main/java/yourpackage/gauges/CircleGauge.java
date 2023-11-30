package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Stop;
import javafx.util.Duration;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import java.io.File;


public class CircleGauge extends Gauge {
    private AnimationTimer timer;
    VideoPlayerSwingIntegration videoPlayer;
    public CircleGauge(int angle, String title, NumericDataField dataField, VideoPlayerSwingIntegration vp) {
        super();

        System.out.println(dataField.getMaximum());
        setGaugeTitle(title);

        // Create a JFXPanel for embedding JavaFX content
        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField gaugeData = dataField;

        // Initialize the tile
        tile = TileBuilder.create()
                .skinType(SkinType.GAUGE2)
                .prefSize(50, 50)
                .unit("Unit")
                .title(title)
                .textVisible(true)
                .value(0)
                .gradientStops(new Stop(0, Tile.GRAY))
                .strokeWithGradient(true)
                .animated(true)
                .angleRange(angle)
                .maxValue(Math.ceil(gaugeData.getMaximum()))
                .unit(gaugeData.getUnit())
                .build();



        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, NumericDataField dataField, Tile inputtile) {
        // Create JavaFX content (TilesFX tile in this case)
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField gaugeData = dataField;

        if (tile != null) {
            // Create a JavaFX Scene
            Scene scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }



        double interval = 0.1; // temporary, user will later be able to provide own value.
        double rate = 1 / interval;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(videoPlayer.isPlaying()) {
                double mapIndex = videoPlayer.getCurrentTimeInSeconds() * (1/interval);
                int mapIndexToInt = (int) Math.round(mapIndex);
                if (mapIndexToInt > gaugeData.getDataRowsLength() - 1)
                {
                    mapIndexToInt = gaugeData.getDataRowsLength() - 1;
                }
                double currentFieldValue = dataField.getIndexOfDouble(mapIndexToInt);


                if (!soundPlaying)
                {
                    soundPlaying = true;
                    Media sound = new Media(new File(audioFile).toURI().toString());
                    soundPlayer = new MediaPlayer(sound);
                    soundPlayer.setOnEndOfMedia(() -> soundPlaying = false);
                }

                if (blueRangeProvided && (currentFieldValue >= minBlueRange && currentFieldValue <= maxBlueRange)) { tile.setGradientStops(new Stop(0, Tile.BLUE)); }
                else if (greenRangeProvided && (currentFieldValue >= minGreenRange && currentFieldValue <= maxGreenRange)) { tile.setGradientStops(new Stop(0, Tile.GREEN)); }
                else if (yellowRangeProvided && (currentFieldValue >= minYellowRange && currentFieldValue <= maxYellowRange)) { tile.setGradientStops(new Stop(0, Tile.YELLOW)); }
                else if (redRangeProvided && (currentFieldValue >= minRedRange && currentFieldValue <= maxRedRange)) {
                    tile.setGradientStops(new Stop(0, Tile.RED));
                    soundPlayer.play();
                }
                else { tile.setGradientStops(new Stop(0, Tile.GRAY)); }

                tile.setValue(currentFieldValue);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }
}
