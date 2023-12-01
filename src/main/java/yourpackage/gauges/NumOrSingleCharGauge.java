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
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import javax.xml.crypto.Data;
import java.io.File;

public class NumOrSingleCharGauge extends Gauge {
    public NumOrSingleCharGauge(String title, DataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
    {
        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        DataField gaugeData = dataField;

        tile = TileBuilder.create().skinType(Tile.SkinType.CHARACTER)
                .prefSize(100, 100)
                .title(title)
                .titleAlignment(TextAlignment.CENTER)
                .descriptionColor(Tile.GRAY)
                .build();

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, DataField dataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        DataField gaugeData = dataField;

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


                if(gaugeData instanceof NumericDataField) {
                    NumericDataField toNumeric = (NumericDataField) dataField;
                    double currentNumericFieldValue = toNumeric.getIndexOfDouble(mapIndexToInt);

                    if ((!soundPlaying) && (this.isVisible()))
                    {
                        soundPlaying = true;
                        Media sound = new Media(new File(audioFile).toURI().toString());
                        soundPlayer = new MediaPlayer(sound);
                        soundPlayer.setOnEndOfMedia(() -> soundPlaying = false);
                    }

                    if (redRangeProvided && (currentNumericFieldValue >= minRedRange && currentNumericFieldValue <= maxRedRange)) {
                        tile.setDescriptionColor(Tile.RED);
                        soundPlayer.play();
                    } else if (yellowRangeProvided && (currentNumericFieldValue >= minYellowRange && currentNumericFieldValue <= maxYellowRange)) { tile.setDescriptionColor(Tile.YELLOW); }
                    else if (greenRangeProvided && (currentNumericFieldValue >= minGreenRange && currentNumericFieldValue <= maxGreenRange)) { tile.setDescriptionColor(Tile.GREEN); }
                    else if (blueRangeProvided && (currentNumericFieldValue >= minBlueRange && currentNumericFieldValue <= maxBlueRange)) { tile.setDescriptionColor(Tile.BLUE); }
                    else { tile.setDescriptionColor(Tile.GRAY); }
                    tile.setDescription(String.valueOf(currentNumericFieldValue));
                } else {
                    String currentFieldValue = dataField.getIndexOfString(mapIndexToInt);
                    tile.setDescription(currentFieldValue);
                }
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }
}