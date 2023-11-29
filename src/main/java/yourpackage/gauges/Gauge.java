package yourpackage.gauges;


import eu.hansolo.tilesfx.Tile;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class Gauge {
    protected Tile tile = null;
    protected JFrame frame;
    protected double minBlueRange, maxBlueRange, minGreenRange, maxGreenRange, minYellowRange, maxYellowRange, minRedRange, maxRedRange;
    protected boolean blueRangeProvided = false;
    protected boolean greenRangeProvided = false;
    protected boolean yellowRangeProvided = false;
    protected boolean redRangeProvided = false;
    protected JFXPanel jfxPanel;
    protected String audioFile = "src/main/resources/criticalAlarm.wav";
    protected MediaPlayer soundPlayer;
    protected boolean soundPlaying = false;
    protected boolean visible = true;
    protected double updateFrequency;

    public Gauge() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.pack();
        frame.setMinimumSize(new Dimension(50, 50));
        frame.setSize(new Dimension(100, 100));
        frame.setTitle("gaugeName");
        frame.setVisible(true);

        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                tile.setPrefSize(frame.getContentPane().getWidth(), frame.getContentPane().getHeight());
                jfxPanel.setScene(null);
                Scene scene = new Scene(new Pane(tile));
                jfxPanel.setScene(scene);
            }
        });
    }

    public void setGaugeTitle(String title)
    {
        frame.setTitle(title);
    }

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

    public boolean isVisible() { return visible; }

    public void setInvisible() { visible = false; }
}