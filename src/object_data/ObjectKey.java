package object_data;

import java.awt.Rectangle;
import entity.Entity;
import main.GamePanel;

/** Represents a key item that can be picked up. */
public class ObjectKey extends Entity {

    public ObjectKey(GamePanel gp, int mapIndex) {
        super(gp);                 // khởi tạo gp ở Entity (final) — KHÔNG gán lại
        this.mapIndex = mapIndex;

        name = "key";

        // kích thước theo tile
        width  = gp.tileSize * 3 / 5;
        height = gp.tileSize * 3 / 5;

        // ảnh tĩnh
        staticImage = setup("/object/key", width, height);

        // nhặt được, không chặn va chạm thân
        collision = false;

        // vùng “nhặt” rộng hơn 1 chút
        solidArea = new Rectangle(
                -gp.tileSize / 4,
                -gp.tileSize / 4,
                width + gp.tileSize / 2,
                height + gp.tileSize / 2
        );
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
