package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.Animation;
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

public class BarGauge extends Gauge {

    public BarGauge(String title, NumericDataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
    {
        super();

        System.out.println(dataField.getMaximum());
        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        // Create a JFXPanel for embedding JavaFX content
        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField gaugeData = dataField;

        tile = TileBuilder.create()
                .skinType(Tile.SkinType.BAR_GAUGE)
                .prefSize(100, 100)
                .minValue(0)
                .maxValue(Math.ceil(gaugeData.getMaximum()))
                .title("BarGauge Tile")
                .gradientStops(new Stop(0, Tile.GRAY))
                .strokeWithGradient(true)
                .animated(true)
                .build();

        if (redRangeProvided)
        {
            tile.setThreshold(minRedRange);
            tile.setThresholdVisible(true);
        }

        if (dataField.getUnit() != null) {
            tile.setUnit(gaugeData.getUnit());
        } else { tile.setUnit(" "); }

        if (dataField.getMaximum() < 0)
        {
            tile.setMinValue(Math.floor(dataField.getMinimum()));
        }

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, NumericDataField dataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField gaugeData = dataField;

        if (tile != null) {
            Scene scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }

        double rate = 1 / updateFrequency;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(videoPlayer.isPlaying()) {
                double mapIndex = videoPlayer.getCurrentTimeInSeconds() * (1/updateFrequency);
                int mapIndexToInt = (int) Math.round(mapIndex);
                if (mapIndexToInt > gaugeData.getDataRowsLength() - 1)
                {
                    mapIndexToInt = gaugeData.getDataRowsLength() - 1;
                }
                double currentFieldValue = dataField.getIndexOfDouble(mapIndexToInt);

                if ((!soundPlaying) && (this.isVisible()))
                {
                    soundPlaying = true;
                    Media sound = new Media(new File(audioFile).toURI().toString());
                    soundPlayer = new MediaPlayer(sound);
                    soundPlayer.setOnEndOfMedia(() -> soundPlaying = false);
                }


                // The way I am setting the color for the gauge here won't work with this gauge. I need to do something like this in the tile dec.
                //.gradientStops(new Stop(0, Tile.GRAY),
                //        new Stop(0.2, Tile.BLUE),
                //        new Stop(0.4, Tile.GREEN),
                //        new Stop(0.6, Tile.YELLOW),
                //        new Stop(0.8, Tile.GREEN))


                if (redRangeProvided && (currentFieldValue >= minRedRange && currentFieldValue <= maxRedRange)) {
                    tile.setGradientStops(new Stop(0, Tile.RED));
                    soundPlayer.play();
                } else if (yellowRangeProvided && (currentFieldValue >= minYellowRange && currentFieldValue <= maxYellowRange)) { tile.setGradientStops(new Stop(0, Tile.YELLOW)); }
                else if (greenRangeProvided && (currentFieldValue >= minGreenRange && currentFieldValue <= maxGreenRange)) { tile.setGradientStops(new Stop(0, Tile.GREEN)); }
                else if (blueRangeProvided && (currentFieldValue >= minBlueRange && currentFieldValue <= maxBlueRange)) { tile.setGradientStops(new Stop(0, Tile.BLUE)); }
                else { tile.setGradientStops(new Stop(0, Tile.GRAY)); }

                tile.setValue(currentFieldValue);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }
}


