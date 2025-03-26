package AdventureModel;

/**
 * This interface contains methods to play and stop
 * audio voiceovers.
 */
public interface AudioStrategy {
    /**
     * Plays audio of the associated file or text passed into the method.
     *
     * @param speechOrFile: String to be generated into speech or path to an audio file, depending on selected strategy
     */
    void play(String speechOrFile);

    /**
     * If audio is currently playing, the method stops the playback.
     */
    void stop();
}
