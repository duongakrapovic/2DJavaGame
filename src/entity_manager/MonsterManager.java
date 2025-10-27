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

    private void spawnAll() {
        spawnMap0();
        spawnMap1();
        spawnMap2();
        spawnMap3();
    }

    private void spawnMap0() {
        // no monsters
    }

    private void spawnMap1() {
        int t = gp.tileSize;
        addMonster(new RedSlimeMonster(gp, 1), 10*t, 12*t);
        addMonster(new RedSlimeMonster(gp, 1), 15*t, 9*t);
        addMonster(new BatMonster(gp, 1),       20*t, 8*t);
        addMonster(new SkeletonLord(gp, 1),   30*t, 12*t);
    }

    private void spawnMap2() {
        int t = gp.tileSize;
        addMonster(new OrcMonster(gp, 2), 18 * t, 10 * t);
    }

    private void spawnMap3(){
        
    }
    
    private void addMonster(Entity m, int wx, int wy) {
        m.worldX = wx;
        m.worldY = wy;
        monstersByMap.computeIfAbsent(m.mapIndex, k -> new ArrayList<>()).add(m);
    }

    public List<Entity> getMonsters(int mapId) {
        return monstersByMap.getOrDefault(mapId, Collections.emptyList());
    }

    public void update(int mapId) {
        for (Entity m : getMonsters(mapId)) {
            m.update();
        }
    }

    public void draw(java.awt.Graphics2D g2, int mapId) {
        for (Entity m : getMonsters(mapId)) {
            m.draw(g2);
        }
    }
}
