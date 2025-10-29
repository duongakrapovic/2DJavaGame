package monster_data;

import combat.CombatSystem;
import entity.Entity;
import main.GamePanel;
import player_manager.Player;
import java.awt.Rectangle;

public abstract class Monster extends Entity {

    // config combat
    protected int attackDamage         = 1;
    protected int attackKnockback      = 6;
    protected int attackTriggerRadius  = 36;
    protected int faceLockThreshold    = 4;
    protected int atkW, atkH;

    public Monster(GamePanel gp) {
        super(gp);

        this.combat.setAttackBoxSize(gp.tileSize, gp.tileSize * 3 / 2);
        this.combat.setTimingFrames(8, 6, 10, 30) ;
        this.attackTriggerRadius = Math.max(gp.tileSize, 48);
        this.faceLockThreshold = 6;
        this.atkW = gp.tileSize;
        this.atkH = gp.tileSize * 3 / 2;
        this.combat.setAttackBoxSize(atkW, atkH);
    }


    @Override
    public void update() {
        decideAttack();
        int preX = worldX, preY = worldY;
        boolean hold = CombatSystem.isAttacking(combat) ;
        super.update();
        if (hold) { worldX = preX; worldY = preY; }
    }


    protected void decideAttack() {
        Player p = gp.em.getPlayer();
        if (p == null || p.isDead()) return;

        final int px = p.worldX, py = p.worldY;
        final int dx = px - this.worldX, dy = py - this.worldY;

        // chose direction
        final int absDx = Math.abs(dx), absDy = Math.abs(dy);
        if (absDx - absDy > faceLockThreshold) {
            this.direction = (dx >= 0) ? "right" : "left";
        } else if (absDy - absDx > faceLockThreshold) {
            this.direction = (dy >= 0) ? "down" : "up";
        } else {
            if ("left".equals(direction) || "right".equals(direction)) {
                this.direction = (dx >= 0) ? "right" : "left";
            } else {
                this.direction = (dy >= 0) ? "down" : "up";
            }
        }

        Rectangle meBody = getSolidAreaWorld();
        Rectangle plBody = getSolidAreaWorld(p);

        Rectangle plFat = new Rectangle(plBody);
        plFat.grow(2, 2);

        // hit when body touch
        if (meBody.intersects(plFat)) {
            if (CombatSystem.canStartAttack(this.combat)) {
                CombatSystem.startAttack(this.combat, this);
                this.combat.clearHitThisSwing();
            }
            return;
        }

        // 4) REACH-RECTANGLE: extend body react toward movement direction
        Rectangle reach = new Rectangle(meBody);
        final int rw = (atkW > 0 ? atkW : gp.tileSize);
        final int rh = (atkH > 0 ? atkH : gp.tileSize);

        switch (this.direction) {
            case "up":
                reach.y      -= rh;
                reach.height += rh;
                break;
            case "down":
                reach.height += rh;
                break;
            case "left":
                reach.x     -= rw;
                reach.width += rw;
                break;
            default: // "right"
                reach.width += rw;
                break;
        }

        // Player in react => attack
        if (reach.intersects(plFat)) {
            if (CombatSystem.canStartAttack(this.combat)) {
                CombatSystem.startAttack(this.combat, this);
                this.combat.clearHitThisSwing();
            }
        }

    }

    // solid area
    protected Rectangle getSolidAreaWorld() {
        Rectangle sa = this.getSolidArea();
        return new Rectangle(this.worldX + sa.x, this.worldY + sa.y, sa.width, sa.height);
    }
    protected static Rectangle getSolidAreaWorld(Entity e) {
        Rectangle sa = e.getSolidArea();
        return new Rectangle(e.worldX + sa.x, e.worldY + sa.y, sa.width, sa.height);
    }

    @Override
    public void onDamaged(int damage) { }
    public void onDeath() { }
}