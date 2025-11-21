package main;

import entity.Entity;
import object_data.WorldObject;
import tile.Chunk;

import java.awt.Rectangle;
import java.util.List;

public class CollisionChecker {

    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }
    
    //get tile number from chunk.tmx file 
    private int getTileNumAt(int worldX, int worldY){
        int tileCol = worldX / gp.tileSize;
        int tileRow = worldY / gp.tileSize;

        int chunkX = tileCol / gp.chunkSize;
        int chunkY = tileRow / gp.chunkSize;

        int localCol = tileCol % gp.chunkSize;
        int localRow = tileRow % gp.chunkSize;

        for(Chunk c : gp.chunkM.getActiveChunks()){
            if(c.chunkX == chunkX && c.chunkY == chunkY){
                if(localRow >= 0 && localRow < c.size &&
                   localCol >= 0 && localCol < c.size){
                    return c.mapTileNum[localRow][localCol];
                }
            }
        }
        return 0; // default tile
    }

    public void checkTile(Entity entity,int nextX, int nextY){
        int entityLeftWorldX  = nextX + entity.solidArea.x;
        int entityRightWorldX = nextX + entity.solidArea.x + entity.solidArea.width - 1;
        int entityTopWorldY   = nextY + entity.solidArea.y;
        int entityBotWorldY   = nextY + entity.solidArea.y + entity.solidArea.height - 1;
        
        int tileNum1, tileNum2;

        // Check X
        tileNum1 = getTileNumAt(entityLeftWorldX, entityTopWorldY);
        tileNum2 = getTileNumAt(entityLeftWorldX, entityBotWorldY);
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionXOn = true;
            entity.collisionOn = true;
        }

        tileNum1 = getTileNumAt(entityRightWorldX, entityTopWorldY);
        tileNum2 = getTileNumAt(entityRightWorldX, entityBotWorldY);
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionXOn = true;
            entity.collisionOn = true;
        }

        // Check Y
        tileNum1 = getTileNumAt(entityLeftWorldX, entityTopWorldY);
        tileNum2 = getTileNumAt(entityRightWorldX, entityTopWorldY);
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionYOn = true;
            entity.collisionOn = true;
        }

        tileNum1 = getTileNumAt(entityLeftWorldX, entityBotWorldY);
        tileNum2 = getTileNumAt(entityRightWorldX, entityBotWorldY);
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionYOn = true;
            entity.collisionOn = true;
        }
    }
    // this check player -> entity 
    // this check player -> entity (NPC / Monster)
    public int checkEntity(Entity entity , List<Entity> targets, int nextX, int nextY){
        int index = 999;

        // === tính offset theo hướng nhìn để va chạm dễ bắt hơn ===
        int offsetX = 0, offsetY = 0;
        switch (entity.direction) {
            case "up" -> offsetY = -entity.actualSpeed;
            case "down" -> offsetY = entity.actualSpeed;
            case "left" -> offsetX = -entity.actualSpeed;
            case "right" -> offsetX = entity.actualSpeed;
        }

        // set solidArea cho entity (đã có offset)
        entity.solidArea.x = nextX + entity.solidArea.x + offsetX;
        entity.solidArea.y = nextY + entity.solidArea.y + offsetY;

        for (int i = 0; i < targets.size(); i++) {
            Entity target = targets.get(i);
            if (target != null && target.mapIndex == entity.mapIndex) {
                target.solidArea.x = target.worldX + target.solidArea.x;
                target.solidArea.y = target.worldY + target.solidArea.y;

                if (entity.solidArea.intersects(target.solidArea)) {
                    if (target.collision) entity.collisionOn = true;
                    index = i; // trả về index va chạm
                }

                // reset
                target.solidArea.x = target.solidAreaDefaultX;
                target.solidArea.y = target.solidAreaDefaultY;
            }
        }

        // reset entity
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        return index;
    }


    // this check entity -> player 
    public void checkPlayer(Entity entity , int nextX ,int nextY){
        // set solidArea for entity 
        entity.solidArea.x = nextX + entity.solidArea.x;
        entity.solidArea.y = nextY + entity.solidArea.y;

        // set solidArea for player
        gp.em.getPlayer().solidArea.x = gp.em.getPlayer().worldX + gp.em.getPlayer().solidArea.x;
        gp.em.getPlayer().solidArea.y = gp.em.getPlayer().worldY + gp.em.getPlayer().solidArea.y;

        if (entity.solidArea.intersects(gp.em.getPlayer().solidArea)) {
            entity.collisionOn = true;
        }

        // reset solidArea
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.em.getPlayer().solidArea.x = gp.em.getPlayer().solidAreaDefaultX; 
        gp.em.getPlayer().solidArea.y = gp.em.getPlayer().solidAreaDefaultY;
    }
    // return object touched
    public int checkWorldObject(Entity mover, List<WorldObject> objects, int nextDX, int nextDY) {
        if (mover == null || objects == null || objects.isEmpty()) return 999;

        Rectangle moverSolid = mover.solidArea != null
                ? new Rectangle(mover.solidArea)
                : new Rectangle(0, 0, mover.width, mover.height);

        int nextWorldX = mover.worldX + nextDX;
        int nextWorldY = mover.worldY + nextDY;

        Rectangle moverNext = new Rectangle(
                nextWorldX + moverSolid.x,
                nextWorldY + moverSolid.y,
                moverSolid.width,
                moverSolid.height
        );

        int interactedIndex = 999;

        for (int i = 0; i < objects.size(); i++) {
            WorldObject obj = objects.get(i);
            if (obj == null || obj.mapIndex != mover.mapIndex) continue;

            Rectangle objSolid = obj.solidArea != null
                    ? obj.solidArea
                    : new Rectangle(0, 0, obj.width, obj.height);

            Rectangle objRect = new Rectangle(
                    obj.worldX + objSolid.x,
                    obj.worldY + objSolid.y,
                    objSolid.width,
                    objSolid.height
            );

            if (moverNext.intersects(objRect)) {
                if (obj.collision) mover.collisionOn = true;

                // Ưu tiên door hơn các object khác
                if (interactedIndex == 999) {
                    interactedIndex = i;
                } else {
                    WorldObject current = objects.get(interactedIndex);
                    if (!"door".equals(current.name) && "door".equals(obj.name)) {
                        interactedIndex = i; // ưu tiên door
                    }
                }
            }
        }
    return interactedIndex;
    }
}

