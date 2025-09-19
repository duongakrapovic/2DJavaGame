/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class ObjectChest extends Entity{
    public ObjectChest( GamePanel gp){
        super(gp);
        name = "chest";
        width = gp.tileSize;
        height = gp.tileSize;
        staticImage = setup("/object/chest" , width, height);
        
        collision = false;
        
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
