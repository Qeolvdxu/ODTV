package yourpackage.app;

import javax.swing.*;

public class Window {
    private JPanel panel1;
    private JMenuItem File;
    private JMenuItem Edit;

    public Window () {
        JFrame frame = new JFrame();
        frame.setContentPane(panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
