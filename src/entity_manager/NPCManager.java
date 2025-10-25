package entity_manager;

import entity.Entity;
import main.GamePanel;

import npc_data.NPC_Oldman;
import npc_data.NPC_Frog;

import java.util.*;

public class NPCManager {
    private final GamePanel gp;
    private final Map<Integer, List<Entity>> npcsByMap = new HashMap<>();

    public NPCManager(GamePanel gp) {
        this.gp = gp;
        spawnNPCs();
    }

    private void spawnNPCs() {
        // Map 1
        NPC_Oldman oldman1 = new NPC_Oldman(gp, 0);
        oldman1.worldX = 23 * gp.tileSize;
        oldman1.worldY = 30 * gp.tileSize;
        addNPC(oldman1);

        NPC_Frog frog1 = new NPC_Frog(gp, 0);
        frog1.worldX = 30 * gp.tileSize;
        frog1.worldY = 30 * gp.tileSize;
        addNPC(frog1);

        // Map 2
        NPC_Oldman oldman2 = new NPC_Oldman(gp, 1);
        oldman2.worldX = 20 * gp.tileSize;
        oldman2.worldY = 25 * gp.tileSize;
        addNPC(oldman2);

        NPC_Frog frog2 = new NPC_Frog(gp, 1);
        frog2.worldX = 28 * gp.tileSize;
        frog2.worldY = 28 * gp.tileSize;
        addNPC(frog2);
    }

    public void addNPC(Entity npc) {
        npcsByMap.computeIfAbsent(npc.mapIndex, k -> new ArrayList<>()).add(npc);
    }

    public List<Entity> getNPCs(int mapId) {
        return npcsByMap.getOrDefault(mapId, Collections.emptyList());
    }

    public void update() {
        for(Entity npc : getNPCs(gp.currentMap)) {
            npc.update();
        }
    }

    public void draw(java.awt.Graphics2D g2) {
        for(Entity npc : getNPCs(gp.currentMap)) {
            npc.draw(g2);
        }
    }
}
