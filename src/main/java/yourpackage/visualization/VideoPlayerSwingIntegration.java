package yourpackage.visualization;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.util.Duration;

import javax.swing.*;
import java.io.File;

public class VideoPlayerSwingIntegration {

    public static MediaPlayer player;

    private ReadOnlyObjectProperty<Duration> currentTimeProperty;

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

        String originalFileName = newVideoFile.getName();
        String newFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.')) + ".mp4";
        File renamedFile = new File(newVideoFile.getParent(), newFileName);

        try {
            if (newVideoFile.renameTo(renamedFile)) {
                System.out.println("File Read.");
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e.getMessage());
        }

        Platform.runLater(() -> {
            Media newMedia = new Media(renamedFile.toURI().toString());

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

    public void startUpdatingUIEverySecond(JLabel videoTimeLabel, JSlider slider) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (player != null && player.getStatus() == MediaPlayer.Status.PLAYING) {
                    double totalDuration = player.getTotalDuration().toSeconds();
                    double currentTime = player.getCurrentTime().toSeconds();

                    // Calculate the slider position as a percentage of the total duration
                    double sliderPosition = (currentTime / totalDuration) * 100;

                    // Update the slider's value
                    slider.setValue((int) sliderPosition);

                    // Update the video time in the JLabel (videoTimeLabel)
                    videoTimeLabel.setText(String.format("%.2f seconds", currentTime)); // Display time in seconds with two decimal places
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }


}