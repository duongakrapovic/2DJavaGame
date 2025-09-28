/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// this stores variables that will be use in player , monster and npc class.
package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
 
public class Entity {
    
    // default setting 
    public int worldX, worldY;
    public int width, height;
    public final int screenX;
    public final int screenY;
    
    // system
    UtilityTool uTool = new UtilityTool();
    protected GamePanel gp;
    public int mapIndex = 0;
    
    // set for animaion
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction = "down";
    public int spriteCounter = 0;
    public int spriteNum = 1;
    public int actionLockCounter = 0;
    
    // set collision auto false
    public Rectangle solidArea;
    public boolean collisionXOn = false;
    public boolean collisionYOn = false;
    public boolean collisionOn = false;
    
    // set for colision area of entity
    public int solidAreaDefaultX, solidAreaDefaultY;
    
    //enity state
    public BufferedImage staticImage;// use for enity without animation
    public String name;
    public boolean collision  = false;
    public boolean animationON = false;
    
    //set for utility
    public int defaultSpeed, actualSpeed, buffSpeed;
    private int HP = 0;

    public int getHP() {return HP;}
    public void setHP(int HP) {this.HP = HP;}
   

    public Entity(GamePanel gp) {
        this.gp = gp;
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);  
    }
    public BufferedImage setup(String imagePath, int width , int height){
        
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath +".png"));
            image = uTool.scaleImage(image, width , height);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public void draw(Graphics2D g2){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
            // only draw if object in the right area
            // i dont know how to apply culling methot same as the movement 
            
            BufferedImage image = null;
            if(animationON == true){
                switch(direction){
                case "up":
                    if(spriteNum == 1) image = up1;
                    if(spriteNum == 2) image = up2;   
                    break;
                case "down":
                    if(spriteNum == 1) image = down1;
                    if(spriteNum == 2) image = down2;
                    break;
                case "left":
                    if(spriteNum == 1) image = left1;
                    if(spriteNum == 2) image = left2;
                    break;
                case "right":
                    if(spriteNum == 1) image = right1;
                    if(spriteNum == 2) image = right2;
                    break;
                }
            }
            else{
                image = staticImage;
            }
  
            g2.drawImage(image, screenX , screenY , width, height , null); 
            // Debug: rec solidArea
            g2.setColor(Color.RED);
            int solidScreenX = screenX + solidArea.x;
            int solidScreenY = screenY + solidArea.y;
            g2.drawRect(solidScreenX, solidScreenY, solidArea.width, solidArea.height);
        }
    }
    public void update() {
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
        gp.cChecker.checkEntity(this, gp.em.getObjects(gp.currentMap), nextX, nextY);
        gp.cChecker.checkPlayer(this, nextX, nextY);
        
        if (!collisionOn) {
            worldX = nextX;
            worldY = nextY;
        }
    }
    private void setAction(){
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
