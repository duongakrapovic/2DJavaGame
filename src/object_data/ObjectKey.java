/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class ObjectKey extends Entity{
    public ObjectKey( GamePanel gp,int mapIndex){
        super(gp);
        this.gp = gp;
        this.mapIndex = mapIndex;
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
