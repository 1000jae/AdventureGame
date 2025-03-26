package AdventureModel;

/**
 * This class contains an audio system that works with any
 * AudioContext to play and stop voiceovers in the Adventure Game.
 */
public class AudioContext {
    /**
     * An AudioStrategy to decide how audio will be played by the AudioContext system.
     */
    private AudioStrategy strategy;

    /**
     * AudioContext constructor.
     *
     * @param strategy: An AudioStrategy to decide how audio will be played by the AudioContext system.
     */
    public AudioContext(AudioStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Plays audio of the associated file or text passed into the method, depending on the strategy.
     *
     * @param textFile: String to be generated into speech or path to an audio file, depending on selected strategy
     */
    public void playAudio(String textFile) {
        strategy.play(textFile);
    }

    /**
     * If audio is currently playing, the method stops the playback.
     */
    public void stopAudio() {
        strategy.stop();
    }
}
