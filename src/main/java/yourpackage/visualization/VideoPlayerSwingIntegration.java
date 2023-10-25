package yourpackage.visualization;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.io.File;

public class VideoPlayerSwingIntegration {

    private static MediaPlayer player;

    private static JFXPanel fxPanel;

    public static void embedVideoIntoJFrame(JFrame frame) {
        fxPanel = new JFXPanel();
        frame.add(fxPanel);
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swing Video Player");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);

            // Provide the path to the video file as a command-line argument
            if (args.length > 0) {
                String videoFilePath = args[0];
                embedVideoIntoJFrame(frame);
            } else {
                System.out.println("Please provide the path to the video file as a command-line argument.");
            }

            frame.setVisible(true);
        });
    }

    public static void changeVideo(String newVideoFilePath) {
        File newVideoFile = new File(newVideoFilePath);

        if (!newVideoFile.exists()) {
            System.out.println("File not found: " + newVideoFilePath);
            return;
        }

        Platform.runLater(() -> {
            Media newMedia = new Media(newVideoFile.toURI().toString());

            if (player != null) {
                player.stop(); // Stop the existing player if there is one
            }

            player = new MediaPlayer(newMedia);
            MediaView viewer = new MediaView(player);
            viewer.setPreserveRatio(true);

            StackPane root = new StackPane();
            root.getChildren().add(viewer);

            Scene scene = new Scene(root, 800, 600);
            fxPanel.setScene(scene);

            player.play();
        });
    }

    public void play() {
        if (player != null) {
            player.play();
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void stop() {
        System.out.println("stop!");
        if (player != null) {
            player.stop();
        }
    }

    public void forward() {
        if (player != null) {
            Duration currentTime = player.getCurrentTime();
            player.seek(currentTime.add(Duration.seconds(10))); // Forward by 10 seconds
        }
    }

    public void rewind() {
        if (player != null) {
            Duration currentTime = player.getCurrentTime();
            player.seek(currentTime.subtract(Duration.seconds(10))); // Rewind by 10 seconds
        }
    }

    public double getCurrentTimeInSeconds() {
        if (player != null) {
            Duration currentTime = player.getCurrentTime();
            return currentTime.toSeconds();
        }
        return 0.0;
    }

    public double getTotalDurationInSeconds() {
        if (player != null) {
            Duration totalDuration = player.getTotalDuration();
            return totalDuration.toSeconds();
        }
        return 0.0;
    }

    public void setVideoSpeed(double speedMultiplier) {
        if (player != null) {
            player.setRate(speedMultiplier);
        }
    }

    public void skipToTime(double timeInSeconds) {
        if (player != null) {
            player.seek(Duration.seconds(timeInSeconds));
        }
    }

    // Method to add a time listener to update the slider position
    public void addTimeListener(ChangeListener listener) {
        if (player != null) {
            player.currentTimeProperty().addListener((javafx.beans.value.ChangeListener<? super Duration>) listener);
        }
    }
}