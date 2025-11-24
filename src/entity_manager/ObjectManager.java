package entity_manager;

import object_data.items.*;
import object_data.*;
import entity.Entity;            // lấy player để tính world->screen
import main.GamePanel;
import object_data.weapons.*;

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
        spawnMap0();
        spawnMap1();
        spawnMap2();
        spawnMap3();
    }
    private void spawnMap0() {
        int t = gp.tileSize;
        addObject(new Shop(gp, 0),46 * t, 15 * t);
        addObject(new ObjectDoor(gp, 0),48 * t - 23, 18 * t);
        addObject(new ObjectPortal(gp, 0),47 * t + 12, 47 * t + 12);
    }
    private void spawnMap1() {
        int t = gp.tileSize;
        addObject(new ObjectPortal(gp, 1),47 * t + 12, 47 * t + 12);
    }
    private void spawnMap2() {
        // no 
    }
    private void spawnMap3() {
        int t = gp.tileSize;
        addObject(new ObjectDoor(gp, 3), 15 * t + 22 , 23 * t);
        addObject(new ObjectKey(gp, 3 ), 10 * t, 18 * t + 5);
        addObject(new Axe(gp, 3), 16 * t , 18 * t);
    }
    public void addObject(WorldObject obj, int wx, int wy) {
        obj.worldX = wx;
        obj.worldY = wy;
        objectsByMap.computeIfAbsent(obj.mapIndex, k -> new ArrayList<>()).add(obj);
    }

    public List<WorldObject> getObjects(int mapId) {
        return objectsByMap.getOrDefault(mapId, Collections.emptyList());
    }

    // ==== Tick ====
    public void update() {
        update(gp.currentMap);
    }
    public void update(int mapId) {
        for (WorldObject o : getObjects(mapId))
            o.update();
    }

    public void draw(Graphics2D g2, Entity player) {
        draw(g2, gp.currentMap, player);
    }

    public void draw(Graphics2D g2, int mapId, Entity player) {
        if (player == null) return;

        List<WorldObject> list = getObjects(mapId);

        final int leftWorld  = player.worldX - player.screenX - gp.tileSize;
        final int rightWorld = player.worldX + (gp.screenWidth - player.screenX) + gp.tileSize;
        final int topWorld   = player.worldY - player.screenY - gp.tileSize;
        final int botWorld   = player.worldY + (gp.screenHeight - player.screenY) + gp.tileSize;

        for (WorldObject o : list) {


            int ow = (o.width  > 0 ? o.width  : gp.tileSize);
            int oh = (o.height > 0 ? o.height : gp.tileSize);
            int ox2 = o.worldX + ow;
            int oy2 = o.worldY + oh;

            if (ox2 < leftWorld || o.worldX > rightWorld || oy2 < topWorld || o.worldY > botWorld) {
                continue;
            }

            int sx = o.worldX - player.worldX + player.screenX;
            int sy = o.worldY - player.worldY + player.screenY;

            BufferedImage img = null;
            try {
                img = o.getRenderImage();
            } catch (NoSuchMethodError | Exception ignored) {}

            if (img == null) img = o.staticImage;

            if (img != null) g2.drawImage(img, sx, sy, null);

//            g2.setColor(java.awt.Color.RED);
//            g2.drawRect(sx + o.solidArea.x, sy + o.solidArea.y,
//                    o.solidArea.width, o.solidArea.height);
        }
    }
    public void reloadMapObjects(int mapId) {
        objectsByMap.clear();
        spawnObjects();
    }
    // === Drop từ quái: HealthPosion ===
    public void spawnHealthPosion(int mapIndex, int wx, int wy) {
        HealthPosion potion = new HealthPosion(gp, mapIndex);
        addObject(potion, wx, wy);

        int size = getObjects(mapIndex).size();
        System.out.println("[DROP] HealthPosion spawn tại map " + mapIndex +
                " (" + wx + ", " + wy + ")  -> list size = " + size);
    }
    public void spawnSword(int mapIndex, int wx, int wy) {
        Sword sword = new Sword(gp, mapIndex);
        addObject(sword, wx, wy);

        int size = getObjects(mapIndex).size();
        System.out.println("[DROP] Sword spawn tại map " + mapIndex +
                " (" + wx + ", " + wy + ")  -> list size = " + size);
    }
}
