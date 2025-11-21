package ai.movement;

import entity.Entity;
import main.GamePanel;
import player_manager.Player;

import java.util.function.Supplier;

public class ChaseMovement implements MovementController {
    private final GamePanel gp;
    private final Supplier<Player> targetSup;
    private final int moveSpeed;
    private final int stopRadius;

    // --- chống giật khi player chạy chéo ---
    private final int retargetEveryFrames = 8; // bao lâu xem xét đổi hướng 1 lần
    private final int minHoldFrames       = 8; // giữ nguyên hướng tối thiểu
    private final int axisBiasPx          = 6; // chênh lệch px cần thiết để đổi trục

    // state
    private int counter = 0, hold = 0;
    private String currentDir = "down";

    public ChaseMovement(GamePanel gp, Supplier<Player> targetSup, int moveSpeed, int stopRadiusPx) {
        this.gp = gp;
        this.targetSup = targetSup;
        this.moveSpeed = moveSpeed;
        this.stopRadius = Math.max(0, stopRadiusPx);
    }

    @Override
    public void decide(Entity e) {
        Player target = (targetSup != null) ? targetSup.get() : null;
        if (target == null) return; // an toàn nếu chưa có player
        e.actualSpeed = moveSpeed;

        int dx = target.worldX - e.worldX;
        int dy = target.worldY - e.worldY;

        long r2 = (long) stopRadius * (long) stopRadius;
        long d2 = (long) dx * (long) dx + (long) dy * (long) dy;
        if (d2 <= r2) { // đứng lại khi đủ gần
            e.actualSpeed = 0;
            return;
        }

        // giữ hướng tối thiểu để tránh rung khi dx≈dy
        if (hold < minHoldFrames) {
            hold++;
            e.direction = currentDir;
            return;
        }

        // chỉ retarget định kỳ
        counter++;
        if (counter >= retargetEveryFrames) {
            counter = 0;
            hold = 0;

            // chọn trục ưu thế có "bias" để không lắc khi dx≈dy
            if (Math.abs(dx) > Math.abs(dy) + axisBiasPx) {
                currentDir = (dx >= 0) ? "right" : "left";
            } else if (Math.abs(dy) > Math.abs(dx) + axisBiasPx) {
                currentDir = (dy >= 0) ? "down" : "up";
            } else {
                // khi chênh lệch nhỏ, giữ hướng cũ để khỏi giật
            }
        }

        e.direction = currentDir;
    }
}
