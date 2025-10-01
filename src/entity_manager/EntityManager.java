/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity_manager;

import entity.Entity;
import monster_data.Monster;
import player_manager.Player;
import input_manager.InputController;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import main.GamePanel;

public class EntityManager {

    private Player player;
    private MonsterManager mM;
    private NPCManager npcM;
    private ObjectManager oM;

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
        for (Entity o : oM.getObjects(currentMap)) 
            o.update();
        for (Entity m : mM.getMonsters(currentMap))
            m.update();
        for (Entity n : npcM.getNPCs(currentMap)) 
            n.update();
        processPlayerHits(currentMap);     // Player đánh trúng quái -> takeDamage(...)
        processMonsterContacts(currentMap);
        removeDeadMonsters(currentMap);    // Dọn quái chết (hp <= 0)
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
    public void processPlayerHits(int currentMap) {
        if (player == null || !player.getCombat().isAttackActive()) return;

        Rectangle atk = player.attackBox;     // dùng chung Rectangle với combat
        if (atk.width <= 0 || atk.height <= 0) return;

        int rawDmg = Math.max(1, player.getATK());
        int[] kb   = player.getCombat().getKnockbackVector();

        for (Entity e : mM.getMonsters(currentMap)) {
            if (!(e instanceof Monster m)) continue;
            Rectangle mBody = bodyRectWorld(m); // dùng helper đã viết
            if (atk.intersects(mBody)) {
                m.takeDamage(rawDmg, kb[0], kb[1]);
            }
        }
    }

    public void removeDeadMonsters(int currentMap) {
        mM.getMonsters(currentMap).removeIf(e -> e instanceof Monster && ((Monster) e).isDead());
    }
    // EntityManager.java (thêm dưới cùng class hoặc trước các hàm process)
    private java.awt.Rectangle bodyRectWorld(entity.Entity e) {
        int bx = e.worldX + (e.solidArea != null ? e.solidArea.x : 0);
        int by = e.worldY + (e.solidArea != null ? e.solidArea.y : 0);
        int bw = (e.solidArea != null ? e.solidArea.width  : e.width);
        int bh = (e.solidArea != null ? e.solidArea.height : e.height);
        return new java.awt.Rectangle(bx, by, bw, bh);
    }
    public void processMonsterContacts(int currentMap) {
        if (player == null || player.isDead()) return;

        // Thân thật của player theo WORLD
        java.awt.Rectangle pBody = bodyRectWorld(player);

        // Nở 1px để "chạm mép" cũng coi là va chạm
        java.awt.Rectangle pBodyContact = new java.awt.Rectangle(pBody);
        pBodyContact.grow(1, 1);

        for (entity.Entity e : mM.getMonsters(currentMap)) {
            if (!(e instanceof monster_data.Monster)) continue;
            monster_data.Monster m = (monster_data.Monster) e;
            if (m.isDead()) continue;

            java.awt.Rectangle mBody = bodyRectWorld(m);

            // Inclusive contact: dùng rect player đã nở 1px
            if (pBodyContact.intersects(mBody)) {
                // i-frame của Player sẽ tự chặn spam
                if (!player.isInvulnerable()) {
                    int raw = Math.max(1, m.getATK());

                    // Knockback từ quái -> player (theo tâm)
                    int pcx = pBody.x + pBody.width  / 2;
                    int pcy = pBody.y + pBody.height / 2;
                    int mcx = mBody.x + mBody.width  / 2;
                    int mcy = mBody.y + mBody.height / 2;

                    int kx = Integer.compare(pcx, mcx) * 6; // lực đẩy 6px/frame (tuỳ chỉnh)
                    int ky = Integer.compare(pcy, mcy) * 6;

                    player.takeDamage(raw, kx, ky);

                    // Debug: in HP ngay tại thời điểm dính đòn
                    System.out.println("[CONTACT DMG] " + m.name + " hit -> Player HP="
                            + player.getHP() + "/" + player.getMaxHP());
                }
            }
        }
    }

}


