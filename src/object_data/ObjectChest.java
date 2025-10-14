package object_data;

import java.awt.Rectangle;
import main.GamePanel;

/** Rương — có thể mở lấy đồ. */
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

    /** Gọi khi mở rương (ví dụ từ Interact). */
    public void open() {
        if (opened) return;
        opened = true;
        // Nếu bạn có sprite rương mở:
        // staticImage = setup("/object/chest_open", width, height);
        // collision = false; // tuỳ game design
    }
}
