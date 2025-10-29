package entity_manager;

import object_data.*;
import entity.Entity;            // lấy player để tính world->screen
import main.GamePanel;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.*;

public class ObjectManager {

    private final GamePanel gp;
    // Mỗi map giữ 1 list WorldObject
    private final Map<Integer, List<WorldObject>> objectsByMap = new HashMap<>();

    public ObjectManager(GamePanel gp) {
        this.gp = gp;
        spawnObjects();
    }

    private void spawnObjects() {
        // Map 0
        ObjectKey key = new ObjectKey(gp, 0);
        key.worldX = 20 * gp.tileSize;
        key.worldY = 20 * gp.tileSize;
        addObject(key);
        
        Shop shop = new Shop(gp, 0);
        shop.worldX = 46 * gp.tileSize ;
        shop.worldY = 15 * gp.tileSize;
        addObject(shop);
        
        ObjectDoor door = new ObjectDoor(gp, 0);
        door.worldX = 48 * gp.tileSize - 23;
        door.worldY = 18 * gp.tileSize;
        addObject(door);
             
        ObjectPortal portal = new ObjectPortal(gp, 0);
        portal.worldX = 47 * gp.tileSize + 12;
        portal.worldY = 47 * gp.tileSize + 12;
        addObject(portal);

        // Map 1
        ObjectPortal portal1 = new ObjectPortal(gp, 1);
        portal1.worldX = 47 * gp.tileSize;
        portal1.worldY = 47 * gp.tileSize;
        addObject(portal1);
        
        Shop shop1 = new Shop(gp, 0);
        shop1.worldX = 46 * gp.tileSize ;
        shop1.worldY = 15 * gp.tileSize;
        addObject(shop1);
//        
//        ObjectDoor door1 = new ObjectDoor(gp, 1);
//        door1.worldX = 48 * gp.tileSize - 23;
//        door1.worldY = 18 * gp.tileSize;
//        addObject(door1);
        
        // map 2 
        
        // map shop = map 3 
        ObjectDoor door3 = new ObjectDoor(gp, 3);
        door3.worldX = 15 * gp.tileSize + 22 ;
        door3.worldY = 23 * gp.tileSize;
        addObject(door3);
    }

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
        update(gp.currentMap);
    }

    public void update(int mapId) {
        for (WorldObject o : getObjects(mapId)) o.update();
    }


    public void draw(Graphics2D g2, Entity player) {
        draw(g2, gp.currentMap, player);
    }

    public void draw(Graphics2D g2, int mapId, Entity player) {
        if (player == null) return;

        final int leftWorld  = player.worldX - player.screenX - gp.tileSize;
        final int rightWorld = player.worldX + (gp.screenWidth - player.screenX) + gp.tileSize;
        final int topWorld   = player.worldY - player.screenY - gp.tileSize;
        final int botWorld   = player.worldY + (gp.screenHeight - player.screenY) + gp.tileSize;

        for (WorldObject o : getObjects(mapId)) {
            // culling
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

            BufferedImage img = null;
            try {
                img = o.getRenderImage();
            } catch (NoSuchMethodError | Exception ignored) {
            }
            if (img == null) img = o.staticImage;

            if (img != null) g2.drawImage(img, sx, sy, null);

            // debug hitbox
            /*
            g2.setColor(java.awt.Color.YELLOW);
            g2.drawRect(sx + o.solidArea.x, sy + o.solidArea.y, o.solidArea.width, o.solidArea.height);
            */
        }
    }
}
