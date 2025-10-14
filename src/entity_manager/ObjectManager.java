package entity_manager;

import object_data.WorldObject;
import object_data.ObjectKey;
import object_data.ObjectDoor;
import object_data.ObjectPortal;
import entity.Entity;            // dùng để lấy player (worldX/Y, screenX/Y)
import main.GamePanel;

import java.awt.Graphics2D;
import java.util.*;

public class ObjectManager {

    private final GamePanel gp;
    // Mỗi map giữ 1 list WorldObject
    private final Map<Integer, List<WorldObject>> objectsByMap = new HashMap<>();

    public ObjectManager(GamePanel gp) {
        this.gp = gp;
        spawnObjects();
    }

    // ==== Khởi tạo vật phẩm mẫu ====
    private void spawnObjects() {
        // Map 0
        ObjectKey key = new ObjectKey(gp, 0);
        key.worldX = 20 * gp.tileSize;
        key.worldY = 20 * gp.tileSize;
        addObject(key);

        ObjectDoor door = new ObjectDoor(gp, 0);
        door.worldX = 22 * gp.tileSize;
        door.worldY = 26 * gp.tileSize;
        addObject(door);

        ObjectPortal portal = new ObjectPortal(gp, 0);
        portal.worldX = 25 * gp.tileSize;
        portal.worldY = 23 * gp.tileSize;
        addObject(portal);

        // Map 1
        ObjectPortal portal1 = new ObjectPortal(gp, 1);
        portal1.worldX = 25 * gp.tileSize;
        portal1.worldY = 23 * gp.tileSize;
        addObject(portal1);
    }

    // ==== CRUD ====
    public void addObject(WorldObject obj) {
        objectsByMap.computeIfAbsent(obj.mapIndex, k -> new ArrayList<>()).add(obj);
    }

    public List<WorldObject> getObjects(int mapId) {
        return objectsByMap.getOrDefault(mapId, Collections.emptyList());
    }

    public void removeObject(WorldObject obj) {
        List<WorldObject> list = objectsByMap.get(obj.mapIndex);
        if (list != null) list.remove(obj);
    }

    // ==== Tick ====
    public void update() {
        for (WorldObject o : getObjects(gp.currentMap)) {
            o.update();
        }
    }

    /**
     * Vẽ object theo camera của player: world -> screen
     * Gọi từ EntityManager.draw(g2) với player hiện tại.
     */
    public void draw(Graphics2D g2, Entity player) {
        if (player == null) return;

        final int leftWorld  = player.worldX - player.screenX - gp.tileSize;             // thêm đệm 1 tile
        final int rightWorld = player.worldX + (gp.screenWidth - player.screenX) + gp.tileSize;
        final int topWorld   = player.worldY - player.screenY - gp.tileSize;
        final int botWorld   = player.worldY + (gp.screenHeight - player.screenY) + gp.tileSize;

        for (WorldObject o : getObjects(gp.currentMap)) {
            if (o.staticImage == null) continue;

            // culling thô: bỏ qua nếu object nằm ngoài camera nhiều
            int ow = (o.width  > 0 ? o.width  : gp.tileSize);
            int oh = (o.height > 0 ? o.height : gp.tileSize);
            int ox2 = o.worldX + ow;
            int oy2 = o.worldY + oh;

            if (ox2 < leftWorld || o.worldX > rightWorld || oy2 < topWorld || o.worldY > botWorld) {
                continue;
            }

            // world -> screen
            int sx = o.worldX - player.worldX + player.screenX;
            int sy = o.worldY - player.worldY + player.screenY;

            g2.drawImage(o.staticImage, sx, sy, null);
            //  debug hitbox:
            g2.setColor(java.awt.Color.YELLOW);
            g2.drawRect(sx + o.solidArea.x, sy + o.solidArea.y, o.solidArea.width, o.solidArea.height);
        }
    }
}
