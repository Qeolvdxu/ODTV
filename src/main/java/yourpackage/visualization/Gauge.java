package yourpackage.visualization;

import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import eu.hansolo.steelseries.gauges.Radial;

public class Gauge {
    private String name;
    private float size;
    private float xpos;
    private float ypos;
    //private Datafield correspondingField;
    private float blueLimit;
    private float greenLimit;
    private float yellowLimit;
    private float redLimit;

    public Gauge()
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(250, 250));
        frame.setTitle("gaugeName");
        frame.setVisible(true);
    }
    public enum GaugeType {
        Circle,
        Bar,
        XPlot,
        XByYPLOT,
        NumOrSingleChar,
        TextDisplay,
        Stopwatch,
        OnOffLight,
    }
    public void resize(){

    }
    public void playAlarm(){

    }
}