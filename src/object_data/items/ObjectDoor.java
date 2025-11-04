package object_data.items;

import java.awt.Rectangle;
import main.GamePanel;
import object_data.WorldObject;

public class ObjectDoor extends WorldObject {

    public ObjectDoor(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "door";
        width  = gp.tileSize;
        height = gp.tileSize;

        staticImage = setup("/object/door", width, height);
        collision = false;
        solidArea = new Rectangle(2, 2, width - 4, height - 4);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
