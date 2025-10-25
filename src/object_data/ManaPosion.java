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

        name   = "boots";
        width  = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;

        staticImage = setup("/object/boots", width, height);
        collision = false;

        solidArea = new Rectangle(2, 2, width - 4, height - 4);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
