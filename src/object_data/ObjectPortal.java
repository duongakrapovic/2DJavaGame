// src/object_data/ObjectPortal.java
package object_data;

import main.GamePanel;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import object_data.WorldObject;

public class ObjectPortal extends WorldObject {

    // 2 frame animation
    private BufferedImage f1, f2;
    private int animCounter = 0;
    private int frameDuration = 10;

    public int targetMap;
    public int targetWorldX;
    public int targetWorldY;

    public ObjectPortal(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;

        name   = "portal";
        width  = gp.tileSize * 3/2;
        height = gp.tileSize * 3/2;

        f1 = setup("/object/portal1", width, height);
        f2 = setup("/object/portal2", width, height);

        collision = false;

        solidArea = new Rectangle(2, 2, width - 4, height - 4);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

    @Override
    public void update() {
        animCounter++;
    }

    @Override
    public BufferedImage getRenderImage() {
        int idx = (animCounter / frameDuration) % 2;
        if (idx == 0) return (f1 != null ? f1 : f2);
        return (f2 != null ? f2 : f1);
    }
}
