/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package monster;

import java.awt.Rectangle;
import java.util.Random;

import entity.Entity;
import main.GamePanel;

public class SlimeMonster extends Entity {
    GamePanel gp;
    public SlimeMonster(GamePanel gp){
        super(gp);
        this.gp = gp;
        name = "Green Slime";
        width = gp.tileSize;
        height = gp.tileSize;
        getImage();
        
        collision = true;
        animationON = true;
        actualSpeed = 1 ;
        
        solidArea = new Rectangle();
        solidArea.x = 3 ;
        solidArea.y = 18 ;
        solidArea.width = 42 ;
        solidArea.height = 30 ;
        solidAreaDefaultX = solidArea.x ;
        solidAreaDefaultY = solidArea.y ;
        
    }
    public void getImage(){
        up1 = setup("/monster/greenslime_down_1" , width , height) ;
        up2 = setup("/monster/greenslime_down_2" , width ,  height) ;
        down1 = setup("/monster/greenslime_down_1" , width, height) ;
        down2 = setup("/monster/greenslime_down_2" , width, height) ;
        right1 = setup("/monster/greenslime_down_1", width , height) ;
        right2 = setup("/monster/greenslime_down_2" , width, height) ;
        left1 = setup("/monster/greenslime_down_1" , width, height) ;
        left2 = setup("/monster/greenslime_down_2" , width , height) ;
    }
    @Override
    public void update() {
        
        // no time to finish monster moving
        setAction();
        spriteCounter++;
        if(spriteCounter > 8){
            if(spriteNum == 1){
                spriteNum = 2;
            }
            else if(spriteNum == 2){
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        
        int deltaMoveX = 0;
        int deltaMoveY = 0;
        switch (direction) {
            case "up":    deltaMoveY = -actualSpeed; break;
            case "down":  deltaMoveY =  actualSpeed; break;
            case "left":  deltaMoveX = -actualSpeed; break;
            case "right": deltaMoveX =  actualSpeed; break;
        }
        
        /*no need to check x-axis and y-axis separately
        because monster cannot move diagonally
        */
        collisionOn = false;
        int nextX = worldX + deltaMoveX; // posible move X
        int nextY = worldY + deltaMoveY; // posible move Y
        gp.cChecker.checkTile(this, nextX, nextY);
        gp.cChecker.checkObject(this, true, nextX, nextY);
        if (!collisionOn) {
            worldX = nextX;
            worldY = nextY;
        }
    }
    public void setAction(){
        actionLockCounter++ ;
        if(actionLockCounter >= 120){
            Random rand = new Random() ;
            int i = rand.nextInt(100) ;
            if(i <= 25){
                direction = "up" ;
            } else if (i <= 50) {
                direction = "down" ;
            } else if (i <= 75) {
                direction = "right" ;
            }else if (i <= 100) {
                direction = "left" ;
            }
            actionLockCounter = 0;
        }
    }
}
