package yourpackage.gauges;
import eu.hansolo.tilesfx.tools.GradientLookup;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.colors.Bright;
import yourpackage.parsing.DataField;
import yourpackage.parsing.NumericDataField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

import java.io.File;
import java.util.Arrays;

public class CircleGauge extends Gauge {

    private NumericDataField field;
    private transient int alarmIndex;
    private transient GradientLookup gradient;
    private transient Media alarm;
    private double redError;
    public static MediaPlayer mediaPlayer = null;

    public static Tile tile = null;



    public CircleGauge(int angle) {
        super();
        if (angle == 90) {
            this.gauge = GaugeType.Circle90;
        } else if (angle == 180) {
            this.gauge = GaugeType.Circle180;
        } else if (angle == 270) {
            this.gauge = GaugeType.Circle270;
        } else if (angle == 360) {
            this.gauge = GaugeType.Circle360;
        }


        field = null;

        tile.setSkinType(Tile.SkinType.GAUGE);
        tile.setUnit("d");
        tile.setAngleRange(angle);
        tile.setMinValue(0);
        tile.setMaxValue(angle);
        tile.setValue(angle);
        tile.setUnit("MPH");
        tile.setAnimated(true);

        //tile.setMinValueVisible(false);
        //tile.setMaxValueVisible(false);

        gradient = new GradientLookup(Arrays.asList(new Stop(0.25, Bright.BLUE),
                new Stop(0.50, Bright.GREEN),
                new Stop(0.75, Bright.YELLOW),
                new Stop(1, Bright.RED)));

        tile.setGradientStops(gradient.getStops());
        tile.setStrokeWithGradient(true);


        alarm = null;
    }

    public GradientLookup getGradient()
    {
        return gradient;
    }

    public void setGradient(GradientLookup g)
    {
        gradient = g;
        tile.setGradientStops(gradient.getStops());
        redError = g.getStops().get(3).getOffset() * tile.getRange();
    }


    public void update(){
        Double value = field.getNext();

        if (value == null) {
            return;
        }

        double newValue = value.doubleValue();

        double valueInRange = tile.getMinValue() + (tile.getMaxValue() - tile.getMinValue()) * ((newValue)/ (tile.getRange()));
        tile.setValue(valueInRange);

        tile.setBarColor(gradient.getColorAt(newValue / tile.getRange()));

        if (mediaPlayer != null)
        {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING && tile.getValue() < redError)
            {
                mediaPlayer.stop();
                tile.setBackgroundColor(Tile.BACKGROUND);
            }
            else
            {
                if (tile.getValue() >= redError)
                {
                    mediaPlayer.play();
                    tile.setBackgroundColor(Color.DARKRED);
                }
            }
        }
    }

    public static double map (double fValue, double start, double stop, double fStart, double fStop)
    {
        return fStart + (fStop - fStart) * ((fValue - start) / (stop - start));
    }

    public void setAlarm(int i){
        String soundFile;
        redError = map(gradient.getStops().get(3).getOffset(), 0, 1, tile.getMinValue(),tile.getMaxValue());
        alarmIndex = i;
        switch(i)
        {
            case 1, 2: //criticalAlarm
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

    public DataField getField() {
        return field;
    }

    public void setField(NumericDataField field) {
        this.field = field;
    }

    public int getAlarmIndex() {
        return alarmIndex;
    }
}
