// src/object_data/ObjectPortal.java
package object_data;

import main.GamePanel;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class ObjectPortal extends WorldObject {

    // 2 frame animation
    private BufferedImage f1, f2;
    private int animCounter = 0;
    private int frameDuration = 10; // số frame mỗi ảnh

    // Thông tin dịch chuyển (bạn đang dùng ở constructor)
    public int targetMap;
    public int targetWorldX;
    public int targetWorldY;

    public ObjectPortal(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "portal";
        width  = gp.tileSize * 3/2;
        height = gp.tileSize * 3/2;

        // nạp 2 ảnh
        f1 = setup("/object/portal1", width, height);
        f2 = setup("/object/portal2", width, height);
        staticImage = (f1 != null ? f1 : f2); // fallback

        collision = false;

        solidArea = new Rectangle(2, 2, width - 4, height - 4);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        // mặc định dịch chuyển (tùy bạn đổi)
        targetMap = mapIndex;
        targetWorldX = 10 * gp.tileSize;
        targetWorldY = 10 * gp.tileSize;
    }

    @Override
    public void update() {
        animCounter++;
    }

    @Override
    public BufferedImage getRenderImage() {
        // chọn frame theo animCounter
        int idx = (animCounter / frameDuration) % 2;
        if (idx == 0) return (f1 != null ? f1 : f2);
        return (f2 != null ? f2 : f1);
    }
}
