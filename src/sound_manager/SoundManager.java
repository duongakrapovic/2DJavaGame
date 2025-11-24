/*
 * SoundManager.java
 * Handles background music and sound effects for the game.
 * Implements Singleton pattern to ensure only one instance exists.
 */
package sound_manager;

import java.net.URL;
import java.util.EnumMap;
import javax.sound.sampled.*;

public class SoundManager {
    private static SoundManager instance;

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    // --- Enum for all sound IDs ---
    public enum SoundID {
        MUSIC_THEME,   // main background music
        HIT,           // collision effect
        DEAD,          // death effect
        COIN,          // collect item effect
        UNLOCK         // unlock / open door effect
    }

    // --- Map storing sound file URLs by SoundID ---
    private final EnumMap<SoundID, URL> soundFiles = new EnumMap<>(SoundID.class);
    // Background music (looped continuously)
    private final Sound music = new Sound();
    private boolean isMusicPlaying = false;
    // Short sound effects (played once)
    private final Sound se = new Sound();

    public SoundManager() {
        // Load background music
        soundFiles.put(SoundID.MUSIC_THEME, getClass().getResource("/sound/ThemeMusic.wav"));
        // Load sound effects
        soundFiles.put(SoundID.COIN, getClass().getResource("/sound/coin.wav"));
        soundFiles.put(SoundID.UNLOCK, getClass().getResource("/sound/unlock.wav"));
    }

    // --- Background music management ---
    public void playMusic(SoundID id) {
        URL url = soundFiles.get(id);
        if (url != null) {
            if (isMusicPlaying) return;
            music.setFile(url);  // assign file to music object
            music.play();        // start playback
            music.loop();        // loop continuously
            isMusicPlaying = true;
        }
    }

    public void stopMusic() {
        music.stop();
        isMusicPlaying = false;
    }

    // --- Sound effect management ---
    public void playSE(SoundID id) {
        URL url = soundFiles.get(id);
        if (url != null) {
            se.setFile(url); // assign file to SE object
            se.play();       // play the effect once
        }
    }

}
