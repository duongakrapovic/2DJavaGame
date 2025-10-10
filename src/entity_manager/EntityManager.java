/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity_manager;

import combat.CollisionUtil;
import combat.CombatSystem;
import combat.DamageProcessor;
import entity.Entity;
import monster_data.Monster;
import player_manager.Player;
import input_manager.InputController;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;

public class EntityManager {

    private final Player player;
    private final MonsterManager mM;
    private final NPCManager npcM;
    private final ObjectManager oM;

    public EntityManager(GamePanel gp, InputController input) {
        this.player = new Player(gp, input);
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
        for (Entity o : oM.getObjects(currentMap)) o.update();
        for (Entity m : mM.getMonsters(currentMap)) m.update();
        for (Entity n : npcM.getNPCs(currentMap))  n.update();

        processPlayerHits(currentMap);     // Player đánh trúng quái -> apply damage
        processMonsterContacts(currentMap);
        removeDeadMonsters(currentMap);    // Dọn quái chết (hp <= 0)
    }

    public void draw(Graphics2D g2, int currentMap) {
        List<Entity> all = new ArrayList<>();
        all.addAll(mM.getMonsters(currentMap));
        all.addAll(npcM.getNPCs(currentMap));
        all.add(player);

        // Vẽ theo worldY để có chiều sâu
        all.sort((a, b) -> Integer.compare(a.worldY, b.worldY));

        // Đồ vật vẽ trước (nằm dưới)
        all.addAll(0, oM.getObjects(currentMap));

        for (Entity e : all) e.draw(g2);
    }

    /** Player đang trong pha Active thì kiểm tra đụng quái và gây sát thương. */
    public void processPlayerHits(int currentMap) {
        if (player == null) return;
        if (!CombatSystem.isAttackActive(player.combat)) return;

        Rectangle attack = player.combat.getAttackBox();
        if (attack == null || attack.width <= 0 || attack.height <= 0) return;

        int rawDamage = Math.max(1, player.getATK());
        int[] knockback = computePlayerAttackKnockback(player); // {kx, ky}

        for (Entity e : mM.getMonsters(currentMap)) {
            if (!(e instanceof Monster m)) continue;
            if (m.isDead() || m.isInvulnerable()) continue;

            Rectangle monsterBody = CollisionUtil.getEntityBodyWorldRect(m);
            if (attack.intersects(monsterBody)) {
                DamageProcessor.applyDamage(m, rawDamage, knockback[0], knockback[1]);
                // Nếu muốn chống multi-hit trong 1 swing, bạn có thể gọi:
                // CombatSystem.markHitLanded(player.combat, m);
            }
        }
    }

    /** Gỡ quái đã chết khỏi danh sách của map. */
    public void removeDeadMonsters(int currentMap) {
        mM.getMonsters(currentMap).removeIf(e -> (e instanceof Monster m) && m.isDead());
    }

    /** Khi thân Player chạm thân quái thì Player nhận sát thương/knockback. */
    public void processMonsterContacts(int currentMap) {
        if (player == null || player.isDead()) return;

        Rectangle playerBody = CollisionUtil.getEntityBodyWorldRect(player);

        // Nới 1px để “chạm mép” cũng tính
        Rectangle playerContact = new Rectangle(playerBody);
        playerContact.grow(1, 1);

        for (Entity e : mM.getMonsters(currentMap)) {
            if (!(e instanceof Monster m)) continue;
            if (m.isDead()) continue;

            Rectangle monsterBody = CollisionUtil.getEntityBodyWorldRect(m);
            if (playerContact.intersects(monsterBody)) {
                if (!player.isInvulnerable()) {
                    int raw = Math.max(1, m.getATK());

                    // Knockback hướng từ quái -> player (dựa trên tâm)
                    int pcx = playerBody.x + playerBody.width  / 2;
                    int pcy = playerBody.y + playerBody.height / 2;
                    int mcx = monsterBody.x + monsterBody.width  / 2;
                    int mcy = monsterBody.y + monsterBody.height / 2;

                    int kx = Integer.compare(pcx, mcx) * 6; // lực đẩy 6px/frame (tuỳ chỉnh)
                    int ky = Integer.compare(pcy, mcy) * 6;

                    player.takeDamage(raw, kx, ky);

                    // Debug:
                    System.out.println("[CONTACT DMG] " + m.name + " -> Player HP="
                            + player.getHP() + "/" + player.getMaxHP());
                }
            }
        }
    }

    /** Knockback đẩy theo hướng tấn công của Player. */
    private int[] computePlayerAttackKnockback(Player p) {
        int force = 8; // px/frame — chỉnh theo cảm giác
        switch (p.direction) {
            case "up":    return new int[]{0, -force};
            case "down":  return new int[]{0,  force};
            case "left":  return new int[]{-force, 0};
            default:      return new int[]{ force, 0}; // right
        }
    }
}
