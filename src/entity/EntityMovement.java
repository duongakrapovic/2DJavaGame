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

        List<WorldObject> objs = gp.om.getObjects(gp.currentMap);
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
    public void applyKnockback(Entity e) {
        int vx = Math.max(-24, Math.min(24, e.velX));
        int vy = Math.max(-24, Math.min(24, e.velY));

        // --- X ---
        if (vx != 0) {
            int stepX = (vx > 0) ? 1 : -1;
            for (int i = 0; i < Math.abs(vx); i++) {
                if (willCollide(e, stepX, 0)) { e.velX = 0; break; }
                e.worldX += stepX;
            }
        }
        // --- Y ---
        if (vy != 0) {
            int stepY = (vy > 0) ? 1 : -1;
            for (int i = 0; i < Math.abs(vy); i++) {
                if (willCollide(e, 0, stepY)) { e.velY = 0; break; }
                e.worldY += stepY;
            }
        }

        e.setKnockbackCounter(e.getKnockbackCounter() - 1);
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

        // Objects
        var objs = gp.om.getObjects(gp.currentMap);
        int objIndex = gp.cChecker.checkWorldObject(e, objs, dx, dy);
        if (objIndex != 999) {
            var obj = objs.get(objIndex);
            if (obj != null && obj.collision) e.collisionOn = true;
        }

        if (!(e instanceof player_manager.Player)) {
            gp.cChecker.checkPlayer(e, nextX, nextY);        // quái check va chạm với Player
        } else {
        }

        return e.collisionOn;
    }
}
