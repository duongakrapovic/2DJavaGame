package entity;

import java.util.Random;
import main.GamePanel;
import object_data.WorldObject;          // <- thêm import
// (nếu IDE báo unused do bạn không dùng trực tiếp cũng OK)

public class EntityMovement {
    private final GamePanel gp;
    public EntityMovement(GamePanel gp){
        this.gp = gp;
    }

    public void move(Entity e){
        int deltaMoveX = 0;
        int deltaMoveY = 0;
        switch (e.direction) {
            case "up":    deltaMoveY = -e.actualSpeed; break;
            case "down":  deltaMoveY =  e.actualSpeed; break;
            case "left":  deltaMoveX = -e.actualSpeed; break;
            case "right": deltaMoveX =  e.actualSpeed; break;
        }

        e.collisionOn = false;

        int nextX = e.worldX + deltaMoveX;
        int nextY = e.worldY + deltaMoveY;

        // 1) tile collision
        gp.cChecker.checkTile(e, nextX, nextY);

        // 2) world-object collision (dùng API mới, truyền delta)
        int objIndex = gp.cChecker.checkWorldObject(
                e,
                gp.em.getWorldObjects(gp.currentMap),   // <- dùng list WorldObject
                deltaMoveX,
                deltaMoveY
        );
        if (objIndex != 999) {
            // nếu object có collision=true thì chặn di chuyển
            var obj = gp.em.getWorldObjects(gp.currentMap).get(objIndex);
            if (obj != null && obj.collision) e.collisionOn = true;
        }

        // 3) entity -> player
        gp.cChecker.checkPlayer(e, nextX, nextY);

        // move nếu không bị chặn
        if (!e.collisionOn) {
            e.worldX = nextX;
            e.worldY = nextY;
        }
    }

    public void setAction(Entity e){
        e.actionLockCounter++;
        if(e.actionLockCounter >= 120){
            Random rand = new Random();
            int i = rand.nextInt(100);
            if(i <= 25)      e.direction = "up";
            else if(i <= 50) e.direction = "down";
            else if(i <= 75) e.direction = "right";
            else             e.direction = "left";
            e.actionLockCounter = 0;
        }
    }
}
