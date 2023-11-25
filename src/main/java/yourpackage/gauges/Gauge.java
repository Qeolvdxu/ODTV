package yourpackage.gauges;


import java.awt.Dimension;
import javax.swing.JFrame;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import yourpackage.parsing.DataField;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import javafx.beans.value.ChangeListener;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Gauge {
    private String name;
    private float size;
    private float xpos;
    private float ypos;
    private DataField correspondingField;
    private float blueLimit;
    private float greenLimit;
    private float yellowLimit;
    private float redLimit;
    public static Tile tile = null;

    public JFrame frame;

    protected double minBlueRange, maxBlueRange, minGreenRange, maxGreenRange, minYellowRange, maxYellowRange, minRedRange, maxRedRange;
    protected boolean blueRangeProvided = false;
    protected boolean greenRangeProvided = false;
    protected boolean yellowRangeProvided = false;
    protected boolean redRangeProvided = false;

    public Gauge()
    {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.pack();
        frame.setMinimumSize(new Dimension(250, 250));
        frame.setTitle("gaugeName");
        frame.setVisible(true);


    }

    public void setGaugeTitle(String title)
    {
        frame.setTitle(title);
    }



    public enum GaugeType {
        Circle,
        Circle90,
        Circle180,
        Circle270,
        Circle360,
        Bar,
        XPlot,
        XByYPLOT,
        NumOrSingleChar,
        TextDisplay,
        Stopwatch,
        OnOffLight,
    }
    public GaugeType gauge;

    public void resize(){

    }
    public void playAlarm(){

    }
    public void setTitle(String title)
    {
        tile.setTitle(title);
    }

    public Tile getTile() { return tile; }

    public void setBlueRange(double min, double max)
    {
        blueRangeProvided = true;
        minBlueRange = min;
        maxBlueRange = max;
    }

    public void setGreenRange(double min, double max)
    {
        greenRangeProvided = true;
        minGreenRange = min;
        maxGreenRange = max;
    }

    public void setYellowRange(double min, double max)
    {
        yellowRangeProvided = true;
        minYellowRange = min;
        maxYellowRange = max;
    }

    public void setRedRange(double min, double max)
    {
        redRangeProvided = true;
        minRedRange = min;
        maxRedRange = max;
    }
}