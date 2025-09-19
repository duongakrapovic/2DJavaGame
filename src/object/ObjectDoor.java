/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class ObjectDoor extends Entity{
    public ObjectDoor( GamePanel gp){
        super(gp);
        name = "door";
        width = gp.tileSize;
        height = gp.tileSize;
        staticImage = setup("/object/door" , width, height);
        
        collision = true;
        
        solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    } 
}
