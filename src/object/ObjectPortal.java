/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import java.awt.Rectangle;

import entity.Entity;
import main.GamePanel;

public class ObjectPortal extends Entity{
    public ObjectPortal( GamePanel gp){
        super(gp);
        name = "portal";
        width = gp.tileSize * 3/2 ;
        height = gp.tileSize * 3/2;
        image = setup("/object/portal" , width, height);
        
        collision = false;
        
        solidArea = new Rectangle(-gp.tileSize/25, -gp.tileSize/25, width, height);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
