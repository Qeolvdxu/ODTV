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
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.Math.abs;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class VideoPlayerSwingIntegration {

    private static final String TEMP_FOLDER_PATH = "temp";
    private static final String BACKWARD_VIDEO_FILENAME = "backward_temp.mp4";

    private static MediaPlayer player;
    private ReadOnlyObjectProperty<Duration> currentTimeProperty;
    private static JFXPanel fxPanel;

    private boolean reversed;
    File tempFolder = new File("temp");

    private static JFrame processingDialog;
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

    public static void changeVideo(String originalVideoFilePath, String reverseVideoFilePath) {
        File originalVideoFile = new File(originalVideoFilePath);
        File reverseVideoFile = new File(reverseVideoFilePath);

        if (!originalVideoFile.exists() || !reverseVideoFile.exists()) {
            System.out.println("File not found: " + (originalVideoFile.exists() ? reverseVideoFilePath : originalVideoFilePath));
            return;
        }

        File tempFolder = new File(TEMP_FOLDER_PATH);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }

        // Clear existing temp files
        clearTempFiles(tempFolder);

        // Copy original video to temp folder
        File copiedOriginalVideo = new File(tempFolder, "original_temp.mp4");
        try {
            Files.copy(originalVideoFile.toPath(), copiedOriginalVideo.toPath());
        } catch (IOException e) {
            System.out.println("Error occurred while copying the original video: " + e.getMessage());
            return;
        }

        // Copy reverse video to temp folder
        File copiedReverseVideo = new File(tempFolder, BACKWARD_VIDEO_FILENAME);
        try {
            Files.copy(reverseVideoFile.toPath(), copiedReverseVideo.toPath());
        } catch (IOException e) {
            System.out.println("Error occurred while copying the reverse video: " + e.getMessage());
            return;
        }

        closeProcessingDialog();

        Platform.runLater(() -> {
            Media originalMedia = new Media(copiedOriginalVideo.toURI().toString());
            Media reverseMedia = new Media(copiedReverseVideo.toURI().toString());

            if (player != null) {
                player.stop();
            }

            player = new MediaPlayer(originalMedia);
            MediaView viewer = new MediaView(player);
            viewer.setPreserveRatio(true);

            StackPane root = new StackPane();
            root.getChildren().add(viewer);

            Scene scene = new Scene(root, 800, 600);
            fxPanel.setScene(scene);
        });
    }

   /* public static void changeVideo(String newVideoFilePath) {
        File newVideoFile = new File(newVideoFilePath);

        if (!newVideoFile.exists()) {
            System.out.println("File not found: " + newVideoFilePath);
            return;
        }

        File tempFolder = new File(TEMP_FOLDER_PATH);
        if (!tempFolder.exists()) {
            tempFolder.mkdir();
        }

        // Clear existing temp files
        clearTempFiles(tempFolder);

        // Create and show a processing dialog
        SwingUtilities.invokeLater(() -> {
            processingDialog = new JFrame("Processing Video");
            processingDialog.setSize(300, 100);
            processingDialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

            JLabel processingLabel = new JLabel("Processing video, please wait...");
            processingDialog.add(processingLabel);

            processingDialog.setLocationRelativeTo(null);
            processingDialog.setVisible(true);
        });

        // Compress the original video to MP4
        File compressedVideoFile = compressToMP4(newVideoFile, tempFolder);
        if (compressedVideoFile == null) {
            System.out.println("Error occurred during video compression.");
            closeProcessingDialog();
            return;
        }

        // Generate backward version of the compressed video
        String outputReverseFilePath = tempFolder.getAbsolutePath() + File.separator + BACKWARD_VIDEO_FILENAME;
        generateBackwardVideo(compressedVideoFile, outputReverseFilePath);

        // Clean up: delete compressed video file
        compressedVideoFile.delete();

        closeProcessingDialog();
        System.out.println("Backward video generation completed.");

        Platform.runLater(() -> {
            Media newMedia = new Media(new File(tempFolder, newVideoFilePath).toURI().toString());

            if (player != null) {
                player.stop();
            }

            player = new MediaPlayer(newMedia);
            MediaView viewer = new MediaView(player);
            viewer.setPreserveRatio(true);

            StackPane root = new StackPane();
            root.getChildren().add(viewer);

            Scene scene = new Scene(root, 800, 600);
            fxPanel.setScene(scene);
        });
    }*/

    private static void closeProcessingDialog() {
        SwingUtilities.invokeLater(() -> {
            if (processingDialog != null && processingDialog.isVisible()) {
                processingDialog.dispose();
            }
        });
    }

    private static void clearTempFiles(File tempFolder) {
        File[] existingTempFiles = tempFolder.listFiles();
        if (existingTempFiles != null) {
            for (File file : existingTempFiles) {
                if (!file.delete()) {
                    System.out.println("Failed to delete file: " + file.getAbsolutePath());
                }
            }
        }
    }

    private static File compressToMP4(File originalVideo, File tempFolder) {
        File compressedVideoFile = new File(originalVideo.getAbsolutePath());
        String compressCommand = "ffmpeg -i " + originalVideo.getAbsolutePath() + " -c:v libx264 -preset slow -crf 22 -c:a aac -strict experimental -b:a 192k " + compressedVideoFile.getAbsolutePath();

        try {
            ProcessBuilder compressProcessBuilder = new ProcessBuilder(String.valueOf(Collections.singletonList(compressCommand)));
            compressProcessBuilder.redirectErrorStream(true);
            Process compressProcess = compressProcessBuilder.start();
            waitForProcessCompletion(compressProcess, "Video compression");

            // Rename the compressed file to temp.mp4
            Files.move(compressedVideoFile.toPath(), tempFolder.toPath().resolve("original_temp.mp4"), REPLACE_EXISTING);

            return new File(tempFolder, "original_temp.mp4");
        } catch (IOException e) {
            System.out.println("Error occurred during video compression: " + e.getMessage());
            return null;
        }
    }

    private static void generateBackwardVideo(File inputVideo, String outputReverseFilePath) {
        String reverseCommand = "ffmpeg -i " + inputVideo.getAbsolutePath() + " -vf reverse " + outputReverseFilePath;

        try {
            ProcessBuilder reverseProcessBuilder = new ProcessBuilder(reverseCommand.split(" "));
            reverseProcessBuilder.redirectErrorStream(true);
            Process reverseProcess = reverseProcessBuilder.start();
            waitForProcessCompletion(reverseProcess, "Reverse video generation");
        } catch (IOException e) {
            System.out.println("Error occurred during reverse video generation: " + e.getMessage());
        }
    }


    private static void waitForProcessCompletion(Process process, String operation) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();

            if (exitCode != 0) {
                System.out.println("Error occurred during " + operation + ".");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error occurred during " + operation + ": " + e.getMessage());
        }
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
            double totalDuration = player.getTotalDuration().toSeconds();

            if(reversed)
            {
                return totalDuration - currentTime.toSeconds();
            } else
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

        if (reversed == true) {
            double newTime = getCurrentTimeInSeconds();
            double totalDuration = getTotalDurationInSeconds();

            Media newMedia;
            newMedia = new Media(new File(tempFolder, "original_temp.mp4").toURI().toString());
            reversed = false;
            player = new MediaPlayer(newMedia);
            MediaView viewer = new MediaView(player);
            viewer.setPreserveRatio(true);

            StackPane root = new StackPane();
            root.getChildren().add(viewer);

            Scene scene = new Scene(root, 800, 600);
            fxPanel.setScene(scene);

            player.setOnReady(() -> {
                player.seek(Duration.seconds(newTime));
                player.play();
            });
        }
        if (player != null) {
            System.out.println("Speed multiplier: ");
            this.player.rateProperty().set(speedMultiplier);
            System.out.println(player.getCurrentRate());
            System.out.println(player.getTotalDuration());
        }
    }

    public void setPlayInReverse() {

        double oldTime = getCurrentTimeInSeconds();
        double totalDuration = getTotalDurationInSeconds();

        // Calculate the new time in the reversed video
        double newTime = totalDuration - oldTime;

        Platform.runLater(() -> {
            Media newMedia;
            if (reversed == true) {
                return;
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
                    if (reversed == true)
                    {
                        currentTime = (totalDuration - player.getCurrentTime().toSeconds());
                    }

                    // Calculate the slider position as a percentage of the total duration
                    double sliderPosition = (currentTime / totalDuration) * 100;

                    // Update the slider's value
                    slider.setValue((int) sliderPosition);

                    // Update the video time in the JLabel (videoTimeLabel)
                    videoTimeLabel.setText(String.format("%.2f seconds", currentTime)); // Display time in seconds with two decimal places

                    // Check if the current time has reached the total duration
                    if (currentTime >= totalDuration) {
                        stop(); // Stop the player when the max time is reached
                    }
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }



}