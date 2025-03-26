package AdventureModel;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import com.sun.speech.freetts.audio.AudioPlayer;


/**
 * This class contains methods to play and stop
 * text-to-speech voiceovers.
 */
public class TTSStrategy implements AudioStrategy {
    /**
     * The voice of the audio.
     */
    private Voice voice;

    /**
     * The audio player.
     */
    private AudioPlayer audio;

    /**
     * Current state of the audio. True if audio is playing, else false.
     */
    private boolean audioPlaying;

    /**
     * A separate thread for the TTS process to run on.
     */
    private Thread speaking;

    /**
     * Generates text to speech of the text passed into the method.
     *
     * @param speech: String to be generated into speech
     */
    public void play(String speech) {
        speaking = new Thread(() -> {
            System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
            voice = VoiceManager.getInstance().getVoice("kevin16");
            audio = voice.getAudioPlayer();
            voice.allocate();
            audioPlaying = true;
            voice.speak(speech);
            voice.deallocate();
        });
        speaking.start();
    }

    /**
     * Stops the Text to Speech voice if it is currently speaking.
     */
    public void stop() {
        if (audioPlaying) {
            audio.cancel();
            speaking.interrupt();
            audioPlaying = false;
        }
    }
}
