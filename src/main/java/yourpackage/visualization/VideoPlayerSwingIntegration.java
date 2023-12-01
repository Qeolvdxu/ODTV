package yourpackage.visualization;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javax.swing.*;
import java.io.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Math.abs;

public class VideoPlayerSwingIntegration {

    public static MediaPlayer player;

    private ReadOnlyObjectProperty<Duration> currentTimeProperty;

    private static JFXPanel fxPanel;

    private boolean reversed;

    private boolean playInReverse = false;

    private boolean playing = false;

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


    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void changeVideo(String newVideoFilePath) {
        File newVideoFile = new File(newVideoFilePath);

        if (!newVideoFile.exists()) {
            System.out.println("File not found: " + newVideoFilePath);
            return;
        }

        File tempFolder = new File("temp");
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }

        // Delete existing temp video files if they exist
        File[] existingTempFiles = tempFolder.listFiles();
        if (existingTempFiles != null) {
            for (File file : existingTempFiles) {
                file.delete();
            }
        }

        // Rename the new video file to temp.mp4 inside the temp folder
        File tempVideoFile = new File(tempFolder, "temp.mp4");
        try {
            Files.copy(newVideoFile.toPath(), tempVideoFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            System.out.println("Error occurred while copying video file: " + e.getMessage());
            return;
        }

       Platform.runLater(() -> {
            Media newMedia = new Media(new File(tempFolder, "temp.mp4").toURI().toString());

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
        });
    }

    public static void shutdownThreadPool() {
        executorService.shutdown();
    }

    public void play() {
        if (player != null) {
            player.play();
            playing = true;
        }
    }

    public void pause() {
        if (player != null) {
            player.pause();
            playing = false;
        }
    }

    public void stop() {
        System.out.println("stop!");
        if (player != null) {
            player.stop();
            playing = false;
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

    public static double getTotalDurationInSeconds() {
        if (player != null) {
            Duration totalDuration = player.getTotalDuration();
            return totalDuration.toSeconds();
        }
        return 0.0;
    }
    private static long getVideoDuration(File videoFile) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("ffprobe", "-v", "error", "-show_entries",
                    "format=duration", "-of", "default=noprint_wrappers=1:nokey=1", videoFile.getAbsolutePath());
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String durationString = reader.readLine();
            if (durationString != null) {
                double durationInSeconds = Double.parseDouble(durationString);
                return (long) (durationInSeconds * 1000); // Convert to milliseconds
            }
        } catch (Exception e) {
            System.out.println("Error occurred while getting video duration: " + e.getMessage());
        }
        return 0;
    }

    public void setVideoSpeed(double speedMultiplier) {
        if (player != null) {
            player.setRate(speedMultiplier);
        }
    }

    public void setPlayInReverse() {

        double oldTime = getCurrentTimeInSeconds();
        double totalDuration = getTotalDurationInSeconds();

        // Calculate the new time in the reversed video
        double newTime = totalDuration - oldTime;

        File tempFolder = new File("temp");
        Platform.runLater(() -> {
            Media newMedia;
            if (reversed == true) {
                newMedia = new Media(new File(tempFolder, "temp.mp4").toURI().toString());
                reversed = false;
            }
            else
            {
                newMedia = new Media(new File(tempFolder, "backward_temp.mp4").toURI().toString());
                reversed = true;
            }
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

            player.setOnReady(() -> {
                // Set the player's current time to the calculated new time
                player.seek(Duration.seconds(newTime));

                // Start playing the reversed video
                player.play();
            });
        });
    }

    public void skipToTime(double timeInSeconds) {
        if (player != null) {
            player.seek(Duration.seconds(timeInSeconds));
        }
    }
    public Boolean isPlaying() { return playing; }

    public void startUpdatingUIEverySecond(JLabel videoTimeLabel, JSlider slider) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
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