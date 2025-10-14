// src/object_data/ObjectPortal.java
package object_data;

import main.GamePanel;

import java.awt.Rectangle;

/** Cổng dịch chuyển – đồ vật (không combat). */
public class ObjectPortal extends WorldObject {

    // Tuỳ chọn: thông tin dịch chuyển
    public int targetMap = 0;
    public int targetWorldX;
    public int targetWorldY;

    public ObjectPortal(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "portal";
        width  = gp.tileSize;     // có thể chỉnh 2*tileSize nếu sprite lớn
        height = gp.tileSize;

        // Đổi đường dẫn này cho đúng sprite của bạn
        staticImage = setup("/object/portal", width, height);

        collision = false; // thường cho phép đi vào

        // hitbox để player “bước vào” là hoạt động
        solidArea = new Rectangle(2, 2, width - 4, height - 4);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // ví dụ thiết lập mặc định
        targetMap = mapIndex; // ở nguyên map nếu bạn chưa dùng dịch chuyển map
        targetWorldX = 10 * gp.tileSize;
        targetWorldY = 10 * gp.tileSize;
    }
}
