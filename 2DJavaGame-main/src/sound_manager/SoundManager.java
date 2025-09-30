/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sound_manager;

import java.net.URL;
import java.util.EnumMap;

public class SoundManager {
    private static SoundManager instance;

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }
    public enum SoundID {
        MUSIC_THEME,   // nhạc nền
        HIT,           // va chạm
        DEAD,          // chết
        COIN,          // nhặt đồ
        UNLOCK         // mở cửa
    }

    private final EnumMap<SoundID, URL> soundFiles = new EnumMap<>(SoundID.class);
    private final Sound music = new Sound(); // nhạc nền
    private final Sound se = new Sound();    // sound effect

    public SoundManager() {
        // NẠP NGUỒN NHẠC & SE
        soundFiles.put(SoundID.MUSIC_THEME, getClass().getResource("/sound/BlueBoyAdventure.wav"));
        soundFiles.put(SoundID.COIN, getClass().getResource("/sound/coin.wav"));
        soundFiles.put(SoundID.UNLOCK, getClass().getResource("/sound/unlock.wav"));
        // có thể thêm tiếp soundFiles.put(SoundID.HIT, ...);
        // có thể thêm tiếp soundFiles.put(SoundID.DEAD, ...);
    }

    // --- Quản lý nhạc nền ---
    public void playMusic(SoundID id) {
        URL url = soundFiles.get(id);
        if (url != null) {
            music.setFile(url);
            music.play();
            music.loop();
        }
    }

    public void stopMusic() {
        music.stop();
    }

    // --- Quản lý sound effect ---
    public void playSE(SoundID id) {
        URL url = soundFiles.get(id);
        if (url != null) {
            se.setFile(url);
            se.play();
        }
    }
}

