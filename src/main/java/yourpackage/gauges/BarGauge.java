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
import javafx.util.Duration;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;
import java.io.File;
public class BarGauge extends Gauge {

    NumericDataField gaugeData;

    public BarGauge(String title, NumericDataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
    {
        super();

        System.out.println(dataField.getMaximum());
        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
         gaugeData = dataField;

        tile = TileBuilder.create()
                .skinType(Tile.SkinType.BAR_GAUGE)
                .prefSize(100, 100)
                .minValue(0)
                .maxValue(Math.ceil(gaugeData.getMaximum()))
                .title(title)
                .animated(true)
                .build();

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, NumericDataField dataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField gaugeData = dataField;

        if (dataField.getUnit() != null) { tile.setUnit(gaugeData.getUnit()); }

        if (redRangeProvided)
        {
            tile.setThreshold(minRedRange);
            tile.setThresholdVisible(true);
        }

        if (tile != null) {
            scene = new Scene(new Pane(tile));
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

                if (redRangeProvided && (currentFieldValue >= minRedRange && currentFieldValue <= maxRedRange)) {
                    tile.setBarColor(Tile.RED);

                    soundPlayer.play();
                } else if (yellowRangeProvided && (currentFieldValue >= minYellowRange && currentFieldValue <= maxYellowRange)) { tile.setBarColor(Tile.YELLOW); }
                else if (greenRangeProvided && (currentFieldValue >= minGreenRange && currentFieldValue <= maxGreenRange)) { tile.setBarColor(Tile.GREEN); }
                else if (blueRangeProvided && (currentFieldValue >= minBlueRange && currentFieldValue <= maxBlueRange)) { tile.setBarColor(Tile.BLUE); }
                else { tile.setBarColor(Tile.GRAY);}

                tile.setValue(currentFieldValue);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }

    public String getDataFieldName() { return gaugeData.getFieldName(); }
}


