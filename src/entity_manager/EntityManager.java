package entity_manager;

import combat.CombatSystem;
import entity.Entity;
import monster_data.Monster;
import player_manager.Player;
import input_manager.InputController;
import object_data.WorldObject;           // << thêm
import main.GamePanel;
import object_data.WorldObject;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    private final GamePanel gp;
    private final Player player;
    private final MonsterManager mM;
    private final NPCManager npcM;
    private final ObjectManager oM;

    public EntityManager(GamePanel gp, InputController input) {
        this.gp = gp;                    // << lưu lại gp để dùng nếu cần
        this.player = new Player(gp, input);
        this.mM = new MonsterManager(gp);
        this.npcM = new NPCManager(gp);
        this.oM = new ObjectManager(gp);
    }

    public Player getPlayer() { return player; }
    public List<Entity> getMonsters(int map) { return mM.getMonsters(map); }
    public List<Entity> getNPCs(int map) { return npcM.getNPCs(map); }
    public List<WorldObject> getObjects(int map) { return oM.getObjects(map); } // << đổi kiểu
    public List<WorldObject> getWorldObjects(int map) {
        return oM.getObjects(map);
    }


    public void update(int currentMap) {
        // tick entities
        player.update();
        // WorldObject do ObjectManager quản lý
        oM.update(); // dùng currentMap bên trong ObjectManager

        for (Entity m : mM.getMonsters(currentMap)) m.update();
        for (Entity n : npcM.getNPCs(currentMap))  n.update();

        // combat/collision
        CombatSystem.resolvePlayerHits(player, mM.getMonsters(currentMap));
        CombatSystem.resolveMonsterContacts(player, mM.getMonsters(currentMap));

        // cleanup
        removeDeadMonsters(currentMap);
    }

    public void draw(Graphics2D g2, int currentMap) {
        // 1) Vẽ WorldObject theo camera player
        oM.draw(g2, player);

        // 2) Gom và vẽ Entity (player, npc, monster)
        List<Entity> all = new ArrayList<>();
        all.addAll(mM.getMonsters(currentMap));
        all.addAll(npcM.getNPCs(currentMap));
        all.add(player);

        all.sort((a, b) -> Integer.compare(a.worldY, b.worldY));
        for (Entity e : all) e.draw(g2);
    }

    // remove dead monster
    public void removeDeadMonsters(int currentMap) {
        mM.getMonsters(currentMap).removeIf(e -> (e instanceof Monster m) && m.isDead());
    }
}
