package yourpackage.gauges;

import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import eu.hansolo.tilesfx.Tile.ChartType;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import yourpackage.parsing.NumericDataField;
import yourpackage.visualization.VideoPlayerSwingIntegration;

import java.util.Random;


public class XPlotGauge extends Gauge {

    public XPlotGauge(String title, NumericDataField dataField, VideoPlayerSwingIntegration vp, double dataFrequency)
    {
        super();

        System.out.println(dataField.getMaximum());
        updateFrequency = dataFrequency;
        setGaugeTitle(title);

        jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        VideoPlayerSwingIntegration videoPlayer = vp;
        NumericDataField gaugeData = dataField;

        // The default series
        XYChart.Series<String, Number> series = new XYChart.Series();
        series.setName("Name");
        series.getData().add(new XYChart.Data("0.1", 0));
        series.getData().add(new XYChart.Data("0.2", 0));
        series.getData().add(new XYChart.Data("0.3", 0));
        series.getData().add(new XYChart.Data("0.4", 0));
        series.getData().add(new XYChart.Data("0.5", 0));
        series.getData().add(new XYChart.Data("0.6", 0));
        series.getData().add(new XYChart.Data("0.7", 0));
        series.getData().add(new XYChart.Data("0.8", 0));
        series.getData().add(new XYChart.Data("0.9", 0));
        series.getData().add(new XYChart.Data("1,0", 0));

        tile = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(100, 100)
                .title("Title")
                .chartType(ChartType.LINE)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(series,
                        Tile.GRAY))
                .build();

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


    }
}

