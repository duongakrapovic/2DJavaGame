package object_data;

import java.awt.Rectangle;
import main.GamePanel;

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
