package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
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
import javafx.scene.paint.Color;
import javafx.util.Duration;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import java.io.File;

public class XByYPlotGauge extends Gauge {
    public XByYPlotGauge(String title, NumericDataField xDataField, NumericDataField yDataField, VideoPlayerSwingIntegration vp, double dataFrequency) {
        super();

        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        xGaugeData = xDataField;
        yGaugeData = yDataField;

        tile = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(100, 100)
                .title(title)
                .chartType(Tile.ChartType.LINE)
                .smoothing(true)
                .build();

        setDefaultSeries();

        Platform.runLater(() -> initFX(jfxPanel, videoPlayer, xDataField, yDataField, tile));
    }

    private void initFX(JFXPanel jfxPanel, VideoPlayerSwingIntegration vp, NumericDataField xDataField, NumericDataField yDataField, Tile inputtile) {
        tile = inputtile;
        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField xGaugeData = xDataField;
        NumericDataField yGaugeData = yDataField;

        if (tile != null) {
            scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }

        double rate = 1 / updateFrequency;

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(videoPlayer.isPlaying()) {
                double mapIndex = videoPlayer.getCurrentTimeInSeconds() * (1/updateFrequency);
                int mapIndexToInt = (int) Math.round(mapIndex);
                if (mapIndexToInt > xGaugeData.getDataRowsLength() - 1)
                {
                    mapIndexToInt = xGaugeData.getDataRowsLength() - 1;
                }
                double currentFieldValue = xGaugeData.getIndexOfDouble(mapIndexToInt);
                double fieldDataRowsLength =  xGaugeData.getDataRowsLength() - 1;

                if ((!soundPlaying) && (this.isVisible()))
                {
                    soundPlaying = true;
                    Media sound = new Media(new File(audioFile).toURI().toString());
                    soundPlayer = new MediaPlayer(sound);
                    soundPlayer.setOnEndOfMedia(() -> soundPlaying = false);
                }

                if(mapIndexToInt + 10 <= fieldDataRowsLength)
                {
                    XYChart.Series<String, Number> seriesx = new XYChart.Series();
                    seriesx.setName(xGaugeData.getFieldName());
                    seriesx.getData().add(new XYChart.Data("1", xGaugeData.getIndexOfDouble(mapIndexToInt)));
                    seriesx.getData().add(new XYChart.Data("2", xGaugeData.getIndexOfDouble(mapIndexToInt+1)));
                    seriesx.getData().add(new XYChart.Data("3", xGaugeData.getIndexOfDouble(mapIndexToInt+2)));
                    seriesx.getData().add(new XYChart.Data("4", xGaugeData.getIndexOfDouble(mapIndexToInt+3)));
                    seriesx.getData().add(new XYChart.Data("5", xGaugeData.getIndexOfDouble(mapIndexToInt+4)));
                    seriesx.getData().add(new XYChart.Data("6", xGaugeData.getIndexOfDouble(mapIndexToInt+5)));
                    seriesx.getData().add(new XYChart.Data("7", xGaugeData.getIndexOfDouble(mapIndexToInt+6)));
                    seriesx.getData().add(new XYChart.Data("8", xGaugeData.getIndexOfDouble(mapIndexToInt+7)));
                    seriesx.getData().add(new XYChart.Data("9", xGaugeData.getIndexOfDouble(mapIndexToInt+8)));
                    seriesx.getData().add(new XYChart.Data("10", xGaugeData.getIndexOfDouble(mapIndexToInt+9)));
                    XYChart.Series<String, Number> seriesy = new XYChart.Series();
                    seriesy.setName(yGaugeData.getFieldName());
                    seriesy.getData().add(new XYChart.Data("1", yGaugeData.getIndexOfDouble(mapIndexToInt)));
                    seriesy.getData().add(new XYChart.Data("2", yGaugeData.getIndexOfDouble(mapIndexToInt+1)));
                    seriesy.getData().add(new XYChart.Data("3", yGaugeData.getIndexOfDouble(mapIndexToInt+2)));
                    seriesy.getData().add(new XYChart.Data("4", yGaugeData.getIndexOfDouble(mapIndexToInt+3)));
                    seriesy.getData().add(new XYChart.Data("5", yGaugeData.getIndexOfDouble(mapIndexToInt+4)));
                    seriesy.getData().add(new XYChart.Data("6", yGaugeData.getIndexOfDouble(mapIndexToInt+5)));
                    seriesy.getData().add(new XYChart.Data("7", yGaugeData.getIndexOfDouble(mapIndexToInt+6)));
                    seriesy.getData().add(new XYChart.Data("8", yGaugeData.getIndexOfDouble(mapIndexToInt+7)));
                    seriesy.getData().add(new XYChart.Data("9", yGaugeData.getIndexOfDouble(mapIndexToInt+8)));
                    seriesy.getData().add(new XYChart.Data("10", yGaugeData.getIndexOfDouble(mapIndexToInt+9)));


                    if (redRangeProvided && (currentFieldValue >= minRedRange && currentFieldValue <= maxRedRange)) {
                        tile.setTilesFXSeries(new TilesFXSeries<>(seriesx, Tile.RED), new TilesFXSeries<>(seriesy, Color.WHITE));
                        soundPlayer.play();
                    } else if (yellowRangeProvided && (currentFieldValue >= minYellowRange && currentFieldValue <= maxYellowRange)) { tile.setTilesFXSeries(new TilesFXSeries<>(seriesx, Tile.YELLOW), new TilesFXSeries<>(seriesy, Color.WHITE)); }
                    else if (greenRangeProvided && (currentFieldValue >= minGreenRange && currentFieldValue <= maxGreenRange)) { tile.setTilesFXSeries(new TilesFXSeries<>(seriesx, Tile.GREEN), new TilesFXSeries<>(seriesy, Color.WHITE)); }
                    else if (blueRangeProvided && (currentFieldValue >= minBlueRange && currentFieldValue <= maxBlueRange)) { tile.setTilesFXSeries(new TilesFXSeries<>(seriesx, Tile.BLUE), new TilesFXSeries<>(seriesy, Color.WHITE)); }
                    else { tile.setTilesFXSeries(new TilesFXSeries<>(seriesx, Tile.GRAY), new TilesFXSeries<>(seriesy, Color.WHITE));}
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
        XYChart.Series<String, Number> seriesx = new XYChart.Series();
        seriesx.setName(xGaugeData.getFieldName());
        seriesx.getData().add(new XYChart.Data("1", 0));
        seriesx.getData().add(new XYChart.Data("2", 0));
        seriesx.getData().add(new XYChart.Data("3", 0));
        seriesx.getData().add(new XYChart.Data("4", 0));
        seriesx.getData().add(new XYChart.Data("5", 0));
        seriesx.getData().add(new XYChart.Data("6", 0));
        seriesx.getData().add(new XYChart.Data("7", 0));
        seriesx.getData().add(new XYChart.Data("8", 0));
        seriesx.getData().add(new XYChart.Data("9", 0));
        seriesx.getData().add(new XYChart.Data("10", 0));
        XYChart.Series<String, Number> seriesy = new XYChart.Series();
        seriesy.setName(yGaugeData.getFieldName());
        seriesy.getData().add(new XYChart.Data("1", 0));
        seriesy.getData().add(new XYChart.Data("2", 0));
        seriesy.getData().add(new XYChart.Data("3", 0));
        seriesy.getData().add(new XYChart.Data("4", 0));
        seriesy.getData().add(new XYChart.Data("5", 0));
        seriesy.getData().add(new XYChart.Data("6", 0));
        seriesy.getData().add(new XYChart.Data("7", 0));
        seriesy.getData().add(new XYChart.Data("8", 0));
        seriesy.getData().add(new XYChart.Data("9", 0));
        seriesy.getData().add(new XYChart.Data("10", 0));
        tile.setTilesFXSeries(new TilesFXSeries<>(seriesx, Tile.GRAY), new TilesFXSeries<>(seriesy, Color.WHITE));
    }
}
