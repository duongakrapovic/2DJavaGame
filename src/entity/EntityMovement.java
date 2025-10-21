package entity;

import main.GamePanel;
import object_data.WorldObject;

import java.util.List;

/** Di chuyển có va chạm; hỗ trợ cả theo direction lẫn (dx,dy). */
public class EntityMovement {
    private final GamePanel gp;
    public EntityMovement(GamePanel gp){ this.gp = gp; }

    /** Di chuyển theo direction * actualSpeed (dành cho controller decide). */
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

    /** Di chuyển theo (dx,dy) đã tính sẵn (fallback/AI kiểu khác). */
    public void moveWithDelta(Entity e, int dx, int dy){
        e.collisionOn = false;

        int nextX = e.worldX + dx;
        int nextY = e.worldY + dy;

        // 1) tile collision
        gp.cChecker.checkTile(e, nextX, nextY);

        // 2) world-object collision
        List<WorldObject> objs = gp.em.getWorldObjects(gp.currentMap);
        int objIndex = gp.cChecker.checkWorldObject(e, objs, dx, dy);
        if (objIndex != 999) {
            var obj = objs.get(objIndex);
            if (obj != null && obj.collision) e.collisionOn = true;
        }

        // 3) entity -> player
        gp.cChecker.checkPlayer(e, nextX, nextY);

        if (!e.collisionOn) {
            e.worldX = nextX;
            e.worldY = nextY;
        }
    }
}
