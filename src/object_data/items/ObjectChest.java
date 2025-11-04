package object_data.items;

import java.awt.Rectangle;
import main.GamePanel;
import object_data.WorldObject;

public class ObjectChest extends WorldObject {

    public boolean opened = false;

    public ObjectChest(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "chest";
        width  = gp.tileSize;
        height = gp.tileSize;

        staticImage = setup("/object/chest", width, height);
        collision = true;

        solidArea = new Rectangle(2, 4, width - 4, height - 8);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void open() {
        if (opened) return;
        opened = true;
        // staticImage = setup("/object/chest_open", width, height);
    }
}
