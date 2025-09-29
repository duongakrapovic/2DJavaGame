/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.Random;
import main.GamePanel;

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
        
        /*no need to check x-axis and y-axis separately
        because monster cannot move diagonally
        */
        e.collisionOn = false;
        int nextX = e.worldX + deltaMoveX; // posible move X
        int nextY = e.worldY + deltaMoveY; // posible move Y
        
        gp.cChecker.checkTile(e, nextX, nextY);
        gp.cChecker.checkEntity(e, gp.em.getObjects(gp.currentMap), nextX, nextY);
        gp.cChecker.checkPlayer(e, nextX, nextY);
        
        if (!e.collisionOn) {
            e.worldX = nextX;
            e.worldY = nextY;
        }
    }
    public void setAction(Entity e){
        e.actionLockCounter++ ;
        if(e.actionLockCounter >= 120){
            Random rand = new Random() ;
            int i = rand.nextInt(100) ;
            if(i <= 25){
                e.direction = "up" ;
            } else if (i <= 50) {
                e.direction = "down" ;
            } else if (i <= 75) {
                e.direction = "right" ;
            }else if (i <= 100) {
                e.direction = "left" ;
            }
            e.actionLockCounter = 0;
        }
    }
}
