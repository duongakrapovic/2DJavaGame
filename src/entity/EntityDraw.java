/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import main.GamePanel;

public class EntityDraw {
    private final GamePanel gp;
    public EntityDraw(GamePanel gp){
        this.gp = gp;
    }
    public void draw(Graphics2D g2, Entity e){
        int screenX = e.worldX - gp.em.getPlayer().worldX + gp.em.getPlayer().screenX;
        int screenY = e.worldY - gp.em.getPlayer().worldY + gp.em.getPlayer().screenY;
        
        if(e.worldX + gp.tileSize > gp.em.getPlayer().worldX - gp.em.getPlayer().screenX &&
           e.worldX - gp.tileSize < gp.em.getPlayer().worldX + gp.em.getPlayer().screenX &&
           e.worldY + gp.tileSize > gp.em.getPlayer().worldY - gp.em.getPlayer().screenY &&
           e.worldY - gp.tileSize < gp.em.getPlayer().worldY + gp.em.getPlayer().screenY){
            // only draw if object in the right area
            // i dont know how to apply culling methot same as the movement 
            
            BufferedImage image = null;
            if(e.animationON == true){
                switch(e.direction){
                case "up":
                    if(e.spriteNum == 1) image = e.up1;
                    if(e.spriteNum == 2) image = e.up2;   
                    break;
                case "down":
                    if(e.spriteNum == 1) image = e.down1;
                    if(e.spriteNum == 2) image = e.down2;
                    break;
                case "left":
                    if(e.spriteNum == 1) image = e.left1;
                    if(e.spriteNum == 2) image = e.left2;
                    break;
                case "right":
                    if(e.spriteNum == 1) image = e.right1;
                    if(e.spriteNum == 2) image = e.right2;
                    break;
                }
            }
            else{
                image = e.staticImage;
            }
  
            g2.drawImage(image, screenX , screenY , e.width, e.height , null); 
            // Debug: rec solidArea
            g2.setColor(Color.RED);
            int solidScreenX = screenX + e.solidArea.x;
            int solidScreenY = screenY + e.solidArea.y;
            g2.drawRect(solidScreenX, solidScreenY, e.solidArea.width, e.solidArea.height);
        }
    }
}
