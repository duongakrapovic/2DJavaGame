package ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import main.GamePanel;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;

/**
 * SoundButton — chuẩn hóa cho hệ thống GamePanel (không dùng chuột, chỉ phím)
 * Cắt sprite theo kích thước gốc (42x42), vẽ scale theo GamePanel.SCALE
 */
public class SoundButton {
    private int x, y;
    private boolean muted;
    private BufferedImage[][] imgs; // [2][3]
    private int rowIndex, colIndex;
    private Rectangle bounds;

    public SoundButton(int x, int y) {
        this.x = x;
        this.y = y;
        loadImgs();
        bounds = new Rectangle(x, y, SOUND_SIZE, SOUND_SIZE);
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        if (temp == null) return;

        int tileW = temp.getWidth() / 3;  // = 42
        int tileH = temp.getHeight() / 2; // = 42
        imgs = new BufferedImage[2][3];

        for (int row = 0; row < 2; row++)
            for (int col = 0; col < 3; col++)
                imgs[row][col] = temp.getSubimage(col * tileW, row * tileH, tileW, tileH);
    }

    /** Cập nhật frame hiển thị (0 = bình thường, 1 = hover, 2 = pressed) */
    public void update() {
        rowIndex = muted ? 1 : 0;
        colIndex = 0; // vì điều khiển bằng phím, không có hover/press
    }

    public void draw(Graphics2D g2) {
        if (imgs == null) return;
        g2.drawImage(imgs[rowIndex][colIndex], x, y, SOUND_SIZE, SOUND_SIZE, null);
    }

    // === Getter/Setter ===
    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
    public Rectangle getBounds() { return bounds; }
}
