package yourpackage.visualization;

import javax.swing.*;
import java.awt.*;

public class Gauge {
    public Gauge()
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(250, 250));
        frame.setTitle("gaugeName");
        frame.setVisible(true);
    }
}