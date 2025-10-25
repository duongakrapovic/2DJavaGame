package ui;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Font;
import main.GamePanel;
import sound_manager.SoundManager;

/** Hiển thị và điều chỉnh âm lượng bằng phím A/D (không phụ thuộc Game.SCALE). */
public class AudioOptions {

    private final GamePanel gp;
    private int volume = 50; // 0..100

    public AudioOptions(GamePanel gp) {
        this.gp = gp;
    }

    public void increaseVolume() {
        volume += 5;
        if (volume > 100) volume = 100;
        SoundManager.playSFX("hover");
    }

    public void decreaseVolume() {
        volume -= 5;
        if (volume < 0) volume = 0;
        SoundManager.playSFX("hover");
    }

    public void update() {}

    public void draw(Graphics g) {
        // font size lấy theo tileSize để tương thích mọi độ phân giải
        int fontSize = Math.max(16, gp.tileSize / 2);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        // vẽ ở góc dưới trái một chút
        int x = gp.tileSize * 2;
        int y = gp.screenHeight - gp.tileSize;
        g.drawString("Volume: " + volume + "%   (A/D để chỉnh)", x, y);
    }
}
