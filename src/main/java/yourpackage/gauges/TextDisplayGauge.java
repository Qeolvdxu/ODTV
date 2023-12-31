package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import yourpackage.parsing.BooleanDataField;
import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import java.io.File;

public class TextDisplayGauge extends Gauge {

    DataField gaugeData;

    public TextDisplayGauge(String title, DataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
    {
        super();

        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        gaugeData = dataField;

        tile = TileBuilder.create()
                .skinType(Tile.SkinType.TEXT)
                .prefSize(100, 100)
                .title(title)
                .titleAlignment(TextAlignment.CENTER)
                .descriptionAlignment(Pos.CENTER)
                .textVisible(true)
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

                if (gaugeData instanceof BooleanDataField && mapIndexToInt > ((BooleanDataField) gaugeData).getBooleanDataRowsLength() - 1) {
                    mapIndexToInt = ((BooleanDataField) gaugeData).getBooleanDataRowsLength() - 1; // Had to do this since getDataRowsLength was always returning 0 for Booleans.
                } else if (!(gaugeData instanceof BooleanDataField) && mapIndexToInt > gaugeData.getDataRowsLength() - 1) { mapIndexToInt = gaugeData.getDataRowsLength() - 1; }

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
                        tile.setTextColor(Tile.RED);
                        soundPlayer.play();
                    } else if (yellowRangeProvided && (currentNumericFieldValue >= minYellowRange && currentNumericFieldValue <= maxYellowRange)) { tile.setTextColor(Tile.YELLOW); }
                    else if (greenRangeProvided && (currentNumericFieldValue >= minGreenRange && currentNumericFieldValue <= maxGreenRange)) { tile.setTextColor(Tile.GREEN); }
                    else if (blueRangeProvided && (currentNumericFieldValue >= minBlueRange && currentNumericFieldValue <= maxBlueRange)) { tile.setTextColor(Tile.BLUE); }
                    else { tile.setTextColor(Tile.GRAY); }
                    tile.setDescription(String.valueOf(currentNumericFieldValue));
                } else if (gaugeData instanceof BooleanDataField) {
                    BooleanDataField toBool = (BooleanDataField) dataField;
                    boolean currentBooleanFieldValue = toBool.getIndexOfBool(mapIndexToInt);

                    if (currentBooleanFieldValue) { tile.setDescription("True"); }
                    else { tile.setDescription("False"); }
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

    public String getDataFieldName() { return gaugeData.getFieldName(); }
}