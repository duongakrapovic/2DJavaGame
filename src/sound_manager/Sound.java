
package sound_manager;

import javax.sound.sampled.*;
import java.net.URL;


public class Sound {
    // Audio clip object
    private Clip clip;
    public void setFile(URL url) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace(); // Print error if file cannot be loaded
        }
    }
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);// rewind to start
            clip.start();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);// rewind to start
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

