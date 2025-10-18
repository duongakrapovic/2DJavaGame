package ai.movement;

import entity.Entity;
import player_manager.Player;
import main.GamePanel;

/** Đuổi theo player: set hướng theo vector (dx, dy). */
public class ChaseMovement implements MovementController {
    private final GamePanel gp;
    private final Player target;
    private final int moveSpeed;     // px/frame
    private final int stopRadius;    // px, đứng lại khi đủ gần

    public ChaseMovement(GamePanel gp, Player target, int moveSpeed, int stopRadiusPx) {
        this.gp = gp;
        this.target = target;
        this.moveSpeed = moveSpeed;
        this.stopRadius = Math.max(0, stopRadiusPx);
    }

    @Override
    public void decide(Entity e) {
        if (target == null) return;
        e.actualSpeed = moveSpeed;

        int dx = target.worldX - e.worldX;
        int dy = target.worldY - e.worldY;

        // đủ gần -> đứng im (không thay direction để vẫn quay mặt cũ)
        if ((long)dx*dx + (long)dy*dy <= (long)stopRadius*stopRadius) {
            e.actualSpeed = 0;
            return;
        }

        // chọn trục có độ lệch lớn hơn
        if (Math.abs(dx) >= Math.abs(dy)) {
            e.direction = (dx >= 0) ? "right" : "left";
        } else {
            e.direction = (dy >= 0) ? "down" : "up";
        }
    }
}
