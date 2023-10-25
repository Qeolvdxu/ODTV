package yourpackage.visualization;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;


/* THIS IS A TEST FILE TO TEST THE VIDEO PLAYING COMPONENTS OF THE PROGRAM
 * THIS DOES NOT RUN ON NORMAL PROGRAM USAGE
 */

public class VideoTester {
    public static void run(String[] args) {
        // Create a new JFrame
        JFrame frame = new JFrame("Swing Video Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Add a label to the JFrame
        JLabel label = new JLabel("Welcome to Video Player!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        frame.add(label, BorderLayout.NORTH);

        // Set the JFrame to be visible
        frame.setVisible(true);

        // Get video file path from user input using Scanner
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path to the video file: ");
        String videoFilePath = scanner.nextLine();


        // Check if the entered file path is not empty
        if (!videoFilePath.isEmpty()) {
            VideoPlayerSwingIntegration.embedVideoIntoJFrame(frame);
            VideoPlayerSwingIntegration.changeVideo(videoFilePath);
        } else {
            System.out.println("Invalid video file path. Please provide a valid path.");
        }

        // Close the scanner
        scanner.close();
    }
}