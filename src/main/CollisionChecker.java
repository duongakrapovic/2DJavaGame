/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

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

    public int checkObject(Entity entity , boolean player,int nextX, int nextY){
        int index = 999;

        entity.solidArea.x = nextX + entity.solidArea.x;
        entity.solidArea.y = nextY + entity.solidArea.y;

        for(int i = 0 ; i <gp.obj.length ; i++){
            if(gp.obj[i] != null){
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;

                if (entity.solidArea.intersects(gp.obj[i].solidArea)) {
                    if (gp.obj[i].collision) {
                        entity.collisionOn = true;
                    }
                    if (player) {
                        index = i;
                    }
                }

                gp.obj[i].solidArea.x = gp.obj[i].solidAreaDefaultX;
                gp.obj[i].solidArea.y = gp.obj[i].solidAreaDefaultY;
            }
        }

        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY; 
        return index;
    }
}

