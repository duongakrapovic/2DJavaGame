/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class ObjectBoots extends Entity {
    public ObjectBoots(GamePanel gp){
        super(gp);
        name = "boots";
        width = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;
        staticImage = setup("/object/boots" , width, height);
        
        collision = false;
        
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}