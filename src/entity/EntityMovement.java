package entity;

import main.GamePanel;
import object_data.WorldObject;

import java.util.List;

public class EntityMovement {
    private final GamePanel gp;
    public EntityMovement(GamePanel gp){ this.gp = gp; }

    //move after direction
    public void moveByDirection(Entity e){
        int dx = 0, dy = 0;
        switch (e.direction) {
            case "up":    dy = -e.actualSpeed; break;
            case "down":  dy =  e.actualSpeed; break;
            case "left":  dx = -e.actualSpeed; break;
            case "right": dx =  e.actualSpeed; break;
        }
        moveWithDelta(e, dx, dy);
    }

    // move after AI movement
    public void moveWithDelta(Entity e, int dx, int dy){
        e.collisionOn = false;

        int nextX = e.worldX + dx;
        int nextY = e.worldY + dy;

        gp.cChecker.checkTile(e, nextX, nextY);

        List<WorldObject> objs = gp.em.getWorldObjects(gp.currentMap);
        int objIndex = gp.cChecker.checkWorldObject(e, objs, dx, dy);
        if (objIndex != 999) {
            var obj = objs.get(objIndex);
            if (obj != null && obj.collision) e.collisionOn = true;
        }

        gp.cChecker.checkPlayer(e, nextX, nextY);

        if (!e.collisionOn) {
            e.worldX = nextX;
            e.worldY = nextY;
        }
    }
    // EntityMovement.java
    public void applyKnockback(Entity e) {
        // Đẩy từng pixel để collision chính xác (kể cả góc)
        int vx = e.velX;
        int vy = e.velY;

        // --- Trục X ---
        if (vx != 0) {
            int stepX = (vx > 0) ? 1 : -1;
            for (int i = 0; i < Math.abs(vx); i++) {
                if (willCollide(e, stepX, 0)) {        // kẹt → triệt tiêu vận tốc trục X
                    e.velX = 0;
                    break;
                }
                e.worldX += stepX;
            }
        }

        // --- Trục Y ---
        if (vy != 0) {
            int stepY = (vy > 0) ? 1 : -1;
            for (int i = 0; i < Math.abs(vy); i++) {
                if (willCollide(e, 0, stepY)) {        // kẹt → triệt tiêu vận tốc trục Y
                    e.velY = 0;
                    break;
                }
                e.worldY += stepY;
            }
        }

        // Giảm thời lượng knockback mỗi frame
        e.setKnockbackCounter(e.getKnockbackCounter() - 1);

        // Hết thời lượng hoặc bị kẹt cả hai trục → tắt KB
        if (e.getKnockbackCounter() <= 0 || (e.velX == 0 && e.velY == 0)) {
            e.clearVelocity();
            e.setKnockbackCounter(0);
        }
    }

    private boolean willCollide(Entity e, int dx, int dy) {
        e.collisionOn = false;

        int nextX = e.worldX + dx;
        int nextY = e.worldY + dy;

        // Tiles
        gp.cChecker.checkTile(e, nextX, nextY);

        var objs = gp.em.getWorldObjects(gp.currentMap);
        int objIndex = gp.cChecker.checkWorldObject(e, objs, dx, dy);
        if (objIndex != 999) {
            var obj = objs.get(objIndex);
            if (obj != null && obj.collision) e.collisionOn = true;
        }

        // Player/Entity (nếu có check va chạm với player/others)
        gp.cChecker.checkPlayer(e, nextX, nextY);

        return e.collisionOn;
    }

}
