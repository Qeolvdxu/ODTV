package yourpackage.gauges;

import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

public class XByYPlotGauge extends Gauge {

    private NumericDataField xField;
    private NumericDataField yField;

    private XYChart.Series<Number, Number> series;

    public NumberAxis xAxis;
    public NumberAxis yAxis;
    private ScatterChart<Number,Number> scatterChart;

    public XByYPlotGauge(double xMin, double xMax, double xTick, double yMin, double yMax, double yTick)
    {
        super();
        this.gauge = GaugeType.XByYPLOT;

        // LineChart Data
        series = new XYChart.Series();

        tile.setTitle("XYPlot Gauge");

        tile.setAnimated(true);
        tile.setRunning(true);
        tile.setActive(true);

        xAxis = new NumberAxis(xMin, xMax, xTick);
        yAxis = new NumberAxis(yMin, yMax, yTick);
        xAxis.setLabel("X");
        yAxis.setLabel("Y");

        scatterChart = new ScatterChart<Number,Number>(xAxis,yAxis);
        scatterChart.getData().addAll(series);

        tile.setGraphic(scatterChart);

    }

    public DataField getField() {
        return null;
    }

    public void update() {

        Double xValue = xField.getNext();
        Double yValue = yField.getNext();

        if (xValue == null || yValue == null) {
            return;
        }

        double newXVal = xValue.doubleValue();
        double newYVal = yValue.doubleValue();

        series.getData().add(new XYChart.Data(newXVal, newYVal));
        if(series.getData().size() > 5)
        {
            series.getData().remove(0);
        }
    }

    public void setXLabel(String label)
    {
        xAxis.setLabel(label);
    }
    public void setYLabel(String label) { yAxis.setLabel(label); }

    public NumericDataField getxField() {
        return xField;
    }

    public void setxField(NumericDataField xField) {
        this.xField = xField;
    }

    public NumericDataField getyField() {
        return yField;
    }

    public void setyField(NumericDataField yField) {
        this.yField = yField;
    }
}
