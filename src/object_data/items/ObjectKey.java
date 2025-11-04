package object_data.items;

import java.awt.Rectangle;
import main.GamePanel;
import object_data.WorldObject;

public class ObjectKey extends WorldObject {

    public ObjectKey(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "key";
        width  = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;

        staticImage = setup("/object/key", width, height);
        collision = false;

        solidArea = new Rectangle(-gp.tileSize/4, -gp.tileSize/4,
                width + gp.tileSize/2, height + gp.tileSize/2);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
