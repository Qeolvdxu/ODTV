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

public class TextDisplayGauge extends Gauge {

    NumericDataField gaugeData;

    public TextDisplayGauge(String title, NumericDataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
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
                .skinType(Tile.SkinType.TEXT)
                .prefSize(100, 100)
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

                    soundPlayer.play();

                tile.setValue(currentFieldValue);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }

    public String getDataFieldName() { return gaugeData.getFieldName(); }
}



