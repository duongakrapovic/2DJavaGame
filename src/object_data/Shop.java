/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object_data;

import java.awt.Rectangle;
import main.GamePanel;

public class Shop extends WorldObject {

    public Shop(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "shop";
        width  = gp.tileSize * 4;
        height = gp.tileSize * 4;

        staticImage = setup("/object/shop", width, height);
        collision = false;

        solidArea = new Rectangle(0, 0, width, height);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
