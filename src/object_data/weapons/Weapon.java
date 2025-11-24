package object_data.weapons;

import entity.Entity;
import main.GamePanel;
import object_data.WorldObject;
import player_manager.Player;


/**
 * Base Weapon class:
 * - Là vật phẩm trong thế giới (extends WorldObject)
 * - Có config combat (damage, knockback, timing, spriteKey)
 */
public abstract class Weapon extends WorldObject {

    public Weapon(GamePanel gp) {
        this(gp, gp.currentMap);
    }

    // ✅ Constructor đầy đủ
    public Weapon(GamePanel gp, int mapIndex) {
        super(gp);
        this.mapIndex = mapIndex;
        collision = true;
        width = gp.tileSize;
        height = gp.tileSize;
        solidArea = new java.awt.Rectangle(8, 8, gp.tileSize - 16, gp.tileSize - 16);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    // ===================== ABSTRACT CONFIG =====================

    public abstract String spriteKey();

    public abstract int atkBoxW();
    public abstract int atkBoxH();
    public abstract int windup();
    public abstract int active();
    public abstract int recover();
    public abstract int cooldown();
    public abstract float atkMultiplier();
    public abstract int atkFlat();

    // ===================== COMMON UTILITY =====================


    public void loadSprite() {
        staticImage = setup("/object/" + spriteKey(), gp.tileSize, gp.tileSize);
    }

    // ===================== COMBAT LOGIC =====================
    // use later in CombatSystem
    public int computeDamage(Player p, Entity target) {
        int offensive = Math.round(p.getATK() * atkMultiplier()) + atkFlat();
        int def = Math.max(0, target.getDEF());
        float mitig = 100f / (100f + def * 10f);
        return Math.max(1, Math.round(offensive * mitig));
    }

}
