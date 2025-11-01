package ui;

import main.GamePanel;
import main.GameState;
import java.awt.*;

/**
 * BaseUI.java
 * Lớp cơ sở cho mọi UI trong game (PauseOverlay, GameOverUI, HealthUI, v.v.)
 * Cung cấp khung phương thức update() và draw(), cùng khả năng kiểm soát hiển thị.
 */
public abstract class BaseUI {
    protected final GamePanel gp;

    public BaseUI(GamePanel gp) {
        this.gp = gp;
    }

    /**
     * Cập nhật logic UI (nếu cần).
     * Gọi mỗi khung hình nếu shouldUpdate() = true.
     */
    public abstract void update();

    /**
     * Vẽ UI lên màn hình.
     * Gọi mỗi khung hình nếu shouldDraw() = true.
     */
    public abstract void draw(Graphics2D g2);


    public boolean shouldRenderIn(GameState state) {
        return true;
    }
    /**
     * Xác định UI có được cập nhật ở khung hình hiện tại không.
     * Mặc định: luôn update.
     */
    public boolean shouldUpdate() {
        return true;
    }

    /**
     * Xác định UI có được vẽ ở khung hình hiện tại không.
     * Mặc định: luôn vẽ.
     */
    public boolean shouldDraw() {
        return true;
    }
}
