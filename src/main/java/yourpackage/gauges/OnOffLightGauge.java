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
import yourpackage.parsing.BooleanDataField;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import java.io.File;

public class OnOffLightGauge extends Gauge {

    BooleanDataField gaugeData;

    public OnOffLightGauge(String title, BooleanDataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
    {
        super();

        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        gaugeData = dataField;

        tile = TileBuilder.create()
                .skinType(Tile.SkinType.LED)
                .prefSize(100, 100)
                .title(title)
                .animated(true)
                .build();

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, BooleanDataField dataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        BooleanDataField gaugeData = dataField;

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

                if ((!soundPlaying) && (this.isVisible()))
                {
                    soundPlaying = true;
                    Media sound = new Media(new File(audioFile).toURI().toString());
                    soundPlayer = new MediaPlayer(sound);
                    soundPlayer.setOnEndOfMedia(() -> soundPlaying = false);
                }

                    soundPlayer.play();

                tile.setValue(0); // temporary
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }

    public String getDataFieldName() { return gaugeData.getFieldName(); }
}