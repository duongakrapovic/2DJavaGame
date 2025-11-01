/*
 * SoundManager.java
 * Handles background music and sound effects for the game.
 * Implements Singleton pattern to ensure only one instance exists.
 */
package sound_manager;

import java.net.URL;
import java.util.EnumMap;
import javax.sound.sampled.*;

/**
 * Manages all background music and sound effects in the game.
 * Provides both looping background tracks and short one-time SFX sounds.
 */
public class SoundManager {
    private static SoundManager instance;

    /**
     * Returns the single instance of SoundManager.
     * Creates a new instance if it doesn't exist yet.
     */
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

    /**
     * Constructor: loads all sound files into the EnumMap.
     * Only called once when initializing the Singleton.
     */
    public SoundManager() {
        // Load background music
        soundFiles.put(SoundID.MUSIC_THEME, getClass().getResource("/sound/ThemeMusic.wav"));
        // Load sound effects
        soundFiles.put(SoundID.COIN, getClass().getResource("/sound/coin.wav"));
        soundFiles.put(SoundID.UNLOCK, getClass().getResource("/sound/unlock.wav"));
        // TODO: Add HIT and DEAD effects if needed
        // soundFiles.put(SoundID.HIT, ...);
        // soundFiles.put(SoundID.DEAD, ...);
    }

    // --- Background music management ---
    /**
     * Plays background music corresponding to the given SoundID.
     * @param id SoundID of the music track
     */
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

    /** Stops the currently playing background music */
    public void stopMusic() {
        music.stop();
        isMusicPlaying = false;
    }

    // --- Sound effect management ---
    /**
     * Plays a one-time sound effect corresponding to the given SoundID.
     * @param id SoundID of the effect
     */
    public void playSE(SoundID id) {
        URL url = soundFiles.get(id);
        if (url != null) {
            se.setFile(url); // assign file to SE object
            se.play();       // play the effect once
        }
    }

    // --- Custom SFX playback for UI (hover, click, etc.) ---
    /**
     * Plays a short UI sound effect by name (e.g. "hover", "click").
     * Looks for .wav files inside the /sound/ resource folder.
     * Example usage:
     *     SoundManager.playSFX("click");
     */
    public static void playSFX(String name) {
        try {
            String path = "/sound/" + name + ".wav";
            URL url = SoundManager.class.getResource(path);
            if (url == null) {
                System.out.println("[SoundManager] Missing SFX: " + path);
                return;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
