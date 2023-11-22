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

    public Gauge()
    {
        System.out.println("Creating a new Gauge instance."); // <-- Added print statement

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.pack();
        frame.setMinimumSize(new Dimension(250, 250));
        frame.setTitle("gaugeName");
        frame.setVisible(true);
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
}