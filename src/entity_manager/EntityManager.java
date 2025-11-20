package entity_manager;

import combat.CombatSystem;
import entity.Entity;
import monster_data.Monster;
import player_manager.Player;
import input_manager.InputController;
import object_data.WorldObject;           // << thêm
import main.GamePanel;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {

    private final GamePanel gp;
    private final Player player;
    private final MonsterManager mM;
    private final NPCManager npcM;

    public EntityManager(GamePanel gp, InputController input) {
        this.gp = gp;
        this.player = new Player(gp, input);
        this.mM = new MonsterManager(gp);
        this.npcM = new NPCManager(gp);
    }

    public Player getPlayer() { return player; }
    public List<Entity> getMonsters(int map) { return mM.getMonsters(map); }
    public List<Entity> getNPCs(int map) { return npcM.getNPCs(map); }


    public List<Entity> getMonsters() {
        return mM.getMonsters(gp.currentMap);
    }

    public void update(int currentMap) {
        // 1) Player + object
        player.update();

        // 2) Update + xử lý chết + respawn quái thông qua MonsterManager
        //    => chỗ này sẽ gọi e.update() cho từng quái, check isDead, registerDeath, respawn...
        mM.update(currentMap, player.worldX, player.worldY);

        // 3) Lấy lại danh sách quái sống sau khi MonsterManager xử lý
        List<Entity> monsters = mM.getMonsters(currentMap);

        // 4) Update NPC
        for (Entity n : npcM.getNPCs(currentMap)) {
            n.update();
        }

        // 5) Combat: player tấn công quái
        CombatSystem.resolvePlayerHits(player, monsters);

        //    Quái tấn công player
        for (Entity e : monsters) {
            if (e instanceof Monster m) {
                CombatSystem.resolveMonsterHitAgainstPlayer(m, player);
            }
        }

        // ❌ KHÔNG còn removeDeadMonsters() ở đây nữa
    }


    public void draw(Graphics2D g2, int currentMap) {
        // World Object

        // Entity
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
