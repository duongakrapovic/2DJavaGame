/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class ObjectKey extends Entity{
    public ObjectKey( GamePanel gp){
        super(gp);
        name = "key";   
        width = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;
        staticImage = setup("/object/key" , width, height);
        
        collision = false;
        
        solidArea = new Rectangle(-gp.tileSize/4, -gp.tileSize/4,
                        width + gp.tileSize/2, height + gp.tileSize/2);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
