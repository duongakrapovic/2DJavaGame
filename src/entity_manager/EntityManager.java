/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity_manager;

import entity.Entity;
import entity.Player;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;

public class EntityManager {

    private Player player;
    private MonsterManager mM;
    private NPCManager npcM;
    private ObjectManager oM;

    public EntityManager(Player player, GamePanel gp) {
        this.player = player;
        this.mM = new MonsterManager(gp);
        this.npcM = new NPCManager(gp);
        this.oM = new ObjectManager(gp);
    }

    public Player getPlayer() { return player; }
    public List<Entity> getMonsters(int map) { return mM.getMonsters(map); }
    public List<Entity> getNPCs(int map) { return npcM.getNPCs(map); }
    public List<Entity> getObjects(int map) { return oM.getObjects(map); }
    
    public void update(int currentMap) {
        player.update();
        for (Entity o : oM.getObjects(currentMap)) 
            o.update();
        for (Entity m : mM.getMonsters(currentMap))
            m.update();
        for (Entity n : npcM.getNPCs(currentMap)) 
            n.update();
        
    }

    public void draw(Graphics2D g2, int currentMap) { 
        List<Entity> all = new ArrayList<>();
        
        all.addAll(mM.getMonsters(currentMap));
        
        all.addAll(npcM.getNPCs(currentMap));
        
        all.add(player);

        // sort theo worldY
        all.sort((a, b) -> Integer.compare(a.worldY, b.worldY));
        
        all.addAll(0, oM.getObjects(currentMap));
        for (Entity e : all) e.draw(g2);
    }
}


