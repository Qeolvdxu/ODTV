package yourpackage.app;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import yourpackage.visualization.VideoPlayerSwingIntegration;
import yourpackage.visualization.Gauge;

public class Window {
    private JPanel mainPanelW;
    private JButton playButton;
    private JButton pauseButton;
    private JButton stopButton;
    private JComboBox videoSpeedComboBox;
    private JLabel videoSpeedLabel;
    private JButton forwardButton;
    private JButton backwardButton;
    private JMenuItem exitButton;
    private JMenu fileButton;
    private JMenuItem openVideoAndDataButton;
    private JMenu editButton;
    private JMenu viewButton;
    private JMenu helpButton;
    private JMenuItem aboutButton;
    private JMenuItem loadConfiguration;
    private JMenuItem saveConfiguration;
    private JSlider slider1;
    private JMenuItem viewGaugesButton;
    private JMenuItem viewDataVisualization;
    private JMenuItem visualizerSetup;
    private JMenuItem gaugeSetup;
    private JMenuItem createGauge;
    private VideoPlayerSwingIntegration videoPlayer = new VideoPlayerSwingIntegration();

    public Window() {
        JFrame frame = new JFrame();
        System.out.println(System.getProperty("user.dir"));
        String iconPath = System.getProperty("user.dir") + "/src/main/resources/drone.png";
        ImageIcon img = new ImageIcon(iconPath);
        frame.setIconImage(img.getImage()); // Get and set a custom icon for the GUI.
        frame.setContentPane(mainPanelW);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setMinimumSize(new Dimension(250, 250));
        frame.setTitle("ODTV");
        frame.setVisible(true);

        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement("1X");
        comboBoxModel.addElement("5X");
        comboBoxModel.addElement("10X");
        comboBoxModel.addElement("1X Reverse");

        videoSpeedComboBox.setModel(comboBoxModel);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        openVideoAndDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FileSelectionWindow fileSelectionWindow = new FileSelectionWindow();
                fileSelectionWindow.show(new FileSelectionWindow.FileSelectionListener() {
                    @Override
                    public void onFilesSelected(String videoFilePath, String csvFilePath) {
                        videoPlayer.embedVideoIntoJFrame(frame, videoFilePath);
                        playButton.setEnabled(true);
                        pauseButton.setEnabled(true);
                        stopButton.setEnabled(true);
                        forwardButton.setEnabled(true);
                        backwardButton.setEnabled(true);
                        slider1.setEnabled(true);
                    }
                });
            }
        });

        slider1.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int value = slider1.getValue();
                if (videoPlayer != null && videoPlayer.getTotalDurationInSeconds() > 0) {
                    double newTime = (value / 100.0) * videoPlayer.getTotalDurationInSeconds();
                    videoPlayer.skipToTime(newTime);
                }
            }
        });



        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AboutWindow w = AboutWindow.getInstance();
                w.show();
            }
        });


        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (videoPlayer != null) {
                    videoPlayer.play();
                }
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (videoPlayer != null) {
                    videoPlayer.pause();
                }
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (videoPlayer != null) {
                    videoPlayer.stop();
                }
            }
        });

        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (videoPlayer != null) {
                    videoPlayer.forward();
                }
            }
        });

        backwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (videoPlayer != null) {
                    videoPlayer.rewind();
                }
            }
        });

        videoPlayer.addTimeListener(new ChangeListener() {
                                        @Override
                                        public void stateChanged(ChangeEvent changeEvent) {
                                            double totalDuration = videoPlayer.getTotalDurationInSeconds();
                                            double currentTime = videoPlayer.getCurrentTimeInSeconds();

                                            // Calculate the slider position as a percentage of the total duration
                                            double sliderPosition = (currentTime / totalDuration) * 100;

                                            // Update the slider's value
                                            slider1.setValue((int) sliderPosition);
                                        }
                                    }
        );

        createGauge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Gauge gauge = new Gauge();
                System.out.println("New gauge created!");
            }
        });
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanelW = new JPanel();
        mainPanelW.setLayout(new BorderLayout(0, 0));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        mainPanelW.add(panel1, BorderLayout.NORTH);
        final JMenuBar menuBar1 = new JMenuBar();
        menuBar1.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel1.add(menuBar1, BorderLayout.CENTER);
        fileButton = new JMenu();
        fileButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        fileButton.setText("File");
        menuBar1.add(fileButton);
        openVideoAndDataButton = new JMenuItem();
        openVideoAndDataButton.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        openVideoAndDataButton.setText("Open Video and Data");
        fileButton.add(openVideoAndDataButton);
        loadConfiguration = new JMenuItem();
        loadConfiguration.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        loadConfiguration.setText("Load Saved Configuration");
        fileButton.add(loadConfiguration);
        saveConfiguration = new JMenuItem();
        saveConfiguration.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        saveConfiguration.setText("Save Configuration");
        fileButton.add(saveConfiguration);
        exitButton = new JMenuItem();
        exitButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        exitButton.setText("Exit");
        fileButton.add(exitButton);
        editButton = new JMenu();
        editButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        editButton.setText("Edit");
        menuBar1.add(editButton);
        gaugeSetup = new JMenuItem();
        gaugeSetup.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        gaugeSetup.setText("Gauge Setup");
        editButton.add(gaugeSetup);
        visualizerSetup = new JMenuItem();
        visualizerSetup.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        visualizerSetup.setText("Data Visualizer Setup");
        editButton.add(visualizerSetup);
        createGauge = new JMenuItem();
        createGauge.setText("Create Gauge (TEMPORARY BUTTON)");
        editButton.add(createGauge);
        viewButton = new JMenu();
        viewButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        viewButton.setText("View");
        menuBar1.add(viewButton);
        viewGaugesButton = new JMenuItem();
        viewGaugesButton.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        viewGaugesButton.setText("Gauges");
        viewButton.add(viewGaugesButton);
        viewDataVisualization = new JMenuItem();
        viewDataVisualization.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        viewDataVisualization.setText("Data Visualization");
        viewButton.add(viewDataVisualization);
        helpButton = new JMenu();
        helpButton.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        helpButton.setText("Help");
        menuBar1.add(helpButton);
        aboutButton = new JMenuItem();
        aboutButton.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        aboutButton.setText("About");
        helpButton.add(aboutButton);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 10, new Insets(0, 0, 0, 0), -1, -1));
        mainPanelW.add(panel2, BorderLayout.SOUTH);
        pauseButton = new JButton();
        pauseButton.setEnabled(false);
        pauseButton.setHorizontalTextPosition(0);
        pauseButton.setIcon(new ImageIcon(getClass().getResource("/pause.png")));
        pauseButton.setText("");
        pauseButton.setToolTipText("Pause");
        panel2.add(pauseButton, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        stopButton = new JButton();
        stopButton.setEnabled(false);
        stopButton.setHorizontalTextPosition(0);
        stopButton.setIcon(new ImageIcon(getClass().getResource("/stop.png")));
        stopButton.setText("");
        stopButton.setToolTipText("Stop");
        panel2.add(stopButton, new GridConstraints(1, 6, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(1, 9, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        videoSpeedComboBox = new JComboBox();
        videoSpeedComboBox.setEnabled(false);
        panel2.add(videoSpeedComboBox, new GridConstraints(1, 8, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        videoSpeedLabel = new JLabel();
        videoSpeedLabel.setText("Video Speed:");
        panel2.add(videoSpeedLabel, new GridConstraints(1, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        forwardButton = new JButton();
        forwardButton.setEnabled(false);
        forwardButton.setHideActionText(false);
        forwardButton.setHorizontalTextPosition(0);
        forwardButton.setIcon(new ImageIcon(getClass().getResource("/fastforward.png")));
        forwardButton.setText("");
        forwardButton.setToolTipText("Forward");
        panel2.add(forwardButton, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider1 = new JSlider();
        slider1.setEnabled(false);
        slider1.setValue(0);
        panel2.add(slider1, new GridConstraints(0, 0, 1, 10, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        playButton = new JButton();
        playButton.setEnabled(false);
        playButton.setHorizontalTextPosition(0);
        playButton.setIcon(new ImageIcon(getClass().getResource("/play.png")));
        playButton.setText("");
        playButton.setToolTipText("Play");
        panel2.add(playButton, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        backwardButton = new JButton();
        backwardButton.setEnabled(false);
        backwardButton.setHorizontalTextPosition(0);
        backwardButton.setIcon(new ImageIcon(getClass().getResource("/rewind.png")));
        backwardButton.setText("");
        backwardButton.setToolTipText("Rewind");
        panel2.add(backwardButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel2.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanelW;
    }


}
