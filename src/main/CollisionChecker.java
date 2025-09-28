/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import java.util.List;

import entity.Entity;
import tile.Chunk;

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
    public int checkEntity(Entity entity ,List<Entity> targets, int nextX, int nextY){
        int index = 999;

        // set solidArea cho entity
        entity.solidArea.x = nextX + entity.solidArea.x;
        entity.solidArea.y = nextY + entity.solidArea.y;

        for(int i = 0 ; i < targets.size() ; i++){
            Entity target = targets.get(i);
            if(target != null){
                target.solidArea.x = target.worldX + target.solidArea.x;
                target.solidArea.y = target.worldY + target.solidArea.y;

                if (entity.solidArea.intersects(target.solidArea)) {
                    if (target.collision) {
                        entity.collisionOn = true;
                    }
                    index = i; // trả về index va chạm
                }

                // reset
                target.solidArea.x = target.solidAreaDefaultX;
                target.solidArea.y = target.solidAreaDefaultY;
            }
        }

        // reset lại entity
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
        gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
        gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;

        if (entity.solidArea.intersects(gp.player.solidArea)) {
            entity.collisionOn = true;
        }

        // reset solidArea
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        gp.player.solidArea.x = gp.player.solidAreaDefaultX;
        gp.player.solidArea.y = gp.player.solidAreaDefaultY;
    }
}

