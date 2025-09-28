
package sound_manager;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound {
    private Clip clip;

    // Nạp file âm thanh từ đường dẫn
    public void setFile(URL url) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Phát 1 lần
    public void play() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    // Phát lặp vô hạn (thường cho nhạc nền)
    public void loop() {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    // Dừng phát
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
}

