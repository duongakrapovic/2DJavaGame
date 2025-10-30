package ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import utilz.LoadSave;
import main.GamePanel;
import static utilz.Constants.UI.URMButtons.*;

/**
 * UrmButton — chuẩn hóa cho hệ thống GamePanel, điều khiển bằng bàn phím
 * Cắt theo kích thước gốc (56x56), vẽ scale theo GamePanel.SCALE
 */
public class UrmButton {
    private final int x, y;
    private final int rowIndex; // 0: Resume, 1: Replay, 2: Menu
    private BufferedImage[] imgs;
    private int index;
    private boolean focused;
    private boolean pressed;
    private final Rectangle bounds;

    public UrmButton(int x, int y, int rowIndex) {
        this.x = x;
        this.y = y;
        this.rowIndex = rowIndex;
        loadImgs();
        this.bounds = new Rectangle(x, y, URM_SIZE, URM_SIZE);
    }

    private void loadImgs() {
        BufferedImage sheet = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        if (sheet == null) return;

        int tileW = sheet.getWidth() / 3; // = 56
        int tileH = sheet.getHeight() / 3; // = 56
        imgs = new BufferedImage[3];
        for (int col = 0; col < 3; col++) {
            imgs[col] = sheet.getSubimage(col * tileW, rowIndex * tileH, tileW, tileH);
        }
    }

    /** Cập nhật frame hiển thị theo trạng thái focus/pressed */
    public void update() {
        index = 0;              // normal
        if (focused) index = 1; // sáng khi focus
        if (pressed) index = 2; // pressed ưu tiên cao nhất
    }

    public void draw(Graphics2D g2) {
        if (imgs == null) return;
        g2.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    // ===== Điều khiển bằng phím =====
    public void setFocused(boolean focused) { this.focused = focused; }
    public void setPressed(boolean pressed) { this.pressed = pressed; }
    public void reset() { focused = false; pressed = false; }

    // ===== Getter =====
    public Rectangle getBounds() { return bounds; }
    public int getRowIndex() { return rowIndex; }
}
