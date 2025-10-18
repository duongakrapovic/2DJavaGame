package object_data;

import java.awt.Rectangle;
import main.GamePanel;

public class ObjectBoots extends WorldObject {

    public ObjectBoots(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "boots";
        width  = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;

        staticImage = setup("/object/boots", width, height);
        collision = false;

        solidArea = new Rectangle(-gp.tileSize/4, -gp.tileSize/4,
                width + gp.tileSize/2, height + gp.tileSize/2);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
