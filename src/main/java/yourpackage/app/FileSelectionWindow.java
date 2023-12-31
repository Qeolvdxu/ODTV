package yourpackage.app;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import yourpackage.parsing.DataFieldParser;
import yourpackage.visualization.VideoPlayerSwingIntegration;

public class FileSelectionWindow {
    private JFormattedTextField videoFileTextfield;
    private JFormattedTextField csvTextField;
    private JButton selectVideoButton;
    private JButton selectCSVButton;
    private JButton OKButton;
    private JPanel mainPanelFS;
    private JButton selectReverseVideoButton;
    private JFormattedTextField reverseVideoFileTextField;
    private final JFrame frame;
    private DataFieldParser parser;

    private String selectedVideoFilePath;
    private String selectedReverseVideoFilePath;
    private String selectedCSVFilePath;

    private FieldChooser Fc;

    VideoPlayerSwingIntegration videoPlayer;

    public FileSelectionWindow(VideoPlayerSwingIntegration vp) {
        frame = new JFrame();
        String iconPath = System.getProperty("user.dir") + "/src/main/resources/drone.png";
        ImageIcon img = new ImageIcon(iconPath);
        frame.setIconImage(img.getImage()); // Get and set a custom icon for the GUI.
        frame.setContentPane(mainPanelFS);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setTitle("File Selection");
        frame.setResizable(false);
        videoPlayer = vp;
        OKButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedVideoFilePath = videoFileTextfield.getText();
                selectedCSVFilePath = csvTextField.getText();
                selectedReverseVideoFilePath = reverseVideoFileTextField.getText();
                frame.setVisible(false);
                frame.dispose(); // Close the JFrame associated with the FileSelectionWindow
            }
        });


        selectVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(); // Create a JFileChooser instance
                fileChooser.showOpenDialog(frame); // Hopefully it gets rid of that stupid duke icon.
                // Configure file chooser settings if needed (e.g., set initial directory, file filters)
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Quicktime Movie (*.MOV)", "MOV");
                // fileChooser.setFileFilter(filter); // Set the file filter for the file chooser
                int returnValue = fileChooser.showOpenDialog(null); // Show the file chooser dialog and capture the user's choice
                if (returnValue == JFileChooser.APPROVE_OPTION) { // Check if the user selected a file
                    File selectedFile = fileChooser.getSelectedFile(); // Get the selected file
                    videoFileTextfield.setText(selectedFile.getAbsolutePath()); // Handle the selected file, e.g., display its path
                }
            }
        });
        selectCSVButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser(); // Create a JFileChooser instance
                fileChooser.showOpenDialog(frame);
                // Configure file chooser settings if needed (e.g., set initial directory, file filters)
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Column-separated Values (*.csv)", "csv");
                fileChooser.setFileFilter(filter); // Set the file filter for the file chooser
                int returnValue = fileChooser.showOpenDialog(null); // Show the file chooser dialog and capture the user's choice
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile(); // Get the selected file
                    csvTextField.setText(selectedFile.getAbsolutePath()); // Handle the selected file, e.g., display its path

                    parser = new DataFieldParser(selectedFile);
                    parser.parseData();
                }
            }

            public String getSelectedVideoFilePath() {
                return selectedVideoFilePath;
            }

            public String getSelectedCSVFilePath() {
                return selectedCSVFilePath;
            }

            public String getReverseVideoFilePath() {
                return selectedReverseVideoFilePath;
            }

        });
        selectReverseVideoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.showOpenDialog(frame);

                FileNameExtensionFilter filter = new FileNameExtensionFilter("Video Files", "mp4", "avi", "mov");
                fileChooser.setFileFilter(filter);

                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    reverseVideoFileTextField.setText(selectedFile.getAbsolutePath());
                }
            }
        });
    }

    public boolean isVisible() {
        return frame.isVisible();
    }

    public void show() {
        frame.setVisible(true);
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
        mainPanelFS = new JPanel();
        mainPanelFS.setLayout(new GridLayoutManager(7, 6, new Insets(0, 0, 0, 0), -1, -1));
        final Spacer spacer1 = new Spacer();
        mainPanelFS.add(spacer1, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanelFS.add(spacer2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        csvTextField = new JFormattedTextField();
        csvTextField.setEditable(false);
        mainPanelFS.add(csvTextField, new GridConstraints(5, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        mainPanelFS.add(spacer3, new GridConstraints(0, 1, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        selectVideoButton = new JButton();
        selectVideoButton.setText("Select");
        mainPanelFS.add(selectVideoButton, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        selectCSVButton = new JButton();
        selectCSVButton.setText("Select");
        mainPanelFS.add(selectCSVButton, new GridConstraints(5, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Select video file:");
        mainPanelFS.add(label1, new GridConstraints(1, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Select .csv file:");
        mainPanelFS.add(label2, new GridConstraints(4, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        mainPanelFS.add(spacer4, new GridConstraints(6, 5, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        mainPanelFS.add(spacer5, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        OKButton = new JButton();
        OKButton.setText("OK");
        mainPanelFS.add(OKButton, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        videoFileTextfield = new JFormattedTextField();
        videoFileTextfield.setEditable(false);
        mainPanelFS.add(videoFileTextfield, new GridConstraints(2, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        selectReverseVideoButton = new JButton();
        selectReverseVideoButton.setText("Select");
        mainPanelFS.add(selectReverseVideoButton, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reverseVideoFileTextField = new JFormattedTextField();
        reverseVideoFileTextField.setEditable(false);
        mainPanelFS.add(reverseVideoFileTextField, new GridConstraints(3, 1, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        label1.setLabelFor(reverseVideoFileTextField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanelFS;
    }

    public interface FileSelectionListener {
        void onFilesSelected(String videoFilePath, String csvFilePath, String reverseVideoFilePath);
    }

    public void show(FileSelectionListener listener) {
        frame.setVisible(true);
        OKButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedVideoFilePath = videoFileTextfield.getText();
                selectedCSVFilePath = csvTextField.getText();
                selectedReverseVideoFilePath = reverseVideoFileTextField.getText();

                frame.setVisible(false);
                frame.dispose();
                listener.onFilesSelected(selectedVideoFilePath, selectedCSVFilePath, selectedReverseVideoFilePath);
                FieldChooser Fc = new FieldChooser(videoPlayer);
                Fc.setFoundFields(parser.getFoundFields());
                parser.clearFields(); // Clear the fields in the parser in case if another file gets opened.
            }
        });
    }

}