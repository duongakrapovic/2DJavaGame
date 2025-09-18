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

public class Entity {
    
    // default setting 
    public int worldX, worldY;
    public int width, height;
    public final int screenX;
    public final int screenY;
    
    // system
    UtilityTool uTool = new UtilityTool();
    
    // set for animaion
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    public String direction;
    public int spriteCounter = 0;
    public int spriteNum = 1;
    
    // set 1 part of player tile is solit 
    public Rectangle solidArea;
    public boolean collisionXOn = false;
    public boolean collisionYOn = false;
    public boolean collisionOn = false;
    
    // set for colision area of entity
    public int solidAreaDefaultX, solidAreaDefaultY;
    
    //OBJECT
    public BufferedImage image;
    public String name;
    public boolean collision  = false;
    
    //set for utility
    public int defaultSpeed, actualSpeed, buffSpeed;
    
    public Entity(GamePanel gp) {
        screenX = gp.screenWidth/2 - (gp.tileSize/2);
        screenY = gp.screenHeight/2 - (gp.tileSize/2);
        UtilityTool uTool = new UtilityTool();
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
    
    public void drawObject(Graphics2D g2, GamePanel gp){
        int screenX = worldX - gp.player.worldX + gp.player.screenX;
        int screenY = worldY - gp.player.worldY + gp.player.screenY;
        
        if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
           worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
           worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
           worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){
           // only draw if object in the right area
           // i dont know how to apply culling methotw same as the movement 
            if(image != null){
                g2.drawImage(image, screenX , screenY , width, height , null); 
                // Debug: vẽ viền solidArea
                g2.setColor(Color.RED);
                int solidScreenX = screenX + solidArea.x;
                int solidScreenY = screenY + solidArea.y;
                g2.drawRect(solidScreenX, solidScreenY, solidArea.width, solidArea.height);
           }
           
        }
    }
}
