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

    public ChaseMovement(GamePanel gp, Supplier<Player> targetSup, int moveSpeed, int stopRadiusPx) {
        this.gp = gp;
        this.targetSup = targetSup;
        this.moveSpeed = moveSpeed;
        this.stopRadius = Math.max(0, stopRadiusPx);
    }

    @Override
    public void decide(Entity e) {
        Player target = (targetSup != null) ? targetSup.get() : null;
        if (target == null) return;           // <-- an toàn nếu chưa có player
        e.actualSpeed = moveSpeed;

        int dx = target.worldX - e.worldX;
        int dy = target.worldY - e.worldY;

        if ((long) dx * dx + (long) dy * dy <= (long) stopRadius * stopRadius) {
            e.actualSpeed = 0;
            return;
        }

        if (Math.abs(dx) >= Math.abs(dy)) {
            e.direction = (dx >= 0) ? "right" : "left";
        } else {
            e.direction = (dy >= 0) ? "down" : "up";
        }
    }
}
