package ai.movement;

import entity.Entity;
import java.util.Random;

public class WanderMovement implements MovementController {
    private final int speed;
    private final int changeEveryFrames;
    private final int minHoldFrames; // giữ hướng tối thiểu để tránh giật
    private final boolean bounded;
    private final int minX, minY, maxX, maxY; // vùng [min, max] theo world px
    private final int fencePadding; // "rào mềm" tính bằng px

    private int counter = 0, hold = 0;
    private String currentDir = "down";
    private final Random rng = new Random();

    public WanderMovement(int speed, int changeEveryFrames) {
        this(speed, changeEveryFrames, /*minHoldFrames*/8);
    }

    /** Không giới hạn vùng, nhưng mượt hơn nhờ giữ hướng tối thiểu. */
    public WanderMovement(int speed, int changeEveryFrames, int minHoldFrames) {
        this.speed = speed;
        this.changeEveryFrames = Math.max(1, changeEveryFrames);
        this.minHoldFrames = Math.max(0, minHoldFrames);
        this.bounded = false;
        this.minX = this.minY = this.maxX = this.maxY = this.fencePadding = 0;
    }

    /** Giới hạn trong vùng chữ nhật [minX,minY]—[maxX,maxY] với “rào mềm” fencePadding. */
    public WanderMovement(int speed, int changeEveryFrames, int minHoldFrames,
                          int minX, int minY, int maxX, int maxY, int fencePadding) {
        this.speed = speed;
        this.changeEveryFrames = Math.max(1, changeEveryFrames);
        this.minHoldFrames = Math.max(0, minHoldFrames);
        this.bounded = true;
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.fencePadding = Math.max(0, fencePadding);
    }

    @Override
    public void decide(Entity e) {
        e.actualSpeed = speed;

        // Nếu đang “giữ hướng” và (không sát rào) thì giữ tiếp
        if (hold < minHoldFrames && !(bounded && nearFence(e, currentDir))) {
            hold++;
            e.direction = currentDir;
            return;
        }

        counter++;
        boolean needChange = counter >= changeEveryFrames
                || (bounded && nearFence(e, currentDir))
                || isBlocked(e);

        if (needChange) {
            counter = 0; hold = 0;

            // Danh sách hướng ứng viên
            String[] candidates = new String[] { "up", "down", "left", "right" };
            String[] valid = bounded ? filterValidDirections(e, candidates) : candidates;

            if (valid.length == 0) { // phòng trường hợp kẹt rào + va chạm
                valid = candidates;
            }

            // Nếu sát rào, ưu tiên hướng quay vào trong
            String inward = bounded ? pickInwardIfNearFence(e) : null;
            if (inward != null && contains(valid, inward)) {
                currentDir = inward;
            } else {
                currentDir = valid[rng.nextInt(valid.length)];
            }
        }

        e.direction = currentDir;
    }

    // --- helpers ---

    private boolean isBlocked(Entity e) {
        // Dựa trên cờ collision được set ở bước move tách trục của engine
        return e.collisionXOn || e.collisionYOn;
    }

    private String[] filterValidDirections(Entity e, String[] dirs) {
        java.util.List<String> ok = new java.util.ArrayList<>();
        for (String d : dirs) if (!wouldExceedBounds(e, d)) ok.add(d);
        return ok.toArray(new String[0]);
    }

    private boolean wouldExceedBounds(Entity e, String dir) {
        if (!bounded) return false;
        int nx = e.worldX, ny = e.worldY;
        switch (dir) {
            case "up":    ny -= speed; break;
            case "down":  ny += speed; break;
            case "left":  nx -= speed; break;
            case "right": nx += speed; break;
        }
        return nx < minX || nx > maxX || ny < minY || ny > maxY;
    }

    private boolean nearFence(Entity e, String dir) {
        int nx = e.worldX, ny = e.worldY;
        switch (dir) {
            case "up":    ny -= speed; break;
            case "down":  ny += speed; break;
            case "left":  nx -= speed; break;
            case "right": nx += speed; break;
        }
        return nx <= minX + fencePadding || nx >= maxX - fencePadding
                || ny <= minY + fencePadding || ny >= maxY - fencePadding;
    }

    private String pickInwardIfNearFence(Entity e) {
        if (!bounded) return null;
        if (e.worldY <= minY + fencePadding) return "down";
        if (e.worldY >= maxY - fencePadding) return "up";
        if (e.worldX <= minX + fencePadding) return "right";
        if (e.worldX >= maxX - fencePadding) return "left";
        return null;
    }

    private boolean contains(String[] arr, String v) {
        for (String s : arr) if (s.equals(v)) return true;
        return false;
    }
}
