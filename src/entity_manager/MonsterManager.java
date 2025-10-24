package entity_manager;

import entity.Entity;
import monster_data.SlimeMonster;
import main.GamePanel;
import monster_data.OrcMonster;
import java.util.*;

public class MonsterManager {
    private final GamePanel gp;
    private final Map<Integer, List<Entity>> monstersByMap = new HashMap<>();

    public MonsterManager(GamePanel gp) {
        this.gp = gp;
        spawnMonsters();
    }

    private void spawnMonsters() {
        // Map 0

        SlimeMonster slime1 = new SlimeMonster(gp, 0);
        slime1.worldX = 20 * gp.tileSize;
        slime1.worldY = 23 * gp.tileSize;
        addMonster(slime1);

        OrcMonster orc1 = new OrcMonster(gp, 0);
        orc1.worldX = 22 * gp.tileSize;
        orc1.worldY = 25 * gp.tileSize;
        addMonster(orc1);

        // Map 1
        SlimeMonster slime2 = new SlimeMonster(gp, 1);
        slime2.worldX = 25 * gp.tileSize;
        slime2.worldY = 28 * gp.tileSize;
        addMonster(slime2);

        OrcMonster orc2 = new OrcMonster(gp, 1);
        orc2.worldX = 28 * gp.tileSize;
        orc2.worldY = 30 * gp.tileSize;
        addMonster(orc2);
    }

    public void addMonster(Entity monster) {
        monstersByMap.computeIfAbsent(monster.mapIndex, k -> new ArrayList<>()).add(monster);
    }

    public List<Entity> getMonsters(int mapId) {
        return monstersByMap.getOrDefault(mapId, Collections.emptyList());
    }

    public void update() {
        for(Entity m : getMonsters(gp.currentMap)) {
            m.update();
        }
    }

    public void draw(java.awt.Graphics2D g2) {
        for(Entity m : getMonsters(gp.currentMap)) {
            m.draw(g2);
        }
    }
}
