package yourpackage.gauges;

import eu.hansolo.tilesfx.TileBuilder;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
//import dronetelemetrytool.skins.SingleBarTileSkin;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.colors.Bright;
import eu.hansolo.toolboxfx.GradientLookup;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

import java.io.File;
import java.util.Arrays;

public class BarGauge extends Gauge{

    private NumericDataField field;

    private ChartData data;
    //private GradientLookup gradient;
    private transient int alarmIndex;
    private Media alarm;
    private double redError;
    public static MediaPlayer mediaPlayer = null;

    public BarGauge(String title, double min, double max, double green, double yellow, double red, String unit)
    {
        super();

        JFXPanel jfxPanel = new JFXPanel();
        frame.add(jfxPanel);

        this.gauge = GaugeType.Bar;
        field = null;

        tile = TileBuilder.create()
            .prefSize(150*2, 150)
            .title(title)
            .maxValue(max)
            .minValue(min)
            .strokeWithGradient(true)
            .build();

        data = new ChartData("");
        //data.setFormatString("%.1f " + unit);
        //data.setMinValue(min);
        //data.setMaxValue(max);
        //data.setGradientLookup(new GradientLookup((Arrays.asList(
        //        new Stop(0, Bright.BLUE),
        //        new Stop(map(green,min,max,0,1), Bright.GREEN),
        //        new Stop(map(yellow,min,max,0,1), Bright.YELLOW),
        //        new Stop(map(red,min,max,0,1), Bright.RED),
        //        new Stop(1, Bright.RED)))));
        tile.addChartData(data);

        redError = 0.9 * tile.getMaxValue();
        alarm = null;
        mediaPlayer = null;

        Platform.runLater(() -> initFX(jfxPanel));
    }

    private void initFX(JFXPanel jfxPanel) {
        tile = this.getTile();

        if (tile != null) {
            System.out.println("Creating a new gauge.");
            Scene scene = new Scene(new Pane(tile));
            jfxPanel.setScene(scene);
        }
    }

    public static double map (double fValue, double start, double stop, double fStart, double fStop)
    {
        return fStart + (fStop - fStart) * ((fValue - start) / (stop - start));
    }



    public void update() {
        Double newValue = field.getNext();

        if (newValue == null) {
            return;
        }

        data.setValue(newValue.doubleValue());

        if (mediaPlayer != null)
        {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && data.getValue() < redError)
            {
                mediaPlayer.stop();
                tile.setBackgroundColor(Tile.BACKGROUND);
            }
            else
            {
                if (data.getValue() >= redError)
                {
                    mediaPlayer.play();
                    tile.setBackgroundColor(Color.DARKRED);
                }
            }
        }
    }


    public void setAlarm(int i) {
        String soundFile;
        //redError = map(data.getGradientLookup().getStops().get(3).getOffset(), 0, 1, tile.getMinValue(),tile.getMaxValue());
        alarmIndex = i;
        switch(i)
        {
            case 1,2: //criticalAlarm
                soundFile = "src/main/resources/criticalAlarm.wav";
                alarm = new Media(new File(soundFile).toURI().toString());
                mediaPlayer = new MediaPlayer(alarm);
                mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                break;
            default:
                //no alarm
                break;
        }
    }

    public DataField getField() { return field; }

    public void setField(NumericDataField field) {
        this.field = field;
    }

    public int getAlarmIndex() {
        return alarmIndex;
    }
}

