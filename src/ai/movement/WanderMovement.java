package ai.movement;

import entity.Entity;

import java.util.Random;

/** Đi ngẫu nhiên: đổi hướng sau mỗi N frame. */
public class WanderMovement implements MovementController {
    private final int speed;
    private final int changeEveryFrames;
    private int counter = 0;
    private final Random rng = new Random();

    public WanderMovement(int speed, int changeEveryFrames) {
        this.speed = speed;
        this.changeEveryFrames = Math.max(1, changeEveryFrames);
    }

    @Override
    public void decide(Entity e) {
        e.actualSpeed = speed;

        counter++;
        if (counter >= changeEveryFrames) {
            counter = 0;
            int i = rng.nextInt(4);
            e.direction = (i == 0) ? "up" : (i == 1) ? "down" : (i == 2) ? "left" : "right";
        }
        // Không chỉnh worldX/worldY ở đây — việc đó do EntityMovement.move xử lý.
    }
}
