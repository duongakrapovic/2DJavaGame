
package sound_manager;

import javax.sound.sampled.*;
import java.net.URL;

/**
 * Sound.java
 * Handles loading and playing audio clips using Java Sound API.
 * Supports one-time playback and continuous looping (for background music).
 */
public class Sound {
    // Audio clip object
    private Clip clip;

    /**
     * Loads an audio file from the given URL into the clip.
     * @param url URL pointing to the audio file (WAV recommended)
     */
    public void setFile(URL url) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace(); // Print error if file cannot be loaded
        }
    }

    /**
     * Plays the clip once from the beginning.
     * Resets frame position before playing.
     */
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);// rewind to start
            clip.start();
        }
    }

    /**
     * Loops the clip continuously.
     * Typically used for background music.
     */
    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);// rewind to start
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    /**
     * Stops the clip if it is currently playing.
     */
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

