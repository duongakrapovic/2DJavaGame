/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;
import main.GamePanel;

public class ManaPosion extends WorldObject {

    public ManaPosion(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "manaposion";
        width  = gp.tileSize;
        height = gp.tileSize;

        staticImage = setup("/object/manaposion", width, height);
        collision = false;
        
        
        int t = gp.tileSize / 2;
        solidArea = new Rectangle(-t / 2, -t /2, width + t, height + t);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
