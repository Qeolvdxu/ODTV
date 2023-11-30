package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.Tile.ChartType;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;
import java.io.File;
public class XPlotGauge extends Gauge {

    NumericDataField gaugeData;
    public XPlotGauge(String title, NumericDataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency) {
        super();

        System.out.println(dataField.getMaximum());
        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        gaugeData = dataField;

        tile = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(100, 100)
                .title(title)
                .chartType(ChartType.LINE)
                .smoothing(true)
                .build();

        setDefaultSeries();

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, gaugeData, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, NumericDataField dataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField gaugeData = dataField;

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
                double fieldDataRowsLength =  gaugeData.getDataRowsLength() - 1;

                if ((!soundPlaying) && (this.isVisible()))
                {
                    soundPlaying = true;
                    Media sound = new Media(new File(audioFile).toURI().toString());
                    soundPlayer = new MediaPlayer(sound);
                    soundPlayer.setOnEndOfMedia(() -> soundPlaying = false);
                }

                if(mapIndexToInt + 10 <= fieldDataRowsLength)
                {
                    XYChart.Series<String, Number> series = new XYChart.Series();
                    series.setName(dataField.getFieldName());
                    series.getData().add(new XYChart.Data("1", dataField.getIndexOfDouble(mapIndexToInt)));
                    series.getData().add(new XYChart.Data("2", dataField.getIndexOfDouble(mapIndexToInt+1)));
                    series.getData().add(new XYChart.Data("3", dataField.getIndexOfDouble(mapIndexToInt+2)));
                    series.getData().add(new XYChart.Data("4", dataField.getIndexOfDouble(mapIndexToInt+3)));
                    series.getData().add(new XYChart.Data("5", dataField.getIndexOfDouble(mapIndexToInt+4)));
                    series.getData().add(new XYChart.Data("6", dataField.getIndexOfDouble(mapIndexToInt+5)));
                    series.getData().add(new XYChart.Data("7", dataField.getIndexOfDouble(mapIndexToInt+6)));
                    series.getData().add(new XYChart.Data("8", dataField.getIndexOfDouble(mapIndexToInt+7)));
                    series.getData().add(new XYChart.Data("9", dataField.getIndexOfDouble(mapIndexToInt+8)));
                    series.getData().add(new XYChart.Data("10", dataField.getIndexOfDouble(mapIndexToInt+9)));


                    if (redRangeProvided && (currentFieldValue >= minRedRange && currentFieldValue <= maxRedRange)) {
                        tile.setTilesFXSeries(new TilesFXSeries<>(series, Tile.RED));
                        soundPlayer.play();
                    } else if (yellowRangeProvided && (currentFieldValue >= minYellowRange && currentFieldValue <= maxYellowRange)) { tile.setTilesFXSeries(new TilesFXSeries<>(series, Tile.YELLOW)); }
                    else if (greenRangeProvided && (currentFieldValue >= minGreenRange && currentFieldValue <= maxGreenRange)) { tile.setTilesFXSeries(new TilesFXSeries<>(series, Tile.GREEN)); }
                    else if (blueRangeProvided && (currentFieldValue >= minBlueRange && currentFieldValue <= maxBlueRange)) { tile.setTilesFXSeries(new TilesFXSeries<>(series, Tile.BLUE)); }
                    else { tile.setTilesFXSeries(new TilesFXSeries<>(series, Tile.GRAY));}
                }
            } else if (tile != null) {
                setDefaultSeries();
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        timeline.setRate(rate);
    }

    private void setDefaultSeries()
    {
        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName(gaugeData.getFieldName());
        series.getData().add(new XYChart.Data("1", 0));
        series.getData().add(new XYChart.Data("2", 0));
        series.getData().add(new XYChart.Data("3", 0));
        series.getData().add(new XYChart.Data("4", 0));
        series.getData().add(new XYChart.Data("5", 0));
        series.getData().add(new XYChart.Data("6", 0));
        series.getData().add(new XYChart.Data("7", 0));
        series.getData().add(new XYChart.Data("8", 0));
        series.getData().add(new XYChart.Data("9", 0));
        series.getData().add(new XYChart.Data("10", 0));
        tile.setTilesFXSeries(new TilesFXSeries<>(series, Tile.GRAY));
    }
}

