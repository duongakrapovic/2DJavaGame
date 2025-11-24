package ui.screens.pause;

import java.awt.*;
import java.awt.image.BufferedImage;
import ui.utilz.LoadSave;

import static ui.utilz.Constants.UI.URMButtons.*;

public class UrmButton {

    private final int x, y;
    private final int rowIndex;
    private BufferedImage[] imgs;
    private int index;         // chỉ số ảnh đang hiển thị
    private boolean focused;   // đang được chọn
    private boolean pressed;   // trạng thái nhấn
    private final Rectangle bounds; // vùng va chạm để UIManager highlight

    public UrmButton(int x, int y, int rowIndex) {
        this.x = x;
        this.y = y;
        this.rowIndex = rowIndex;
        loadImgs();
        this.bounds = new Rectangle(x, y, URM_SIZE, URM_SIZE);
    }
    // Tải từng frame của nút trong sprite sheet
    private void loadImgs() {
        BufferedImage sheet = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
        if (sheet == null) return;

        int tileW = sheet.getWidth()  / 3; // 3 trạng thái
        int tileH = sheet.getHeight() / 3; // 3 hàng (Resume, Replay, Menu)

        imgs = new BufferedImage[3];
        for (int col = 0; col < 3; col++) {
            imgs[col] = sheet.getSubimage(
                    col * tileW,
                    rowIndex * tileH,
                    tileW,
                    tileH
            );
        }
    }
    // Cập nhật trạng thái hiển thị
    public void update() {
        index = 0;              // bình thường
        if (focused) index = 1; // sáng khi focus
        if (pressed) index = 2; // trạng thái nhấn ưu tiên cao nhất
    }

    // Vẽ nút
    public void draw(Graphics2D g2) {
        if (imgs == null) return;
        g2.drawImage(imgs[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    // ==== Điều khiển bằng phím ====
    public void setFocused(boolean focused) { this.focused = focused; }
    public void setPressed(boolean pressed) { this.pressed = pressed; }
    public void reset() { focused = false; pressed = false; }

    // ==== Getter ====
    public Rectangle getBounds() { return bounds; }
    public int getRowIndex() { return rowIndex; }
}
