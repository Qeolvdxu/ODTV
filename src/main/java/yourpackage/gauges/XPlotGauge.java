package yourpackage.gauges;

import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import javax.swing.text.Element;
import java.util.ArrayList;
import java.util.List;


public class XPlotGauge extends Gauge {

    private NumericDataField field;

    private XYChart.Series<Number, Number> series;
    public NumberAxis primaryAxis;
    public NumberAxis subAxis;
    private ScatterChart<Number,Number> scatterChart;
    public GaugeOrientation orient;

    public XPlotGauge(GaugeOrientation orient, double lowerBound, double upperBound, double tickUnit)
    {
        super();
        this.gauge = GaugeType.XPlot;

        series = new XYChart.Series();

        tile.setTitle("Line Plot Gauge");

        tile.setAnimated(true);
        tile.setRunning(true);
        tile.setActive(true);

        tile.setMaxValue(upperBound);
        tile.setMinValue(lowerBound);

        primaryAxis = new NumberAxis(lowerBound, upperBound, tickUnit);
        subAxis = new NumberAxis(0, 2, 0);

        switch (orient)
        {
            case VERTICAL:
                scatterChart = new ScatterChart<Number, Number>(subAxis, primaryAxis);
                this.orient = GaugeOrientation.VERTICAL;
                break;
            default:
                scatterChart = new ScatterChart<Number, Number>(primaryAxis,subAxis);
                this.orient = GaugeOrientation.HORIZONTAL;
                break;
        }

        scatterChart.setVerticalZeroLineVisible(false);
        scatterChart.setVerticalGridLinesVisible(false);
        scatterChart.setHorizontalZeroLineVisible(false);
        scatterChart.setHorizontalGridLinesVisible(false);

        primaryAxis.setLabel("X Axis Label");
        subAxis.setTickLabelsVisible(false);

        scatterChart.getData().addAll(series);

        tile.setGraphic(scatterChart);

    }

    public static double map (double fValue, double start, double stop, double fStart, double fStop)
    {
        return fStart + (fStop - fStart) * ((fValue - start) / (stop - start));
    }

    public void update() {
        Double newVal_o = field.getNext();

        if (newVal_o == null) {
            return;
        }

        double newValue = newVal_o.doubleValue();
        double newValueInRange = map(newValue, 0, tile.getRange(), tile.getMinValue(), tile.getMaxValue());
        switch (orient)
        {
            case VERTICAL:
                series.getData().add(new XYChart.Data(1, newValueInRange));
                break;
            default:
                series.getData().add(new XYChart.Data(newValueInRange, 1));
                break;
        }
        if(series.getData().size() > 5)
        {
            series.getData().remove(0);
        }
    }

    public void setLabel(String label)
    {
        primaryAxis.setLabel(label);
    }


    public DataField getField() { return field; }

    public void setField(NumericDataField field) {
        this.field = field;
    }
}

