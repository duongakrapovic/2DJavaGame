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
}
