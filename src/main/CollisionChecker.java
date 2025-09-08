/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import entity.Entity;

public class CollisionChecker {

    GamePanel gp;
    public CollisionChecker(GamePanel gp){
        this.gp = gp;
    }
    
    public void checkTile(Entity entity,int nextX, int nextY){
        //top y bot y left x right x edge
        int entityLeftWorldX = nextX + entity.solidArea.x;
        int entityRightWorldX =  nextX + entity.solidArea.x + entity.solidArea.width - 1;
        int entityTopWorldY = nextY + entity.solidArea.y;
        int entityBotWorldY = nextY + entity.solidArea.y+ entity.solidArea.height - 1;
        
        int entityLeftCol = entityLeftWorldX / gp.tileSize;
        int entityRightCol = entityRightWorldX / gp.tileSize;
        int entityTopRow = entityTopWorldY / gp.tileSize;
        int entityBotRow = entityBotWorldY / gp.tileSize; 
        
        /*
        |-----------------------------|
        |       left x        right x |
        |       |             |       |
        |       |-------------|-----  | top y 
        |       |\            |       |
        |       |  \  solid   |       |
        |       |    \        |       |
        |       |     \       |       |
        |       |  Area \     |       |
        |       |         \   |       |
        |       |-------------|-------| bot y
        |_____________________________|______
        */
        
        // only max 2 tiles at a time for 1 direction to check
        // if pos bettween 4 tiles , there are 2 tiles has to be check
        // for each direction
        
        int tileNum1 , tileNum2; // copy num of the tile in txt file 
      
        // check X 
        // Check the left side of the entity (top-left and bottom-left corners)
        tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
        tileNum2 = gp.tileM.mapTileNum[entityBotRow][entityLeftCol];
        // If either tile is solid (collision = true), block movement in X
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionXOn = true;
            entity.collisionOn = true;
        }
        
        // Check the right side of the entity (top-right and bottom-right corners)
        tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityRightCol];
        tileNum2 = gp.tileM.mapTileNum[entityBotRow][entityRightCol];
        // If either tile is solid, block movement in X
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionXOn = true;
            entity.collisionOn = true;
        }

        // check Y
        // Check the top side of the entity (top-left and top-right corners)
        tileNum1 = gp.tileM.mapTileNum[entityTopRow][entityLeftCol];
        tileNum2 = gp.tileM.mapTileNum[entityTopRow][entityRightCol];
        // If either tile is solid, block movement in Y
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionYOn = true;
            entity.collisionOn = true;
        }
        
        // Check the bottom side of the entity (bottom-left and bottom-right corners)
        tileNum1 = gp.tileM.mapTileNum[entityBotRow][entityLeftCol];
        tileNum2 = gp.tileM.mapTileNum[entityBotRow][entityRightCol];
        // If either tile is solid, block movement in Y
        if (gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision) {
            entity.collisionYOn = true;
            entity.collisionOn = true;
        }
        
        /*there are some bug:
        1 : if move in a diagonally direction and touch the collision tile,
        player get stuck in and only can move the opposite direction of that tile
        2 :If the position touches the boundary, it is impossible to press 2 keys
        to move diagonally and the result is to move in a normal direction
        */
       
    }
    public int checkObject(Entity entity , boolean player,int nextX, int nextY){
        /* we check if player is hitting any object
        if true return index of object
        */
        int index = 999;
        // get entity's solid pos
        entity.solidArea.x = nextX + entity.solidArea.x;
        entity.solidArea.y = nextY + entity.solidArea.y;
        for(int i = 0 ; i <gp.obj.length ; i++){
            if(gp.obj[i] != null){
                
                // get the object's solid area position
                gp.obj[i].solidArea.x = gp.obj[i].worldX + gp.obj[i].solidArea.x;
                gp.obj[i].solidArea.y = gp.obj[i].worldY + gp.obj[i].solidArea.y;
                /*since we have set 0 to the object's solid area's x and y
                the latter part does not add anythign. 
                but i include that so the code still works even if you set 
                specific values in each object. 
                */ 
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
                //intersects  was using here because we only scan a small number of object      
                // using this for map etc can make game slow because of scanning to much 
                        
                }              
            }   
        //reset 
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY; 
        return index;
    }
            
}    
