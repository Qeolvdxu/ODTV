package yourpackage.visualization;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MediaSceneGenerator {
    private static final String MUSIC_FOLDER = "C:\\Users\\Public\\Music\\Sample Music";
    private static final String MUSIC_FILE_EXTENSION = ".mp3";

    private final Label currentlyPlaying = new Label();
    private final ProgressBar progress = new ProgressBar();
    private ChangeListener<Duration> progressChangeListener;

    public Scene createScene() {
        final StackPane layout = new StackPane();

        // determine the source directory for the playlist
        final File dir = new File(MUSIC_FOLDER);
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("Cannot find media source directory: " + dir);
            Platform.exit();
            return null;
        }

        // create some media players.
        final List<MediaPlayer> players = new ArrayList<>();
        for (String file : Objects.requireNonNull(dir.list((dir1, name) -> name.endsWith(MUSIC_FILE_EXTENSION))))
            players.add(
                    createPlayer(
                            normalizeFileURL(dir, file)
                    )
            );
        if (players.isEmpty()) {
            System.out.println("No audio found in " + dir);
            Platform.exit();
            return null;
        }

        // create a view to show the mediaplayers.
        final MediaView mediaView = new MediaView(players.get(0));
        final Button skip = new Button("Skip");
        final Button play = new Button("Pause");

        // play each audio file in turn.
        for (int i = 0; i < players.size(); i++) {
            MediaPlayer player = players.get(i);
            MediaPlayer nextPlayer = players.get((i + 1) % players.size());
            player.setOnEndOfMedia(() -> {
                final MediaPlayer curPlayer = mediaView.getMediaPlayer();
                nextPlayer.seek(Duration.ZERO);
                if (nextPlayer != curPlayer) {
                    curPlayer.currentTimeProperty().removeListener(progressChangeListener);
                }
                mediaView.setMediaPlayer(nextPlayer);
                nextPlayer.play();
            });
        }

        // allow the user to skip a track.
        skip.setOnAction(actionEvent -> {
            final MediaPlayer curPlayer = mediaView.getMediaPlayer();
            MediaPlayer nextPlayer = players.get((players.indexOf(curPlayer) + 1) % players.size());
            nextPlayer.seek(Duration.ZERO);
            mediaView.setMediaPlayer(nextPlayer);
            if (nextPlayer != curPlayer) {
                curPlayer.currentTimeProperty().removeListener(progressChangeListener);
            }
            nextPlayer.play();
        });

        // allow the user to play or pause a track.
        play.setOnAction(actionEvent -> {
            if ("Pause".equals(play.getText())) {
                mediaView.getMediaPlayer().pause();
                play.setText("Play");
            } else {
                mediaView.getMediaPlayer().play();
                play.setText("Pause");
            }
        });

        // display the name of the currently playing track.
        mediaView.mediaPlayerProperty().addListener(
                (observableValue, oldPlayer, newPlayer) -> setCurrentlyPlaying(newPlayer)
        );

        // start playing the first track.
        mediaView.setMediaPlayer(players.get(0));
        mediaView.getMediaPlayer().play();
        setCurrentlyPlaying(mediaView.getMediaPlayer());

        // silly invisible button used as a template to get the actual preferred size of the Pause button.
        Button invisiblePause = new Button("Pause");
        invisiblePause.setVisible(false);
        play.prefHeightProperty().bind(invisiblePause.heightProperty());
        play.prefWidthProperty().bind(invisiblePause.widthProperty());

        // layout the scene.
        HBox controls = new HBox(10, skip, play, progress);
        controls.setAlignment(Pos.CENTER);
        VBox mediaPanel = new VBox(10, currentlyPlaying, mediaView, controls);

        layout.setStyle("-fx-background-color: cornsilk; -fx-font-size: 20; -fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(
                invisiblePause,
                mediaPanel
        );
        progress.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(progress, Priority.ALWAYS);

        return new Scene(layout);
    }

    /**
     * sets the currently playing label to the label of the new media player and updates the progress monitor.
     */
    private void setCurrentlyPlaying(final MediaPlayer newPlayer) {
        progress.setProgress(0);
        progressChangeListener = (observableValue, oldValue, newValue) ->
                progress.setProgress(
                        1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis()
                );
        newPlayer.currentTimeProperty().addListener(progressChangeListener);

        String source = getUserFriendlyMediaName(newPlayer);
        currentlyPlaying.setText("Now Playing: " + source);
    }

    /**
     * @return a MediaPlayer for the given source which will report any errors it encounters
     */
    private MediaPlayer createPlayer(String aMediaSrc) {
        System.out.println("Creating player for: " + aMediaSrc);
        final MediaPlayer player = new MediaPlayer(new Media(aMediaSrc));
        player.setOnError(() -> System.out.println("Media error occurred: " + player.getError()));
        return player;
    }

    private String normalizeFileURL(File dir, String file) {
        return "file:///" + (dir + "\\" + file).replace("\\", "/").replaceAll(" ", "%20");
    }

    private String getUserFriendlyMediaName(MediaPlayer newPlayer) {
        String source = newPlayer.getMedia().getSource();

        source = source.substring(0, source.length() - MUSIC_FILE_EXTENSION.length());
        source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");

        return source;
    }
}