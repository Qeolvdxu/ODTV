package yourpackage.visualization;

import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class VideoPlayerApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        if (getParameters().getRaw().size() == 0) {
            System.out.println("Please provide the path to the video file as a command-line argument.");
            return;
        }

        String videoFilePath = getParameters().getRaw().get(0);
        File videoFile = new File(videoFilePath);

        if (!videoFile.exists()) {
            System.out.println("File not found: " + videoFilePath);
            return;
        }

        Media media = new Media(videoFile.toURI().toString());
        MediaPlayer player = new MediaPlayer(media);
        MediaView viewer = new MediaView(player);

        viewer.setPreserveRatio(true);

        StackPane root = new StackPane();
        root.getChildren().add(viewer);

        Scene scene = new Scene(root, 800, 600, Color.BLACK);
        stage.setScene(scene);
        stage.setTitle("Video Player");
        stage.setFullScreen(true);
        stage.show();

        player.play();
    }
}