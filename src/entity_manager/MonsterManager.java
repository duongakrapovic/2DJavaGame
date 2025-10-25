package entity_manager;

import entity.Entity;
import main.GamePanel;
import monster_data.*;

import java.util.*;

public class MonsterManager {
    private final GamePanel gp;
    private final Map<Integer, List<Entity>> monstersByMap = new HashMap<>();

    public MonsterManager(GamePanel gp) {
        this.gp = gp;
        spawnAll();
    }

    /** Gọi 1 lần khi khởi động */
    private void spawnAll() {
        spawnMap0();
        spawnMap1();
        spawnMap2();
        spawnMap3();
    }

    // === HOME ===
    private void spawnMap0() {
        // Map 0 là nhà → không có quái
    }

    // === FLOOR 1 ===
    private void spawnMap1() {
        int t = gp.tileSize;
        addMonster(new RedSlimeMonster(gp, 1), 10*t, 12*t);
        addMonster(new RedSlimeMonster(gp, 1), 15*t, 9*t);
        addMonster(new BatMonster(gp, 1),       20*t, 8*t);
    }

    // === FLOOR 2 ===
    private void spawnMap2() {
        int t = gp.tileSize;
        addMonster(new OrcMonster(gp, 2), 18*t, 10*t);
        addMonster(new BatMonster(gp, 2), 8*t,  6*t);
        addMonster(new BatMonster(gp, 2), 14*t, 14*t);
    }

    // === FLOOR 3 (BOSS) ===
    private void spawnMap3() {
        int t = gp.tileSize;
        addMonster(new RedSlimeMonster(gp, 3), 16*t, 8*t); // boss duy nhất tầng 3
    }

    // === HÀM HỖ TRỢ CHUNG ===
    private void addMonster(Entity m, int wx, int wy) {
        m.worldX = wx;
        m.worldY = wy;
        monstersByMap.computeIfAbsent(m.mapIndex, k -> new ArrayList<>()).add(m);
    }

    public List<Entity> getMonsters(int mapId) {
        return monstersByMap.getOrDefault(mapId, Collections.emptyList());
    }

    public void update() {
        for (Entity m : getMonsters(gp.currentMap)) {
            m.update();
        }
    }

    public void draw(java.awt.Graphics2D g2) {
        for (Entity m : getMonsters(gp.currentMap)) {
            m.draw(g2);
        }
    }
}
