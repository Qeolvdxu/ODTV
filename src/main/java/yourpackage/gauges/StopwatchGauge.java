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
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import yourpackage.parsing.NumericDataField;
import yourpackage.parsing.TimeDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import java.io.File;

public class StopwatchGauge extends Gauge {

    TimeDataField gaugeData;

    public StopwatchGauge(String title, TimeDataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
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
                .descriptionColor(Tile.GRAY)
                .textVisible(true)
                .textAlignment(TextAlignment.CENTER)
                .build();


        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, TimeDataField dataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        TimeDataField gaugeData = dataField;

        if (tile != null) {
            scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }

        double rate = 1 / updateFrequency;

        tile.setDescription(dataField.getTime(0));
        tile.setText(dataField.getDate(0));

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(videoPlayer.isPlaying()) {
                double mapIndex = videoPlayer.getCurrentTimeInSeconds() * (1/updateFrequency);
                int mapIndexToInt = (int) Math.round(mapIndex);
                if (mapIndexToInt > gaugeData.getDataRowsLength() - 1)
                {
                    mapIndexToInt = gaugeData.getDataRowsLength() - 1;
                }
                String currentTime = dataField.getTime(mapIndexToInt);
                String currentDate =  dataField.getDate(mapIndexToInt);

                tile.setDescription(currentTime);
                tile.setText(currentDate);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }

    public String getDataFieldName() { return gaugeData.getFieldName(); }
}
