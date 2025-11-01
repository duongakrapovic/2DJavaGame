package entity_manager;

import entity.Entity;
import main.GamePanel;
import npc_data.*;

import java.util.*;

public class NPCManager {
    private final GamePanel gp;
    private final Map<Integer, List<Entity>> npcsByMap = new HashMap<>();

    public NPCManager(GamePanel gp) {
        this.gp = gp;
        spawnNPCs();
    }

    private void spawnNPCs() {
        int t = gp.tileSize;

        // Map 0
        NPC_Oldman oldman0 = new NPC_Oldman(gp, 0);
        oldman0.worldX = 23 * t;
        oldman0.worldY = 30 * t;
        addNPC(oldman0);

        // Map 1
        NPC_Frog frog1 = new NPC_Frog(gp, 1);
        frog1.worldX = 20 * t;
        frog1.worldY = 25 * t;
        addNPC(frog1);

        // Map 3 (shop)
    }

    public void addNPC(Entity npc) {
        npcsByMap.computeIfAbsent(npc.mapIndex, k -> new ArrayList<>()).add(npc);
    }

    public List<Entity> getNPCs(int mapId) {
        return npcsByMap.getOrDefault(mapId, Collections.emptyList());
    }

    public void update(int mapId) {
        for (Entity npc : getNPCs(mapId)) {
            npc.update();
        }
    }

    public void draw(java.awt.Graphics2D g2, int mapId) {
        for (Entity npc : getNPCs(mapId)) {
            npc.draw(g2);
        }
    }
}
