package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.SkinType;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import yourpackage.parsing.BooleanDataField;
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
                .skinType(SkinType.LED)
                .prefSize(100, 100)
                .title(title)
                .build();

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, BooleanDataField dataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        BooleanDataField gaugeData = dataField;

        if (tile != null) {
            scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }

        double rate = 1 / updateFrequency;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(videoPlayer.isPlaying()) {
                double mapIndex = videoPlayer.getCurrentTimeInSeconds() * (1/updateFrequency);
                int mapIndexToInt = (int) Math.round(mapIndex);

                if (mapIndexToInt > gaugeData.getBooleanDataRowsLength() - 1) {
                    mapIndexToInt = gaugeData.getBooleanDataRowsLength() - 1;
                }

                boolean currentBooleanFieldValue = gaugeData.getIndexOfBool(mapIndexToInt);

                tile.setActive(currentBooleanFieldValue);
            } else {
                tile.setActive(false);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }
    public String getDataFieldName() { return gaugeData.getFieldName(); }
}