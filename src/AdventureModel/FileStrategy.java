package AdventureModel;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

/**
 * This class contains methods to play and stop
 * audio file voiceovers.
 */
public class FileStrategy implements AudioStrategy {
    /**
     * The media player.
     */
    private MediaPlayer mediaPlayer;

    /**
     * Current state of the audio. True if audio is playing, else false.
     */
    private boolean audioPlaying;

    /**
     * Plays an audio file by accepting the directory of an audio file as a String.
     *
     * @param filepath: String of the file path to the audio file to be played.
     */
    public void play(String filepath) {
        // accepts the directory of an audio file as a String, and plays it
        Media sound = new Media(new File(filepath).toURI().toString());

        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        audioPlaying = true;
    }

    /**
     * Stops the file's audio if it is currently playing.
     */
    public void stop() {
        if (audioPlaying) {
            mediaPlayer.stop();
            audioPlaying = false;
        }
    }
}
