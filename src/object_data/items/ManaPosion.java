/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data.items;

import java.awt.Rectangle;
import main.GamePanel;
import object_data.WorldObject;

public class ManaPosion extends WorldObject {

    public ManaPosion(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;
        name   = "manaposion";
        width  = gp.tileSize;
        height = gp.tileSize;

        staticImage = setup("/object/manaposion", width, height);
        collision = false;
        
        
        int t = gp.tileSize / 8;
        solidArea = new Rectangle(-t / 2, -t /2, width + t, height + t);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
