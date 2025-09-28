package entity_manager;

import entity.Entity;
import object_data.ObjectKey;
import object_data.ObjectDoor;
import object_data.ObjectPortal;
import main.GamePanel;

import java.util.*;

public class ObjectManager {
    private final GamePanel gp;
    private final Map<Integer, List<Entity>> objectsByMap = new HashMap<>();

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

    public void addObject(Entity obj) {
        objectsByMap.computeIfAbsent(obj.mapIndex, k -> new ArrayList<>()).add(obj);
    }

    public List<Entity> getObjects(int mapId) {
        return objectsByMap.getOrDefault(mapId, Collections.emptyList());
    }

    public void removeObject(Entity obj) {
        List<Entity> list = objectsByMap.get(obj.mapIndex);
        if(list != null) list.remove(obj);
    }

    public void update() {
        for(Entity o : getObjects(gp.currentMap)) {
            o.update();
        }
    }

    public void draw(java.awt.Graphics2D g2) {
        for(Entity o : getObjects(gp.currentMap)) {
            o.draw(g2);
        }
    }
}
