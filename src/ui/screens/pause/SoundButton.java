package ui.screens.pause;

import java.awt.*;
import java.awt.image.BufferedImage;
import ui.utilz.LoadSave;

import static ui.utilz.Constants.UI.PauseButtons.SOUND_SIZE;

public class SoundButton {

    private int x, y;
    private boolean muted;
    private BufferedImage[][] imgs;

    private int rowIndex, colIndex;
    private Rectangle bounds;

    public SoundButton(int x, int y) {
        this.x = x;
        this.y = y;
        loadImgs();
        bounds = new Rectangle(x, y, SOUND_SIZE, SOUND_SIZE);
    }
    // Tải atlas nút âm thanh (2 hàng × 3 cột)
    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SOUND_BUTTONS);
        if (temp == null) return;

        int tileW = temp.getWidth()  / 3;
        int tileH = temp.getHeight() / 2;
        imgs = new BufferedImage[2][3];

        for (int row = 0; row < 2; row++)
            for (int col = 0; col < 3; col++)
                imgs[row][col] = temp.getSubimage(
                        col * tileW,
                        row * tileH,
                        tileW,
                        tileH
                );
    }
    // Cập nhật trạng thái hình ảnh (chỉ đổi theo muted)
    public void update() {
        rowIndex = muted ? 1 : 0;  // 0=bật, 1=tắt
        colIndex = 0;              // vì điều khiển bằng phím → không có hover/press
    }

    // Vẽ nút âm thanh
    public void draw(Graphics2D g2) {
        if (imgs == null) return;
        g2.drawImage(imgs[rowIndex][colIndex], x, y, SOUND_SIZE, SOUND_SIZE, null);
    }

    // === Getter / Setter ===
    public boolean isMuted() { return muted; }
    public void setMuted(boolean muted) { this.muted = muted; }
    public Rectangle getBounds() { return bounds; }
}
